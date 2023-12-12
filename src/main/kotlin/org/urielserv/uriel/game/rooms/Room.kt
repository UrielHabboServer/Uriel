package org.urielserv.uriel.game.rooms

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboRoomState
import org.urielserv.uriel.game.rooms.tiles.RoomTileMap
import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.rooms.*

class Room internal constructor(
    val info: RoomInfo,
) {

    private val habbos = mutableListOf<Habbo>()

    var tileMap: RoomTileMap? = null
        private set

    private var isLoaded = false

    private fun load() {
        tileMap = RoomTileMap(info.model)

        isLoaded = true
    }

    suspend fun enter(habbo: Habbo, force: Boolean = false) {
        if (!isLoaded) {
            load()
        }

        if (habbo.room != null && habbo.room != this) {
            habbo.room!!.leave(habbo)
        }

        // TODO: Add support for doorbell, password and room bans

        prepareEnter(habbo)
    }

    private suspend fun prepareEnter(habbo: Habbo) {
        habbo.room = this
        habbos.add(habbo)

        RoomOpenPacket().send(habbo)
        RoomModelNamePacket(this).send(habbo)

        if (info.wallpaper != "0.0") {
            RoomPaintPacket("wallpaper", info.wallpaper).send(habbo)
        }

        if (info.floorPattern != "0.0") {
            RoomPaintPacket("floor", info.floorPattern).send(habbo)
        }

        RoomPaintPacket("landscape", info.landscape).send(habbo)

        // After this, the client sends a FurnitureAlisases / RoomModel packet, which will trigger finishEnter
    }

    internal suspend fun finishEnter(habbo: Habbo) {
        habbo.roomState = HabboRoomState(
            habbo,
            tileMap!!.doorTile,
            tileMap!!.doorDirection
        )

        info.users++
        info.flushChanges()

        RoomInfoOwnerPacket(this, habbo.info.id == info.ownerHabboInfo.id).send(habbo)
        RoomThicknessPacket(this).send(habbo)
        RoomInfoPacket(this, habbo, roomForward = false, roomEnter = true).send(habbo)
    }

    fun leave(habbo: Habbo) {
        habbo.room = null
        habbo.roomState = null

        habbos.remove(habbo)
    }

    fun getHabbos(): List<Habbo> {
        return habbos
    }

    fun appendToPacket(packet: Packet) {
        packet.appendInt(info.id)
        packet.appendString(info.name)

        if (info.isPublic) {
            packet.appendInt(0)
            packet.appendString("")
        } else {
            packet.appendInt(info.ownerHabboInfo.id)
            packet.appendString(info.ownerHabboInfo.username)
        }

        packet.appendInt(info.accessType.ordinal)

        packet.appendInt(info.users)
        packet.appendInt(info.maximumUsers)

        packet.appendString(info.description)

        packet.appendInt(0)
        packet.appendInt(info.score)

        packet.appendInt(0)
        packet.appendInt(info.flatCategory.id)

        val tags = info.tags.split(";")
        packet.appendInt(tags.size)
        for (tag in tags) {
            packet.appendString(tag)
        }

        var base = 0

        /*
        TODO: Add Guild to base
        if (info.guildId > 0) {
            base = base or 2
        }
         */

        /*
        TODO: Add support for Promotions
        if (info.isPromoted) {
            base = base or 4
        }
         */

        if (!info.isPublic) {
            base = base or 8
        }

        packet.appendInt(base)

        // TODO: Add Guild to packet

        // TODO: Add Promotion Info to packet
    }

}