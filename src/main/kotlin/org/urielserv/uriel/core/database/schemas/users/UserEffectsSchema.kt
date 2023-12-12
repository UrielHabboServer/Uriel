package org.urielserv.uriel.core.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.urielserv.uriel.game.habbos.inventory.effects.Effect

object UserEffectsSchema : Table<Effect>("user_effects") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val effectId = int("effect_id").bindTo { it.effectId }

    val duration = int("duration").bindTo { it.duration }
    val quantity = int("quantity").bindTo { it.quantity }

    val activationTimestamp = int("activation_timestamp").bindTo { it.activationTimestamp }
    val isActive = boolean("is_active").bindTo { it.isActive }

}