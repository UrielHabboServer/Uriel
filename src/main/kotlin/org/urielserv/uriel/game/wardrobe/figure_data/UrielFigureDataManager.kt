package org.urielserv.uriel.game.wardrobe.figure_data

import kotlinx.serialization.json.Json
import java.net.URI

class UrielFigureDataManager(url: String) {

    val palettes: List<FigureDataPalette>
    val setTypes: List<FigureDataSetType>

    private val json = Json {
        ignoreUnknownKeys = true
    }

    init {
        val jsonContents = URI(url).toURL().openStream().readAllBytes().toString(Charsets.UTF_8)

        val figureData = json.decodeFromString<FigureData>(jsonContents)

        palettes = figureData.palettes
        setTypes = figureData.setTypes
    }

}