package org.urielserv.uriel.game.habbos.inventory.effects

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.users.UserEffectsSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.users.inventory.UserEffectListPacket

class InventoryEffects(
    val habbo: Habbo
) : Collection<Effect> {

    private val _effects = mutableListOf<Effect>()

    override val size: Int
        get() = _effects.size

    init {
        Database.sequenceOf(UserEffectsSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { _effects.add(it) }

        UserEffectListPacket(_effects.toList()).sendSync(habbo)
    }

    fun checkIfExpired() {
        _effects.forEach { it.checkIfExpired() }
    }

    fun hasActiveEffect(effectId: Int): Boolean =
        _effects.any { it.effectId == effectId && it.isActive }

    fun getEffect(effectId: Int): Effect? =
        _effects.firstOrNull { it.effectId == effectId }

    fun getActiveEffect(effectId: Int): Effect? =
        _effects.firstOrNull { it.effectId == effectId && it.isActive }

    fun registerEffect(effect: Effect) {
        _effects.add(effect)

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
        return _effects.iterator()
    }

    override fun isEmpty(): Boolean {
        return _effects.isEmpty()
    }

    override fun containsAll(elements: Collection<Effect>): Boolean {
        return _effects.containsAll(elements)
    }

    override fun contains(element: Effect): Boolean {
        return _effects.contains(element)
    }

}