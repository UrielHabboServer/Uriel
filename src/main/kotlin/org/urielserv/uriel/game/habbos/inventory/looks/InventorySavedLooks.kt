package org.urielserv.uriel.game.habbos.inventory.looks

import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.UserSavedLooksSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender

class InventorySavedLooks(
    val habbo: Habbo
) {

    private val savedLooks = mutableListOf<SavedLook>()

    init {
        Database.sequenceOf(UserSavedLooksSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { savedLooks.add(it) }
    }

    fun getLook(slotId: Int): SavedLook? =
        savedLooks.firstOrNull { it.slotId == slotId }

    fun getLooks(): List<SavedLook> = savedLooks.toList()

    fun setLook(slotId: Int, look: String, gender: HabboGender) {
        assert(slotId in 0..9)

        val existingLook = getLook(slotId)

        if (existingLook == null) {
            Database.insert(UserSavedLooksSchema) {
                set(it.userId, habbo.info.id)
                set(it.slotId, slotId)
                set(it.look, look)
                set(it.gender, gender)
            }
        } else {
            existingLook.look = look
            existingLook.gender = gender
            existingLook.flushChanges()
        }
    }

    fun clearLook(slotId: Int) {
        assert(slotId in 0..9)

        val existingLook = getLook(slotId) ?: return

        existingLook.delete()
    }

    fun clearLooks() {
        Database.sequenceOf(UserSavedLooksSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { it.delete() }
    }

}