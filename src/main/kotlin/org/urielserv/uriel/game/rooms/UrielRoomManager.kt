package org.urielserv.uriel.game.rooms

import io.klogging.logger
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.rooms.RoomsSchema

/**
 * The UrielRoomManager class is responsible for managing the rooms in the game.
 */
class UrielRoomManager {

    private val logger = logger<UrielRoomManager>()

    private val rooms = mutableMapOf<Int, Room>()

    /**
     * Gets a Room by its ID.
     *
     * @param id The ID of the Room to get.
     * @return The Room with the specified ID, or null if no Room with that ID exists.
     */
    fun getRoomById(id: Int): Room? {
        return rooms[id] ?: buildRoom(id)
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

        return Room(roomInfo)
    }

}