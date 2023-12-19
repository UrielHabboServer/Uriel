package org.urielserv.uriel.packets.outgoing.rooms.heightmap

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomHeightmapPacket(
    private val room: Room
) : Packet() {

    override val packetId = Outgoing.RoomHeightmap

    override suspend fun construct() {
        if (room.tileMap == null) return

        appendInt(room.tileMap!!.tileCount / room.tileMap!!.length)
        appendInt(room.tileMap!!.tileCount)
        for (y in 0..<room.tileMap!!.length) {
            for (x in 0..<room.tileMap!!.width) {
                val tile = room.tileMap!!.getTile(x, y)

                if (tile == null) appendShort(Short.MAX_VALUE)

                val customStacking = true
                if (customStacking) {
                    appendShort((tile!!.height * 256.0).toInt().toShort())
                }/*
                     else {
                        appendShort(tile.height.toShort())
                    }
                    */
            }
        }
    }

}