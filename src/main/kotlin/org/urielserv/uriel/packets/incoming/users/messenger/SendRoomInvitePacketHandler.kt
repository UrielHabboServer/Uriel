package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class SendRoomInvitePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val userAmount = packet.getInt()
        val users = mutableListOf<Int>()
        for (i in 0..<userAmount) {
            users.add(packet.getInt())
        }

        val message = packet.getString()

        users
            .mapNotNull { HabboManager.getHabboInfo(it)?.habbo }
            .filter { // friend
                habbo.messenger.isFriend(it.info)
            }
            .map { // friendship
                habbo.messenger.getFriendship(it)!!
            }
            .forEach {
                it.sendInvite(habbo, message)
            }
    }

}