package org.urielserv.uriel.game.habbos.inventory

import org.urielserv.uriel.game.habbos.Habbo

class HabboInventory(
    val habbo: Habbo
) {

    val wardrobe = InventoryWardrobe(habbo)
    val effects = InventoryEffects(habbo)

}