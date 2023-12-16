package org.urielserv.uriel.game.rooms

import org.urielserv.uriel.Configuration
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.room_unit.HabboRoomUnit
import org.urielserv.uriel.game.rooms.tiles.RoomTileMap
import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.landing_view.DesktopViewPacket
import org.urielserv.uriel.packets.outgoing.rooms.*
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitPacket
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitStatusPacket
import org.urielserv.uriel.tick_loop.TickLoop

class Room internal constructor(
    val info: RoomInfo,
) {

    private val habbos = mutableListOf<Habbo>()

    var tickLoop: TickLoop? = null
        private set
    private var roomUnitCounter = 0

    var tileMap: RoomTileMap? = null
        private set

    private var isLoaded = false

    private fun load() {
        tickLoop = TickLoop(
            name = "Room ${info.id}",
            ticksPerSecond = Configuration.tickLoops.roomTicksPerSecond
        )

        tileMap = RoomTileMap(this, info.model)

        info.users = habbos.size
        info.flushChanges()

        isLoaded = true
    }

    suspend fun enter(habbo: Habbo, force: Boolean = false) {
        if (!isLoaded) {
            load()
        }

        if (habbo.room != null) {
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
        habbo.roomUnit = HabboRoomUnit(
            id = roomUnitCounter++,
            habbo = habbo,
            room = this,
            currentTile = tileMap!!.doorTile,
            bodyRotation = tileMap!!.doorDirection
        )

        info.users = habbos.size
        info.flushChanges()

        RoomInfoOwnerPacket(this, habbo.info.id == info.ownerHabboInfo.id).send(habbo)
        RoomThicknessPacket(this).send(habbo)
        RoomInfoPacket(this, habbo, roomForward = false, roomEnter = true).send(habbo)

        RoomUnitPacket(habbos).broadcast(this)
        RoomUnitStatusPacket(habbos).broadcast(this)
    }

    suspend fun leave(habbo: Habbo) {
        habbo.room = null
        habbo.roomUnit = null

        habbos.remove(habbo)

        info.users = habbos.size
        info.flushChanges()

        for (tile in tileMap!!.tiles.flatten()) {
            tile.roomUnitsOnTile.removeIf { it.habbo == habbo }
        }

        DesktopViewPacket().send(habbo)

        if (habbos.isEmpty()) {
            unload()
        }
    }

    fun getHabbos(): List<Habbo> {
        return habbos
    }

    fun getHabboByUsername(username: String): Habbo? {
        return habbos.find { it.info.username == username }
    }

    suspend fun unload() {
        if (!isLoaded) return

        for (habbo in habbos) {
            leave(habbo)
        }

        tickLoop?.end()
        tickLoop = null

        roomUnitCounter = 0

        tileMap = null

        isLoaded = false
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