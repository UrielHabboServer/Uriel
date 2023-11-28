package org.urielserv.uriel.game.habbos.wardrobe

import io.klogging.noCoLogger
import org.urielserv.uriel.FigureDataManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.inventory.looks.SavedLook
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.FigureDataPalette
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.FigureDataSetType
import java.util.regex.Pattern

object ClothingValidator {

    private val logger = noCoLogger("uriel.game.habbos.wardrobe.UrielWardrobeManager")

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

    fun validateLook(look: String, gender: String, isHabboClubMember: Boolean, ownedClothing: List<Int>): String {
        if (FigureDataManager.palettes.isEmpty() || FigureDataManager.setTypes.isEmpty()) return look

        val lookParts = look.split(Pattern.quote(".").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val parts = mutableMapOf<String, List<String>>()

        lookParts.forEach { lookPart ->
            lookPart.split("-".toRegex()).dropLastWhile { it.isEmpty() }.also { data ->
                FigureDataManager.setTypes[data.getOrNull(0)]?.let {
                    parts[data[0]] = data
                }
            }
        }

        FigureDataManager.setTypes.entries
            .filter { !parts.containsKey(it.key) }
            .forEach { x ->
                val setType: FigureDataSetType = x.value

                if ((gender.equals("M", true) && isHabboClubMember && setType.mandatoryMale1) ||
                    (gender.equals("F", true) && isHabboClubMember && setType.mandatoryFemale1) ||
                    (gender.equals("M", true) && !isHabboClubMember && setType.mandatoryMale0) ||
                    (gender.equals("F", true) && !isHabboClubMember && setType.mandatoryFemale0)) {
                    parts[x.key] = listOf(x.key)
                }
            }

        val rebuiltParts = parts.mapNotNull { (key, data) ->
            try {
                val setType = FigureDataManager.setTypes[data[0]] ?: run {
                    logger.warn { "Failed to validate look part $key; no set type" }
                    return@mapNotNull null
                }

                val palette = FigureDataManager.palettes[setType.paletteId] ?: run {
                    logger.warn { "Palette ${setType.paletteId} does not exist" }
                    return@mapNotNull null
                }

                var setId = data.getOrNull(1)?.toInt() ?: -1
                var set = setType.sets[setId]

                if (set == null || (set.requiresHabboClubMembership && !isHabboClubMember) || !set.canBeSelected || (set.canBeSold && !ownedClothing.contains(set.id)) ||
                    (!set.gender.equals("U", true) && !set.gender.equals(gender, true))) {
                    // Reassign set if validation fails
                    set = setType.getFirstNonClubSetForGender(gender)
                    set?.let { setId = it.id } ?: return@mapNotNull null
                }

                val colorIds = getValidColorIds(data, palette, isHabboClubMember)

                buildLookPart(setType, setId, colorIds)
            } catch (e: Exception) {
                logger.warn(e) { "Failed to validate look part $key" }
                null
            }
        }
        return rebuiltParts.joinToString(".")
    }

    private fun getValidColorIds(data: List<String>, palette: FigureDataPalette, isHabboClubMember: Boolean): List<Int> {
        val colorIds = mutableListOf<Int>()
        if (data.size >= 3) {
            palette.colors[data[2].toInt()]?.let { color ->
                if (color.requiresHabboClubMembership && !isHabboClubMember) {
                    palette.getFirstNonClubColor()?.id?.let { colorIds.add(it) }
                }
            }
        }

        if (data.size >= 4) {
            palette.colors[data[3].toInt()]?.let { color ->
                if (color.requiresHabboClubMembership && !isHabboClubMember) {
                    palette.getFirstNonClubColor()?.id?.let { colorIds.add(it) }
                }
            }
        }
        return colorIds
    }

    private fun buildLookPart(setType: FigureDataSetType, setId: Int, colorIds: List<Int>): String {
        return listOf(setType.type, "$setId", *colorIds.map { it.toString() }.toTypedArray()).joinToString("-")
    }

}