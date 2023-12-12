package org.urielserv.uriel.packets.incoming.handshake

import io.klogging.logger
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.users.UserLoginEvent
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.game.habbos.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.handshake.AuthenticatedPacket
import org.urielserv.uriel.packets.outgoing.handshake.ClientPingPacket
import org.urielserv.uriel.packets.outgoing.users.NoobnessLevelPacket
import org.urielserv.uriel.packets.outgoing.users.UserHomeRoomPacket
import org.urielserv.uriel.packets.outgoing.users.inventory.UserEffectListPacket
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket
import java.io.ByteArrayInputStream

class SecurityTicketPacketHandler : PacketHandler {

    private val logger = logger(SecurityTicketPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        val ticket = packet.readString().replace(" ", "")
        val time = packet.readInt()

        if (client.nitroInformation != null) {
            client.nitroInformation!!.time = time
        }

        if (ticket.isEmpty()) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent an empty SSO ticket")
            return
        }

        if (client.habbo != null) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent a second SSO ticket")
            return
        }

        val habbo = HabboManager.loginHabbo(ticket, client)

        if (habbo == null) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent an invalid SSO ticket")
            return
        }

        client.habbo = habbo

        val event = UserLoginEvent(habbo, ticket)
        EventDispatcher.dispatch(Events.UserLogin, event)

        if (event.isCancelled) {
            habbo.disconnect()
            return
        }

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

        AuthenticatedPacket().send(client)
        UserHomeRoomPacket(
            homeRoomId = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId,
            roomIdToEnter = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId
        ).send(client)
        UserEffectListPacket(habbo.inventory.effects.toList()).send(client)
        NoobnessLevelPacket(NoobnessLevelPacket.NEW_IDENTITY).send(client)
        UserSubscriptionPacket(habbo, "HABBO_CLUB", UserSubscriptionPacket.RESPONSE_TYPE_LOGIN).send(client)
        ClientPingPacket().send(client)

        // Add default navigator saved searches

    }

}