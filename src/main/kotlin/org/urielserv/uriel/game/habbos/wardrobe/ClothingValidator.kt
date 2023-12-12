package org.urielserv.uriel.game.habbos.wardrobe

import io.klogging.noCoLogger
import org.urielserv.uriel.FigureDataManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.inventory.looks.SavedLook
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.FigureDataPalette
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.FigureDataSetType

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
        val parts = extractValidParts(newLookParts, gender, isHabboClubMember)

        return buildLook(parts, gender, isHabboClubMember, ownedClothing)
    }

    private fun extractValidParts(
        newLookParts: Array<String>,
        gender: String,
        isHabboClubMember: Boolean
    ): Map<String, Array<String>> {
        val parts = mutableMapOf<String, Array<String>>()

        fun addPart(data: Array<String>) {
            val setType = FigureDataManager.setTypes[data[0]]
            if (setType != null) {
                parts[data[0]] = data
            }
        }

        for (lookpart in newLookParts) {
            if (lookpart.contains("-")) {
                addPart(lookpart.split("-").toTypedArray())
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

        return parts
    }

    private fun buildLook(
        parts: Map<String, Array<String>>,
        gender: String,
        isHabboClubMember: Boolean,
        ownedClothing: List<Int>
    ): String {
        val lookParts = mutableListOf<String>()

        parts.forEach { (_, data) ->
            try {
                if (data.isNotEmpty()) {
                    val setType = FigureDataManager.setTypes[data[0]] ?: return@forEach
                    val palette = FigureDataManager.palettes[setType.paletteId]

                    if (palette == null) {
                        logger.error("Couldn't find palette for set type ${setType.type}")
                        return@forEach
                    }

                    val (setId, set) = findValidSet(setType, data, gender, isHabboClubMember, ownedClothing)

                    val dataParts = mutableListOf<String>()

                    val (color1, color2) = determineColors(data, set, palette, isHabboClubMember)

                    dataParts.addAll(listOfNotNull(setType.type, "$setId", color1?.toString(), color2?.toString()))

                    lookParts.add(dataParts.joinToString("-"))
                }
            } catch (exc: Exception) {
                logger.error("Couldn't successfully validate look part: ${data.joinToString("-")}")
                exc.printStackTrace()
            }
        }

        return lookParts.joinToString(".")
    }

    private fun findValidSet(
        setType: FigureDataSetType,
        data: Array<String>,
        gender: String,
        isHabboClubMember: Boolean,
        ownedClothing: List<Int>
    ): Pair<Int, FigureDataSetType.Set?> {
        var setId = (if (data.size >= 2) data[1] else "-1").toInt()
        var set: FigureDataSetType.Set? = setType.sets[setId]

        if (set == null || (set.requiresHabboClubMembership && !isHabboClubMember) ||
            !set.canBeSelected || (set.canBeSold && !ownedClothing.contains(set.id)) ||
            (!set.gender.equals("U", ignoreCase = true) && !set.gender.equals(gender, ignoreCase = true))
        ) {
            set = setType.getFirstNonClubSetForGender(gender)
            setId = set!!.id
        }

        return setId to set
    }

    private fun determineColors(
        data: Array<String>,
        set: FigureDataSetType.Set?,
        palette: FigureDataPalette,
        isHabboClubMember: Boolean
    ): Pair<Int?, Int?> {
        val color1 = determineColor(data, palette, 2, isHabboClubMember)
        val color2 = if (set?.canChangeColors!!) {
            determineColor(data, palette, 3, isHabboClubMember)
        } else {
            null
        }

        return color1 to color2
    }

    private fun determineColor(
        data: Array<String>,
        palette: FigureDataPalette,
        index: Int,
        isHabboClubMember: Boolean
    ): Int {
        val color = if (data.size >= index + 1) data[index].toIntOrNull() ?: -1 else -1

        return if (palette.colors[color]?.requiresHabboClubMembership!! && !isHabboClubMember) {
            palette.getFirstNonClubColor()!!.id
        } else {
            color
        }
    }

}