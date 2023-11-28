package org.urielserv.uriel.game.habbos.inventory.looks

import org.ktorm.database.iterator
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.UserSavedLooksSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender

class InventorySavedLooks(
    val habbo: Habbo
) {

    private val savedLooks = mutableListOf<SavedLook>()

    init {
        Database.from(UserSavedLooksSchema)
            .select()
            .where(UserSavedLooksSchema.userId eq habbo.id)
            .rowSet
            .iterator()
            .forEach {
                savedLooks.add(SavedLook(
                    it[UserSavedLooksSchema.id]!!,
                    habbo,
                    it[UserSavedLooksSchema.slotId]!!,
                    it[UserSavedLooksSchema.look]!!,
                    it[UserSavedLooksSchema.gender]!!
                ))
            }
    }

    fun getLook(slotId: Int): SavedLook? =
        savedLooks.firstOrNull { it.slotId == slotId }

    fun getLooks(): List<SavedLook> = savedLooks.toList()

    fun setLook(slotId: Int, look: String, gender: HabboGender) {
        assert(slotId in 0..9)

        val existingLook = getLook(slotId)

        if (existingLook == null) {
            Database.insert(UserSavedLooksSchema) {
                set(it.userId, habbo.id)
                set(it.slotId, slotId)
                set(it.look, look)
                set(it.gender, gender)
            }
        } else {
            Database.update(UserSavedLooksSchema) {
                set(it.look, look)
                set(it.gender, gender)

                where {
                    it.id eq existingLook.id
                }
            }
        }
    }

    fun clearLook(slotId: Int) {
        assert(slotId in 0..9)

        val existingLook = getLook(slotId) ?: return

        Database.delete(UserSavedLooksSchema) {
            it.id eq existingLook.id
        }
    }

    fun clearLooks() {
        Database.delete(UserSavedLooksSchema) {
            it.userId eq habbo.id
        }
    }

}