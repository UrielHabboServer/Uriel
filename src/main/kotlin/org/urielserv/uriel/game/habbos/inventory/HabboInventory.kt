package org.urielserv.uriel.game.habbos.inventory

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.inventory.effects.InventoryEffects

class HabboInventory(
    val habbo: Habbo
) {

    val wardrobe = InventoryWardrobe(habbo)
    val effects = InventoryEffects(habbo)

}