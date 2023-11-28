package org.urielserv.uriel.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int

object UserEffectsSchema : Table<Nothing>("user_effects") {

    val id = int("id").primaryKey()
    val userId = int("user_id")

    val effectId = int("effect_id")

    val duration = int("duration")
    val quantity = int("quantity")

    val activationTimestamp = int("activation_timestamp")
    val isActive = boolean("is_active")

}