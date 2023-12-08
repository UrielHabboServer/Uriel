package org.urielserv.uriel.game.navigator

import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.filter
import org.urielserv.uriel.Database
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.database.schemas.rooms.RoomsSchema
import org.urielserv.uriel.game.rooms.Room

interface NavigatorPublicCategory : Entity<NavigatorPublicCategory> {

    val id: Int
    var name: String

    var hasImage: Boolean
    var isVisible: Boolean
    var order: Int

    fun getRooms(): List<Room> {
        val roomInfos = Database.sequenceOf(RoomsSchema)
            .filter {
                it.publicCategory eq id
            }

        val rooms = mutableListOf<Room>()

        for (roomInfo in roomInfos) {
            RoomManager.getRoomById(roomInfo.id)?.let {
                rooms.add(it)
            }
        }

        return rooms.toList()
    }

}