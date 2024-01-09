package org.urielserv.uriel.game.habbos.inventory

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.inventory.badges.InventoryBadges
import org.urielserv.uriel.game.habbos.inventory.effects.InventoryEffects
import org.urielserv.uriel.game.habbos.inventory.looks.InventorySavedLooks

class HabboInventory(
    val habbo: Habbo
) {

    val savedLooks = InventorySavedLooks(habbo)
    val effects = InventoryEffects(habbo)
    val badges = InventoryBadges(habbo)

}