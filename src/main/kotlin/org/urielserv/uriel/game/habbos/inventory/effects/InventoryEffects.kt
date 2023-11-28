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
                        row[UserEffectsSchema.isActive]!!
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
            set(it.userId, habbo.id)
            set(it.effectId, effect.effectId)
            set(it.duration, effect.duration)
            set(it.quantity, effect.quantity)
            set(it.activationTimestamp, effect.activationTimestamp)
            set(it.isActive, effect.isActive)
        }
    }

    override fun iterator(): Iterator<Effect> {
        return effects.iterator()
    }

}