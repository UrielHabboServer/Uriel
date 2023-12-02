package org.urielserv.uriel.game.habbos.inventory.effects

import org.ktorm.dsl.eq
import org.ktorm.entity.add
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

        Database.sequenceOf(UserEffectsSchema)
            .add(Effect {
                habboInfo = this@InventoryEffects.habbo.info
                effectId = effect.effectId
                duration = effect.duration
                quantity = effect.quantity
                activationTimestamp = effect.activationTimestamp
                isActive = effect.isActive
            })
    }

    override fun iterator(): Iterator<Effect> {
        return effects.iterator()
    }

}