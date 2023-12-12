package org.urielserv.uriel.game.habbos.wardrobe

import io.klogging.noCoLogger
import org.urielserv.uriel.FigureDataManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.inventory.looks.SavedLook
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.FigureDataPalette
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.FigureDataSetType
import java.util.*

object ClothingValidator {

    private val logger = noCoLogger(ClothingValidator::class)

    fun validateLook(savedLook: SavedLook, isHabboClubMember: Boolean): String =
        validateLook(
            savedLook.look,
            savedLook.gender.short(),
            isHabboClubMember,
            listOf()
        )

    fun validateLook(habbo: Habbo): String =
        validateLook(
            habbo.info.look,
            habbo.info.gender.short(),
            habbo.subscriptions.hasActiveHabboClubMembership(),
            listOf() // habbo.inventory.ownedClothing.map { it.id }.toList()
        )

    fun validateLook(
        look: String,
        gender: String,
        isHabboClubMember: Boolean,
        ownedClothing: List<Int>
    ): String {
        if (FigureDataManager.palettes.isEmpty() || FigureDataManager.setTypes.isEmpty()) {
            return look
        }

        val newLookParts = look.split(".").toTypedArray()
        val lookParts = mutableListOf<String>()

        val parts: MutableMap<String, Array<String>> = TreeMap()

        fun addPart(data: Array<String>) {
            val setType: FigureDataSetType? = FigureDataManager.setTypes[data[0]]
            if (setType != null) {
                parts[data[0]] = data
            }
        }

        // Add mandatory setTypes
        for (lookPart in newLookParts) {
            if (lookPart.contains("-")) {
                addPart(lookPart.split("-").toTypedArray())
            }
        }

        FigureDataManager.setTypes.filter { (key, _) -> !parts.containsKey(key) }
            .forEach { (key, setType) ->
                val isMale = gender.equals("M", ignoreCase = true)
                val isFemale = gender.equals("F", ignoreCase = true)

                when {
                    (isMale && !isHabboClubMember && !setType.mandatoryMale0) ||
                            (isFemale && !isHabboClubMember && !setType.mandatoryFemale0) ||
                            (isMale && isHabboClubMember && !setType.mandatoryMale1) ||
                            (isFemale && isHabboClubMember && !setType.mandatoryFemale1) -> return@forEach

                    else -> parts[key] = arrayOf(key)
                }
            }

        parts.forEach { (_, data) ->
            try {
                if (data.isNotEmpty()) {
                    val setType = FigureDataManager.setTypes[data[0]] ?: return@forEach
                    val palette = FigureDataManager.palettes[setType.paletteId]

                    if (palette == null) {
                        logger.error("Palette ${setType.paletteId} does not exist")
                        return@forEach
                    }

                    var setId = (if (data.size >= 2) data[1] else "-1").toInt()
                    var set = setType.sets[setId]

                    if (set == null || (set.requiresHabboClubMembership && !isHabboClubMember) ||
                        !set.canBeSelected || (set.canBeSold && !ownedClothing.contains(set.id)) ||
                        (!set.gender.equals("U", ignoreCase = true) && !set.gender.equals(gender, ignoreCase = true))
                    ) {
                        set = setType.getFirstNonClubSetForGender(gender)
                        setId = set!!.id
                    }

                    val dataParts = mutableListOf<String>()

                    var color1 = -1
                    var color2 = -1

                    if (set.canChangeColors) {
                        color1 = if (data.size >= 3) data[2].toInt() else -1

                        val color = palette.colors[color1]

                        if (color == null || (color.requiresHabboClubMembership && !isHabboClubMember)) {
                            color1 = palette.getFirstNonClubColor()!!.id
                        }
                    }

                    if (data.size >= 4 && set.canChangeColors) {
                        color2 = data[3].toInt()

                        val color: FigureDataPalette.Color? = palette.colors[color2]

                        if (color == null || (color.requiresHabboClubMembership && !isHabboClubMember)) {
                            color2 = palette.getFirstNonClubColor()!!.id
                        }
                    }

                    dataParts.add(setType.type)
                    dataParts.add("$setId")

                    if (color1 > -1) {
                        dataParts.add("$color1")
                    }

                    if (color2 > -1) {
                        dataParts.add("$color2")
                    }

                    lookParts.add(dataParts.joinToString("-"))
                }
            } catch (exc: Exception) {
                logger.error("Error in clothing validation")
                exc.printStackTrace()
            }
        }

        return lookParts.joinToString(".")
    }

}