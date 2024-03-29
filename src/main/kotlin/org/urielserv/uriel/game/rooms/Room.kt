package org.urielserv.uriel.game.rooms

import org.urielserv.uriel.Configuration
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.room_unit.HabboRoomUnit
import org.urielserv.uriel.game.rooms.tiles.RoomTileMap
import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.landing_view.DesktopViewPacket
import org.urielserv.uriel.packets.outgoing.rooms.*
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitPacket
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitRemovePacket
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitStatusPacket
import org.urielserv.uriel.tick_loop.TickLoop

class Room internal constructor(
    val info: RoomInfo,
) {

    private val _habbos = mutableListOf<Habbo>()
    val habbos: List<Habbo>
        get() = _habbos.toList()

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

        info.users = _habbos.size
        info.flushChanges()

        isLoaded = true
    }

    suspend fun enter(habbo: Habbo, force: Boolean = false) {
        if (habbo.room != null) {
            habbo.room!!.leave(habbo, false)
        }

        if (!isLoaded) {
            load()
        }

        // TODO: Add support for doorbell, password and room bans

        prepareEnter(habbo)
    }

    private suspend fun prepareEnter(habbo: Habbo) {
        habbo.room = this
        _habbos.add(habbo)

        habbo.messenger.sendUpdateToFriends()

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

        info.users = _habbos.size
        info.flushChanges()

        RoomInfoOwnerPacket(this, habbo.info.id == info.ownerHabboInfo.id).send(habbo)
        RoomThicknessPacket(this).send(habbo)
        RoomInfoPacket(this, roomForward = false, roomEnter = true).send(habbo)

        RoomUnitPacket(_habbos).broadcast(this)
        RoomUnitStatusPacket(_habbos).broadcast(this)
    }

    suspend fun leave(habbo: Habbo, goToDesktopView: Boolean = true) {
        RoomUnitRemovePacket(habbo).broadcast(this)

        habbo.room = null
        habbo.roomUnit = null

        _habbos.remove(habbo)

        info.users = _habbos.size
        info.flushChanges()

        for (tile in tileMap!!.tiles.flatten()) {
            tile.roomUnitsOnTile.removeIf { it.habbo == habbo }
        }

        if (goToDesktopView)
            DesktopViewPacket().send(habbo)

        if (_habbos.isEmpty()) {
            unload()
        }
    }

    suspend fun unload() {
        if (!isLoaded) return

        for (habbo in _habbos) {
            leave(habbo)
        }

        tickLoop?.end()
        tickLoop = null

        roomUnitCounter = 0

        tileMap = null

        isLoaded = false
    }

    fun getHabboByUsername(username: String): Habbo? {
        return _habbos.find { it.info.username == username }
    }

}