package org.urielserv.uriel.game.habbos.wardrobe.figure_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.TreeMap
import kotlin.collections.Set

@Serializable
data class FigureDataSetType(
    val type: String,
    val paletteId: Int,
    @SerialName("mandatory_m_0") val mandatoryMale0: Boolean,
    @SerialName("mandatory_f_0") val mandatoryFemale0: Boolean,
    @SerialName("mandatory_m_1") val mandatoryMale1: Boolean,
    @SerialName("mandatory_f_1") val mandatoryFemale1: Boolean,
    @SerialName("sets") private val setsInternal: List<Set>
) {

    val sets: TreeMap<Int, Set>
        get() {
            val treeMap = TreeMap<Int, Set>()

            setsInternal.forEach { set ->
                treeMap[set.id] = set
            }

            return treeMap
        }

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

    @Serializable
    data class Set(
        val id: Int,
        val gender: String,
        @SerialName("club") val clubType: Int,
        @SerialName("colorable") val canChangeColors: Boolean,
        @SerialName("selectable") val canBeSelected: Boolean,
        @SerialName("preselectable") val canBePreSelected: Boolean,
        @SerialName("sellable") val canBeSold: Boolean
    ) {

        val requiresHabboClubMembership: Boolean
            get() = clubType > 0

    }

}
