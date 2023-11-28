package org.urielserv.uriel.game.habbos.inventory.effects

import org.ktorm.dsl.eq
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.UserEffectsSchema
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo

class Effect(
    val id: Int,
    val habbo: Habbo,

    val effectId: Int,

    val duration: Int,
    val quantity: Int,

    val activationTimestamp: Int,
    var isActive: Boolean
) {

    val timeUsed: Int
        get() = currentUnixSeconds - activationTimestamp

    val timeLeft: Int
        get() = if (isActive) duration - timeUsed else duration

    val hasExpired: Boolean
        get() = isActive && timeLeft <= 0

    fun checkIfExpired() {
        if (!hasExpired) return

        isActive = false

        Database.delete(UserEffectsSchema) {
            it.id eq id
        }
    }

    fun activate() {
        isActive = true

        Database.update(UserEffectsSchema) {
            set(it.isActive, true)
            set(it.activationTimestamp, currentUnixSeconds)

            where {
                it.id eq id
            }
        }
    }

    fun addToQuantity(amount: Int) {
        Database.update(UserEffectsSchema) {
            set(it.quantity, quantity + amount)

            where {
                it.id eq id
            }
        }
    }

}