package org.urielserv.uriel.game.wardrobe.figure_data

import kotlinx.serialization.json.Json
import java.io.File
import java.net.URI
import java.net.URL

class UrielFigureDataManager(url: String) {

    val palettes: List<FigureDataPalette>
    val setTypes: List<FigureDataSetType>

    private val json = Json {
        ignoreUnknownKeys = true
    }

    init {
        val jsonContents = readContents(url)

        val figureData = json.decodeFromString<FigureData>(jsonContents)

        palettes = figureData.palettes
        setTypes = figureData.setTypes
    }

    fun readContents(url: String): String {
        return if (isLocalFile(url)) {
            readLocalFile(url)
        } else {
            readWebFile(url)
        }
    }

    private fun isLocalFile(url: String): Boolean {
        return try {
            File(url).isFile
        } catch (e: Exception) {
            false
        }
    }

    private fun readLocalFile(filePath: String): String {
        return File(filePath).readText()
    }

    private fun readWebFile(url: String): String {
        return URI(url).toURL().readText()
    }

}