package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerRequestPacket
import java.nio.ByteBuffer

class RequestFriendPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val habbo = client.habbo!!

        val friendLimit = if (habbo.subscriptions.hasActiveHabboClubMembership()) {
            HotelSettings.habbos.messenger.maxFriendsWithHabboClub
        } else {
            HotelSettings.habbos.messenger.maxFriends
        }

        if (!habbo.hasPermission("uriel.messenger.unlimited_friends")
            && habbo.messenger.friendships.size >= friendLimit) return

        val username = packet.getString()

        // TODO: At some point replace with a Friend Request Error packet (when Nitro adds support for it)
        val targetHabboInfo = HabboManager.getHabboInfo(username) ?: return

        if (targetHabboInfo.id == habbo.info.id) return

        val request = FriendshipRequest {
            this.senderHabboInfo = habbo.info
            this.receiverHabboInfo = targetHabboInfo
        }

        MessengerManager.addFriendshipRequest(request)

        if (targetHabboInfo.isOnline) {
            MessengerRequestPacket(request).send(targetHabboInfo.habbo!!)
        }
    }

}