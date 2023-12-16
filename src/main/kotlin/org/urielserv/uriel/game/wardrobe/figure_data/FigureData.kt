package org.urielserv.uriel.game.wardrobe.figure_data

import kotlinx.serialization.Serializable

@Serializable
data class FigureData(
    val palettes: List<FigureDataPalette>,
    val setTypes: List<FigureDataSetType>
)