package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerInvitePacket
import java.nio.ByteBuffer

class SendRoomInvitePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val habbo = client.habbo!!

        val userAmount = packet.getInt()
        val users = mutableListOf<Int>()
        for (i in 0..<userAmount) {
            users.add(packet.getInt())
        }

        val message = packet.getString()

        val habbos = users
            .mapNotNull { HabboManager.getHabboInfoById(it)?.habbo }
            .filter { // friend
                habbo.messenger.isFriend(it.info)
            }

        MessengerInvitePacket(habbo, message).broadcast(habbos)
    }

}