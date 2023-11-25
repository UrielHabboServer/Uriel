package org.urielserv.uriel.packets.incoming.handshake

import io.klogging.logger
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.WardrobeManager
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readShort
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.PacketHandler
import org.urielserv.uriel.packets.outgoing.handshake.PingPacket
import org.urielserv.uriel.packets.outgoing.handshake.SecureLoginOKPacket
import org.urielserv.uriel.packets.outgoing.users.UserHomeRoomPacket
import java.io.ByteArrayInputStream

class SecureLoginPacketHandler : PacketHandler {

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
            val validatedLook = WardrobeManager.validateLook(habbo)
            habbo.info.look = validatedLook
        }

        /*
                messages.add(new UserEffectsListComposer(habbo, this.client.getHabbo().getInventory().getEffectsComponent().effects.values()).compose());
                messages.add(new UserClothesComposer(this.client.getHabbo()).compose());
                messages.add(new NewUserIdentityComposer(habbo).compose()); - probably really important
                messages.add(new UserPermissionsComposer(this.client.getHabbo()).compose());
                messages.add(new AvailabilityStatusMessageComposer(true, false, true).compose());
                messages.add(new PingComposer().compose()); - ADDED
                messages.add(new EnableNotificationsComposer(Emulator.getConfig().getBoolean("bubblealerts.enabled", true)).compose());
                messages.add(new UserAchievementScoreComposer(this.client.getHabbo()).compose());
                messages.add(new IsFirstLoginOfDayComposer(true).compose());
                messages.add(new MysteryBoxKeysComposer().compose());
                messages.add(new BuildersClubExpiredComposer().compose());
                messages.add(new CfhTopicsMessageComposer().compose());
                messages.add(new FavoriteRoomsCountComposer(this.client.getHabbo()).compose());
                messages.add(new GameCenterGameListComposer().compose());
                messages.add(new GameCenterAccountInfoComposer(3, 100).compose());
                messages.add(new GameCenterAccountInfoComposer(0, 100).compose());

                messages.add(new UserClubComposer(this.client.getHabbo(), SubscriptionHabboClub.HABBO_CLUB, UserClubComposer.RESPONSE_TYPE_LOGIN).compose());

                if (this.client.getHabbo().hasPermission(Permission.ACC_SUPPORTTOOL)) {
                    messages.add(new ModToolComposer(this.client.getHabbo()).compose());
                }

                this.client.sendResponses(messages);

                //Hardcoded
                //this.client.sendResponse(new ForumsTestComposer());
                this.client.sendResponse(new InventoryAchievementsComposer());
         */

        SecureLoginOKPacket().send(client)
        UserHomeRoomPacket(
            homeRoomId = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId,
            roomToEnterId = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId
        ).send(client)
        PingPacket().send(client)
    }

}