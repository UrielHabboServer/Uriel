package org.urielserv.uriel.packets.incoming.handshake

import io.klogging.logger
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.game.habbos.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.handshake.PingPacket
import org.urielserv.uriel.packets.outgoing.handshake.AuthenticationOKPacket
import org.urielserv.uriel.packets.outgoing.users.UserHomeRoomPacket
import org.urielserv.uriel.packets.outgoing.users.habbo_club.UserClubPacket
import org.urielserv.uriel.packets.outgoing.users.inventory.UserEffectsPacket
import org.urielserv.uriel.packets.outgoing.users.nux.NewUserExperienceStatusPacket
import java.io.ByteArrayInputStream

class SSOTicketPacketHandler : PacketHandler {

    private val logger = logger("uriel.packets.incoming.handshake.SecureLoginPacketHandler")

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        val ssoTicket = packet.readString().replace(" ", "")
        val timeTaken = packet.readInt()

        client.nitroTimeTaken = timeTaken

        if (ssoTicket.isEmpty()) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent an empty SSO ticket")
            return
        }

        if (client.habbo != null) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent a second SSO ticket")
            return
        }

        val habbo = HabboManager.loginHabbo(ssoTicket, client)

        if (habbo == null) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent an invalid SSO ticket")
            return
        }

        client.habbo = habbo

        if (HotelSettings.habbos.wardrobe.validateLooksOnLogin) {
            // Make sure the player's look is valid
            val validatedLook = ClothingValidator.validateLook(habbo)
            habbo.info.look = validatedLook
        }

        /*
        Packets to add:
        - UserClothesComposer
        - UserPermissionsComposer
        - AvailabilityStatusMessageComposer
        - EnableNotificationsComposer
        - UserAchievementScoreComposer
        - IsFirstLoginOfDayComposer
        - MysteryBoxKeysComposer
        - BuildersClubExpiredComposer
        - CfhTopicsMessageComposer
        - FavoriteRoomsCountComposer
        - GameCenterGameListComposer
        - GameCenterAccountInfoComposer
        - UserClubComposer
        - ModToolComposer
        - InventoryAchievementsComposer
         */

        AuthenticationOKPacket().send(client)
        UserHomeRoomPacket(
            homeRoomId = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId,
            roomToEnterId = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId
        ).send(client)
        UserEffectsPacket(habbo.inventory.effects.toList()).send(client)
        NewUserExperienceStatusPacket(true).send(client)
        UserClubPacket(habbo, "HABBO_CLUB", UserClubPacket.RESPONSE_TYPE_LOGIN).send(client)
        PingPacket().send(client)
    }

}