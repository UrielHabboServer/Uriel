package org.urielserv.uriel.packets.incoming.users.inventory.badges

import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.toList
import org.urielserv.uriel.Database
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.core.database.schemas.users.UserBadgesSchema
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.inventory.badges.UserCurrentBadgesPacket
import java.nio.ByteBuffer

class UserBadgesCurrentPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val targetId = packet.getInt()
        val target = HabboManager.getHabboInfo(targetId) ?: return

        if (target.habbo != null) {
            UserCurrentBadgesPacket(target.habbo!!).send(habbo)
        } else {
            val badges = Database.sequenceOf(UserBadgesSchema)
                .filter { it.userId eq targetId }
                .toList()

            UserCurrentBadgesPacket(target, badges).send(habbo)
        }
    }

}