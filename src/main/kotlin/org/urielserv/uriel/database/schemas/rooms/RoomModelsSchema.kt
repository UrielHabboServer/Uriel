package org.urielserv.uriel.database.schemas.rooms

import org.ktorm.schema.*
import org.urielserv.uriel.game.rooms.RoomModel

object RoomModelsSchema : Table<RoomModel>("room_models") {

    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }

    val heightmap = text("heightmap").bindTo { it.heightmap }

    val isClubOnly = boolean("is_club_only").bindTo { it.isClubOnly }
    val isCustom = boolean("is_custom").bindTo { it.isCustom }

    val doorX = int("door_x").bindTo { it.doorX }
    val doorY = int("door_y").bindTo { it.doorY }
    val doorDirection = int("door_direction").bindTo { it.doorDirection }

}