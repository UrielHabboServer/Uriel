package org.urielserv.uriel.game.habbos.inventory.looks

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.users.UserSavedLooksSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender

class InventorySavedLooks(
    val habbo: Habbo
) : Collection<SavedLook> {

    private val _looks = mutableListOf<SavedLook>()

    override val size: Int
        get() = _looks.size

    init {
        Database.sequenceOf(UserSavedLooksSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { _looks.add(it) }
    }

    fun getLook(slotId: Int): SavedLook? =
        _looks.firstOrNull { it.slotId == slotId }

    fun setLook(slotId: Int, look: String, gender: HabboGender) {
        assert(slotId in 0..9)

        val existingLook = getLook(slotId)

        if (existingLook == null) {
            Database.sequenceOf(UserSavedLooksSchema)
                .add(SavedLook {
                    habboInfo = this@InventorySavedLooks.habbo.info
                    this.slotId = slotId
                    this.look = look
                    this.gender = gender
                })
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

    override fun iterator(): Iterator<SavedLook> {
        return _looks.iterator()
    }

    override fun isEmpty(): Boolean {
        return _looks.isEmpty()
    }

    override fun containsAll(elements: Collection<SavedLook>): Boolean {
        return _looks.containsAll(elements)
    }

    override fun contains(element: SavedLook): Boolean {
        return _looks.contains(element)
    }

}