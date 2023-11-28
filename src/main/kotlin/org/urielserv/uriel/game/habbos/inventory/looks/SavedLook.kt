package org.urielserv.uriel.game.habbos.inventory.looks

import org.ktorm.dsl.eq
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.UsersSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender

data class SavedLook(
    val id: Int,
    val habbo: Habbo,

    val slotId: Int,
    val look: String,
    val gender: HabboGender
) : Cloneable {

    fun apply() = apply(habbo)

    fun apply(habbo: Habbo) {
        Database.update(UsersSchema) {
            set(it.look, look)
            set(it.gender, gender)

            where {
                it.id eq habbo.id
            }
        }
    }

}