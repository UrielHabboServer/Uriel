package org.urielserv.uriel.game.habbos.wardrobe.figure_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
data class FigureDataPalette(
    val id: Int,
    @SerialName("colors") val colorsInternal: List<Color>
) {

    val colors: Map<Int, Color>
        get() = colorsInternal.associateBy { it.index }

    fun getFirstNonClubColor(): Color? =
        colors.values.firstOrNull { !it.requiresHabboClubMembership && it.canBeSelected }
            ?: colors.values.firstOrNull()

    @Serializable
    data class Color(
        val id: Int,
        val index: Int,
        @SerialName("club") val clubType: Int,
        @SerialName("selectable") val canBeSelected: Boolean,
        @SerialName("hexCode") val textContent: String,
    ) {

        val requiresHabboClubMembership: Boolean
            get() = clubType != 0

    }

}
