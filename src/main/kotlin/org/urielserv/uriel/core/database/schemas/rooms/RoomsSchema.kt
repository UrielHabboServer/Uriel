package org.urielserv.uriel.core.database.schemas.rooms

import org.ktorm.schema.*
import org.urielserv.uriel.core.database.schemas.navigator.NavigatorFlatCategoriesSchema
import org.urielserv.uriel.core.database.schemas.navigator.NavigatorPublicCategoriesSchema
import org.urielserv.uriel.core.database.schemas.users.UsersSchema
import org.urielserv.uriel.game.rooms.RoomAccessType
import org.urielserv.uriel.game.rooms.RoomInfo

object RoomsSchema : Table<RoomInfo>("rooms") {

    val id = int("id").primaryKey().bindTo { it.id }
    val ownerId = int("owner_id").references(UsersSchema) { it.ownerHabboInfo }

    val name = varchar("name").bindTo { it.name }
    val description = text("description").bindTo { it.description }
    val flatCategory = int("navigator_flat_category_id").references(NavigatorFlatCategoriesSchema) { it.flatCategory }
    val publicCategory =
        int("navigator_public_category_id").references(NavigatorPublicCategoriesSchema) { it.publicCategory }
    val tags = text("tags").bindTo { it.tags }

    val modelId = int("model_id").references(RoomModelsSchema) { it.model }

    val accessType = enum<RoomAccessType>("access_type").bindTo { it.accessType }
    val password = varchar("password").bindTo { it.password }

    val users = int("users").bindTo { it.users }
    val maximumUsers = int("maximum_users").bindTo { it.maximumUsers }

    val score = int("score").bindTo { it.score }

    val isPublic = boolean("is_public").bindTo { it.isPublic }
    val isStaffPicked = boolean("is_staff_picked").bindTo { it.isStaffPicked }

    val creationTimestamp = int("creation_timestamp").bindTo { it.creationTimestamp }

    val wallpaper = varchar("wallpaper").bindTo { it.wallpaper }
    val floorPattern = varchar("floor_pattern").bindTo { it.floorPattern }
    val landscape = varchar("landscape").bindTo { it.landscape }

    val wallThickness = int("wall_thickness").bindTo { it.wallThickness }
    val floorThickness = int("floor_thickness").bindTo { it.floorThickness }
    val wallHeight = int("wall_height").bindTo { it.wallHeight }

    val areWallsHidden = boolean("are_walls_hidden").bindTo { it.areWallsHidden }
    val areWiredsHidden = boolean("are_wireds_hidden").bindTo { it.areWiredsHidden }

    val allowWalkthrough = boolean("allow_walkthrough").bindTo { it.allowWalkthrough }
    val allowDiagonalMovement = boolean("allow_diagonal_movement").bindTo { it.allowDiagonalMovement }

    val allowOtherPets = boolean("allow_other_pets").bindTo { it.allowOtherPets }
    val allowOtherPetsToEat = boolean("allow_other_pets_to_eat").bindTo { it.allowOtherPetsToEat }

    val chatMode = int("chat_mode").bindTo { it.chatMode }
    val chatWeight = int("chat_weight").bindTo { it.chatWeight }
    val chatScrollingSpeed = int("chat_scrolling_speed").bindTo { it.chatScrollingSpeed }
    val chatHearingDistance = int("chat_hearing_distance").bindTo { it.chatHearingDistance }
    val chatFloodProtection = int("chat_flood_protection").bindTo { it.chatFloodProtection }

    val whoCanKick = int("who_can_kick").bindTo { it.whoCanKick }
    val whoCanBan = int("who_can_ban").bindTo { it.whoCanBan }
    val whoCanMute = int("who_can_mute").bindTo { it.whoCanMute }

    val tradingMode = int("trading_mode").bindTo { it.tradingMode }

    val rollerSpeed = int("roller_speed").bindTo { it.rollerSpeed }

    val isPromoted = boolean("is_promoted").bindTo { it.isPromoted }
    val isForSale = boolean("is_for_sale").bindTo { it.isForSale }

}