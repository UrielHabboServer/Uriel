package org.urielserv.uriel.game.habbos.inventory.effects

import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.UserEffectsSchema
import org.urielserv.uriel.game.habbos.Habbo

class InventoryEffects(
    val habbo: Habbo
) : Iterable<Effect> {

    private val effects = mutableListOf<Effect>()

    init {
        Database.sequenceOf(UserEffectsSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { effects.add(it) }
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
            set(it.userId, habbo.info.id)
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