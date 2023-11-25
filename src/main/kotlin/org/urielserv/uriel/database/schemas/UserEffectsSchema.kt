package org.urielserv.uriel.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.enum
import org.ktorm.schema.int
import org.urielserv.uriel.database.enums.Bool

object UserEffectsSchema : Table<Nothing>("user_effects") {

    val id = int("id").primaryKey()
    val userId = int("user_id")

    val effectId = int("effect_id")

    val startTimestamp = int("start_timestamp")
    val endTimestamp = int("end_timestamp")

    val amount = int("amount")

    val isActive = enum<Bool>("is_active")

}