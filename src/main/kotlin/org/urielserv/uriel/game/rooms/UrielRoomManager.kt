package org.urielserv.uriel.game.rooms

import io.klogging.logger

/**
 * The UrielRoomManager class is responsible for managing the rooms in the game.
 */
class UrielRoomManager {

    private val logger = logger<UrielRoomManager>()

    private val rooms = mutableMapOf<Int, Room>()

    /**
     * Gets a room by its ID.
     *
     * @param id The ID of the room to get.
     * @return The room with the specified ID, or null if no room with that ID exists.
     */
    fun getRoomById(id: Int): Room? {
        return rooms[id]
    }

}