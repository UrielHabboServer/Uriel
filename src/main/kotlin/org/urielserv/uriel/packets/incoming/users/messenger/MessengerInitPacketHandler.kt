package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerFriendsPacket
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerInitPacket
import java.nio.ByteBuffer
import kotlin.math.ceil

class MessengerInitPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        MessengerInitPacket(habbo).send(client)

        val friends = habbo.messenger.friendships
        val batchSize = 750
        val totalFragments = ceil(friends.size.toDouble() / batchSize).toInt()
        for (i in 0..<totalFragments) {
            val startIndex = i * batchSize
            val endIndex = minOf((i + 1) * batchSize, friends.size)

            MessengerFriendsPacket(
                habbo = habbo,
                totalFragments = totalFragments,
                fragmentNumber = i + 1,
                friendships = friends.subList(startIndex, endIndex)
            ).send(client)
        }

        habbo.messenger.sendUpdateToFriends()
        habbo.messenger.checkForOfflineMessages()
    }

}