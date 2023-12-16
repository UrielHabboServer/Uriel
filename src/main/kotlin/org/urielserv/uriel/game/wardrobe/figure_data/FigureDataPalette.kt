package org.urielserv.uriel.game.wardrobe.figure_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FigureDataPalette(
    val id: Int,
    @SerialName("colors") val colors: List<Color>
) {

    fun getFirstNonClubColor(): Color? =
        colors.firstOrNull { !it.requiresHabboClubMembership && it.canBeSelected }

    @Serializable
    data class Color(
        val id: Int,
        val index: Int,
        @SerialName("club") val clubType: Int?,
        @SerialName("selectable") val canBeSelected: Boolean,
        @SerialName("hexCode") val textContent: String,
    ) {

        val requiresHabboClubMembership: Boolean
            get() {
                return try {
                    clubType != null && clubType > 0
                } catch (ignored: Exception) {
                    false
                }
            }

    }

}
