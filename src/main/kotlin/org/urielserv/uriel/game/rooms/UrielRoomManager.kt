package org.urielserv.uriel.game.rooms

import io.klogging.noCoLogger
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.rooms.RoomModelsSchema
import org.urielserv.uriel.core.database.schemas.rooms.RoomsSchema
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.navigator.NavigatorFlatCategory
import org.urielserv.uriel.game.navigator.NavigatorPublicCategory

/**
 * The UrielRoomManager class is responsible for managing the rooms in the game.
 */
class UrielRoomManager {

    private val logger = noCoLogger<UrielRoomManager>()

    private val _rooms = mutableMapOf<Int, Room>()
    val rooms: List<Room>
        get() = _rooms.values.toList()

    private val roomModels = mutableMapOf<String, RoomModel>()

    init {
        Database.sequenceOf(RoomModelsSchema).forEach {
            roomModels[it.name] = it
        }
    }

    fun getRoomModel(name: String): RoomModel? {
        return roomModels[name]
    }

    /**
     * Gets a Room by its ID.
     *
     * @param id The ID of the Room to get.
     * @return The Room with the specified ID, or null if no Room with that ID exists.
     */
    fun getRoom(id: Int): Room? {
        return _rooms[id] ?: buildRoom(id)
    }

    fun getRooms(category: NavigatorPublicCategory): List<Room> {
        return Database.sequenceOf(RoomsSchema)
            .filter { it.publicCategory eq category.id }
            .map { getRoom(it.id) }
            .filterNotNull()
    }

    fun getRooms(category: NavigatorFlatCategory): List<Room> {
        return Database.sequenceOf(RoomsSchema)
            .filter { it.flatCategory eq category.id }
            .map { getRoom(it.id) }
            .filterNotNull()
    }

    fun getRooms(ownerId: Int): List<Room> {
        return Database.sequenceOf(RoomsSchema)
            .filter { it.ownerId eq ownerId }
            .map { getRoom(it.id) }
            .filterNotNull()
    }

    fun getRooms(habbo: Habbo): List<Room> {
        return Database.sequenceOf(RoomsSchema)
            .filter { it.ownerId eq habbo.info.id }
            .map { getRoom(it.id) }
            .filterNotNull()
    }

    val publicRooms: List<Room>
        get() = _rooms.values.filter {
            it.info.isPublic
        }

    val popularRooms: List<Room>
        get() = _rooms.values.filter {
            it.info.users > 0
        }.sortedByDescending {
            it.info.users
        }

    fun createRoom(
        owner: Habbo,
        name: String,
        description: String,
        model: RoomModel,
        flatCategory: NavigatorFlatCategory,
        maxVisitors: Int,
        tradingMode: Int
    ): Room? {
        if (!owner.hasPermission("uriel.rooms.can_create_rooms")) return null

        try {
            val roomInfo = RoomInfo {
                this.ownerHabboInfo = owner.info

                this.name = name
                this.description = description
                this.flatCategory = flatCategory

                this.model = model

                this.maximumUsers = maxVisitors
                this.tradingMode = tradingMode
            }

            Database.sequenceOf(RoomsSchema).add(roomInfo)

            return buildRoom(roomInfo.id)
        } catch (exc: Exception) {
            logger.error("Failed to create room")
            exc.printStackTrace()
            return null
        }
    }

    /**
     * Builds a Room based on the provided Room ID.
     *
     * @param roomId The ID of the room to build.
     * @return A Room object if a Room with the provided ID is found in the database, otherwise null.
     */
    private fun buildRoom(roomId: Int): Room? {
        val roomInfo = Database.sequenceOf(RoomsSchema)
            .find {
                it.id eq roomId
            } ?: return null

        roomInfo.users = 0
        roomInfo.flushChanges()

        _rooms[roomId] = Room(roomInfo)

        return _rooms[roomId]
    }

    internal suspend fun shutdown() {
        for (room in _rooms.values) {
            room.unload()
        }
    }

}