package org.urielserv.uriel.packets.incoming.users.inventory.badges

import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.inventory.badges.UserCurrentBadgesPacket
import java.nio.ByteBuffer

class UserBadgesCurrentUpdatePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        repeat(HotelSettings.habbos.badges.maxActiveBadges) {
            val slot = packet.getInt()
            val badge = packet.getString()

            if (badge.isEmpty()) {
                habbo.inventory.badges.removeBadge(slot)
            } else {
                habbo.inventory.badges.updateBadge(badge, slot)
            }
        }

        if (habbo.room == null) {
            UserCurrentBadgesPacket(habbo).send(habbo)
        } else {
            UserCurrentBadgesPacket(habbo).broadcast(habbo.room!!)
        }
    }

}