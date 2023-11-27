package org.urielserv.uriel.game.habbos.inventory.effects

import org.ktorm.database.iterator
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.UserEffectsSchema
import org.urielserv.uriel.game.habbos.Habbo

class InventoryEffects(
    val habbo: Habbo
) : Iterable<Effect> {

    private val effects = mutableListOf<Effect>()

    init {
        Database.from(UserEffectsSchema)
            .select()
            .where(UserEffectsSchema.userId eq habbo.id)
            .rowSet
            .iterator()
            .forEach { row ->
                effects.add(
                    Effect(
                        row[UserEffectsSchema.id]!!,
                        habbo,
                        row[UserEffectsSchema.effectId]!!,
                        row[UserEffectsSchema.duration]!!,
                        row[UserEffectsSchema.quantity]!!,
                        row[UserEffectsSchema.activationTimestamp]!!,
                        row[UserEffectsSchema.isActive]!!.toBoolean()
                    )
                )
            }
    }

    fun checkIfExpired() {
        effects.forEach { it.checkIfExpired() }
    }

    fun hasActiveEffect(effectId: Int): Boolean =
        effects.any { it.effectId == effectId && it.isActive }

    fun getEffect(effectId: Int): Effect? =
        effects.firstOrNull { it.effectId == effectId }

    fun getActiveEffect(effectId: Int): Effect? =
        effects.firstOrNull { it.effectId == effectId && it.isActive }

    fun registerEffect(effect: Effect) {
        effects.add(effect)

        Database.insert(UserEffectsSchema) {
            it.userId to habbo.id
            it.effectId to effect.effectId
            it.duration to effect.duration
            it.quantity to effect.quantity
            it.activationTimestamp to effect.activationTimestamp
            it.isActive to effect.isActive
        }
    }

    override fun iterator(): Iterator<Effect> {
        return effects.iterator()
    }

}