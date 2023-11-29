package org.urielserv.uriel.game.habbos.wardrobe.figure_data

import java.util.*

data class FigureDataPalette(
    val id: Int,
) {

    val colors: TreeMap<Int, Color> = TreeMap()

    fun getFirstNonClubColor(): Color? =
        colors.values.firstOrNull { !it.requiresHabboClubMembership && it.canBeSelected }
            ?: colors.values.firstOrNull()

    data class Color(
        val id: Int,
        val index: Int,
        val requiresHabboClubMembership: Boolean,
        val canBeSelected: Boolean,
        val textContent: String,
    )

}
