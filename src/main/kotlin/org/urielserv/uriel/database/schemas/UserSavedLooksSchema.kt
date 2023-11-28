package org.urielserv.uriel.database.schemas

import org.ktorm.schema.*
import org.urielserv.uriel.game.habbos.HabboGender

object UserSavedLooksSchema : Table<Nothing>("user_looks") {

    val id = int("id").primaryKey()
    val userId = int("user_id")

    val slotId = int("slot_id")
    val look = text("look")
    val gender = enum<HabboGender>("gender")

}