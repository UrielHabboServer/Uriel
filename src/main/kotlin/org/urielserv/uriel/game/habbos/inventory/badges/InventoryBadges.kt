package org.urielserv.uriel.game.habbos.inventory.badges

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.users.UserBadgesSchema
import org.urielserv.uriel.game.habbos.Habbo

class InventoryBadges(
    val habbo: Habbo
) : Collection<Badge> {

    private val _badges = mutableListOf<Badge>()

    val activeBadges: List<Badge>
        get() = _badges.filter { it.isActive }

    override val size: Int
        get() = _badges.size

    init {
        Database.sequenceOf(UserBadgesSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { _badges.add(it) }
    }

    fun hasBadge(badge: String): Boolean =
        _badges.any { it.code == badge }

    fun hasBadge(badge: Badge): Boolean =
        _badges.any { it.code == badge.code }

    fun addBadge(badge: String) {
        val badgeObj = Badge {
            habboInfo = habbo.info
            this.code = badge
        }

        _badges.add(badgeObj)
        Database.sequenceOf(UserBadgesSchema).add(badgeObj)
    }

    fun updateBadge(badge: String, slotId: Int) {
        val badgeObj = _badges.firstOrNull { it.code == badge } ?: return

        badgeObj.slotId = slotId
        badgeObj.isActive = slotId > 0
        badgeObj.flushChanges()
    }

    fun removeBadge(slotId: Int) {
        val badgeObj = _badges.firstOrNull { it.slotId == slotId } ?: return

        badgeObj.slotId = 0
        badgeObj.isActive = false
        badgeObj.flushChanges()
    }

    fun removeBadge(badge: String) {
        val badgeObj = _badges.firstOrNull { it.code == badge } ?: return

        badgeObj.delete()
        _badges.remove(badgeObj)
    }

    override fun iterator(): Iterator<Badge> {
        return _badges.iterator()
    }

    override fun isEmpty(): Boolean {
        return _badges.isEmpty()
    }

    override fun containsAll(elements: Collection<Badge>): Boolean {
        return _badges.containsAll(elements)
    }

    override fun contains(element: Badge): Boolean {
        return _badges.contains(element)
    }

}