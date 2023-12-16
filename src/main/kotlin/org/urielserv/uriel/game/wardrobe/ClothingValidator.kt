package org.urielserv.uriel.game.wardrobe

import io.klogging.noCoLogger
import org.urielserv.uriel.FigureDataManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.inventory.looks.SavedLook

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
        val lookParts = look.split(".")
        val validatedLookParts = mutableListOf<String>()

        if (lookParts.size < 2) {
            logger.warn("Invalid look: $look")
            return look
        }

        for (lookPart in lookParts) {
            val parts = lookPart.split("-")

            val setType = parts.getOrNull(0)
            val setId = parts.getOrNull(1)
            val firstColorId = parts.getOrNull(2)?.toIntOrNull()
            val secondColorId = parts.getOrNull(3)?.toIntOrNull()

            if (setType == null) {
                continue
            }

            val figureDataSetType = FigureDataManager.setTypes.firstOrNull { it.type == setType }

            if (figureDataSetType == null) {
                continue
            }

            val figureDataSet = figureDataSetType.sets.firstOrNull { it.id == setId?.toIntOrNull() }

            if (figureDataSet == null) {
                continue
            }

            // Check if it's inventory clothing and if the habbo owns it
            if (!figureDataSet.canBeSelected && !ownedClothing.contains(figureDataSet.id)) {
                continue
            }

            // Check if it's a club set and if the habbo is a club member
            if (figureDataSet.requiresHabboClubMembership && !isHabboClubMember) {
                continue
            }

            // Check gender
            if (!figureDataSet.gender.equals("U", ignoreCase = true)
                && !figureDataSet.gender.equals(gender, ignoreCase = true)
            ) {
                if (gender.equals("M", ignoreCase = true)
                    && !isHabboClubMember
                    && !figureDataSetType.mandatoryMale0
                ) continue

                if (gender.equals("F", ignoreCase = true)
                    && !isHabboClubMember
                    && !figureDataSetType.mandatoryFemale0
                ) continue

                if (gender.equals("M", ignoreCase = true)
                    && isHabboClubMember
                    && !figureDataSetType.mandatoryMale1
                ) continue

                if (gender.equals("F", ignoreCase = true)
                    && isHabboClubMember
                    && !figureDataSetType.mandatoryFemale1
                ) continue
            }

            var validatedLookPart = "$setType-$setId"

            // COLORS
            val palette = FigureDataManager.palettes.firstOrNull { it.id == figureDataSetType.paletteId }

            if (palette == null) {
                continue
            }

            var firstColor = if (firstColorId != null && firstColorId > 0) {
                palette.colors.firstOrNull { it.id == firstColorId }
            } else {
                palette.getFirstNonClubColor()
            }

            if (firstColor == null) {
                firstColor = palette.getFirstNonClubColor()
            }

            if (!firstColor!!.canBeSelected) {
                firstColor = palette.getFirstNonClubColor()
            }

            if (firstColor!!.requiresHabboClubMembership && !isHabboClubMember) {
                firstColor = palette.getFirstNonClubColor()
            }

            validatedLookPart += "-${firstColor!!.id}"

            // second color is optional
            if (secondColorId != null) {
                var secondColor = palette.colors.firstOrNull { it.id == secondColorId }

                if (secondColor == null) {
                    secondColor = palette.getFirstNonClubColor()
                }

                if (!secondColor!!.canBeSelected) {
                    secondColor = palette.getFirstNonClubColor()
                }

                if (secondColor!!.requiresHabboClubMembership && !isHabboClubMember) {
                    secondColor = palette.getFirstNonClubColor()
                }

                validatedLookPart += "-${secondColor!!.id}"
            }

            validatedLookParts.add(validatedLookPart)
        }

        return validatedLookParts.joinToString(".")
    }

}