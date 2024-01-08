package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerSearchPacket
import java.nio.ByteBuffer

class MessengerFindFriendsPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val habbo = client.habbo!!

        val query = packet.getString()

        // filter out any habbo infos that are friends with the habbo
        val regularSearchResults = HabboManager.searchHabboInfos(query)
            .filter { habboInfo ->
                !habbo.messenger.isFriend(habboInfo)
            }

        val friendSearchResults = habbo.messenger.friendships
            .filter { friendship ->
                friendship.other(habbo.info).username.startsWith(query, true)
            }
            .map { friendship ->
                friendship.other(habbo.info)
            }

        MessengerSearchPacket(
            unknownUsers = regularSearchResults,
            friendUsers = friendSearchResults
        ).send(client)
    }

}