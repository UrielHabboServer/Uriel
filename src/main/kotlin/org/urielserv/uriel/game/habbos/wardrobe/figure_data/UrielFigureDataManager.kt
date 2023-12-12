package org.urielserv.uriel.game.habbos.wardrobe.figure_data

import kotlinx.serialization.json.Json
import java.net.URI

class UrielFigureDataManager(url: String) {

    val palettes: MutableMap<Int, FigureDataPalette>
    val setTypes: MutableMap<String, FigureDataSetType>

    private val json = Json {
        ignoreUnknownKeys = true
    }

    init {
        val jsonContents = URI(url).toURL().openStream().readAllBytes().toString(Charsets.UTF_8)

        val figureData = json.decodeFromString<FigureData>(jsonContents)

        palettes = figureData.palettes.associateBy { it.id }.toMutableMap()
        setTypes = figureData.setTypes.associateBy { it.type }.toMutableMap()
    }

}