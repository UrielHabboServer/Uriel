package org.urielserv.uriel.game.habbos.wardrobe.figure_data

import java.util.*

data class FigureDataSetType(
    val type: String,
    val paletteId: Int,
    val mandatoryMale0: Boolean,
    val mandatoryFemale0: Boolean,
    val mandatoryMale1: Boolean,
    val mandatoryFemale1: Boolean
) {

    val sets: TreeMap<Int, Set> = TreeMap()

    private fun getFirstMatchingSetForGender(gender: String?, predicate: (Set) -> Boolean): Set? {
        val matchingSet = sets.descendingMap().values.firstOrNull { set ->
            (set.gender.equals(gender, true) || set.gender.equals("u", true)) && predicate(set)
        }

        return matchingSet ?: (if (sets.isNotEmpty()) sets.descendingMap().entries.iterator().next().value else null)
    }

    fun getFirstSetForGender(gender: String?): Set? =
        getFirstMatchingSetForGender(gender) { set -> !set.canBeSold && set.canBeSelected }

    fun getFirstNonClubSetForGender(gender: String?): Set? =
        getFirstMatchingSetForGender(gender) { set -> !set.requiresHabboClubMembership && !set.canBeSold && set.canBeSelected }

    data class Set(
        val id: Int,
        val gender: String,
        val requiresHabboClubMembership: Boolean,
        val canChangeColors: Boolean,
        val canBeSelected: Boolean,
        val canBePreSelected: Boolean,
        val canBeSold: Boolean
    )

}
