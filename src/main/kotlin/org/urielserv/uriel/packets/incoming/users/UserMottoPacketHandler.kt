package org.urielserv.uriel.packets.incoming.users

import io.klogging.logger
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitInfoPacket
import java.nio.ByteBuffer

class UserMottoPacketHandler : PacketHandler {

    private val logger = logger(UserMottoPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val motto = packet.getString()

        if (motto.length > HotelSettings.habbos.mottoMaxLength) {
            logger.warn("Habbo ${habbo.info.username} attempted to set a motto that was too long! (${motto.length} > ${HotelSettings.habbos.mottoMaxLength})")
            return
        }

        habbo.info.motto = motto

        if (habbo.room != null) {
            RoomUnitInfoPacket(habbo).broadcast(habbo.room!!)
        }
    }

}