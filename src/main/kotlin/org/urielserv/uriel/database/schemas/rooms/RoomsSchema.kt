package org.urielserv.uriel.database.schemas.rooms

import org.ktorm.schema.*
import org.urielserv.uriel.game.rooms.RoomAccessType

object RoomsSchema : Table<Nothing>("rooms") {

    val id = int("id").primaryKey()
    val ownerId = int("owner_id")

    val name = varchar("name")
    val description = text("description")
    val category = int("category")
    val tags = text("tags")

    val model = varchar("model")
    val isCustomModel = boolean("is_custom_model")

    val accessType = enum<RoomAccessType>("access_type")
    val password = varchar("password")

    val users = int("users")
    val maxUsers = int("max_users")

    val score = int("score")

    val isPublic = boolean("is_public")
    val isStaffPicked = boolean("is_staff_picked")

    val creationTimestamp = timestamp("creation_timestamp")

    val wallpaper = varchar("wallpaper")
    val floorPattern = varchar("floor_pattern")
    val landscape = varchar("landscape")

    val wallThickness = int("wall_thickness")
    val floorThickness = int("floor_thickness")
    val wallHeight = int("wall_height")

    val areWallsHidden = boolean("are_walls_hidden")
    val areWiredsHidden = boolean("are_wired_hidden")

    val canWalkthrough = boolean("can_walkthrough")
    val canMoveDiagonally = boolean("can_move_diagonally")

    val allowOtherPets = boolean("allow_other_pets")
    val allowOtherPetsToEat = boolean("allow_other_pets_to_eat")

    val chatMode = int("chat_mode")
    val chatWeight = int("chat_weight")
    val chatScrollingSpeed = int("chat_scrolling_speed")
    val chatHearingDistance = int("chat_hearing_distance")
    val chatFloodProtection = int("chat_flood_protection")

    val tradingMode = int("trading_mode")

    val rollerSpeed = int("roller_speed")

    val isPromoted = boolean("is_promoted")
    val isForSale = boolean("is_for_sale")

}