package org.urielserv.uriel.game.navigator.searches.filters

import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList

class NavigatorTagSearchFilter : NavigatorSearchFilter {

    override val id = "tag"

    override fun applyFilter(data: String, searchResultLists: MutableList<NavigatorSearchResultList>) {
        for (searchResultList in searchResultLists) {
            searchResultList.rooms.retainAll {
                val tags = it.info.tags.split(";")

                tags.any { tag -> tag.startsWith(data, true) }
            }
        }

        searchResultLists.removeIf { it.rooms.isEmpty() }
    }

}