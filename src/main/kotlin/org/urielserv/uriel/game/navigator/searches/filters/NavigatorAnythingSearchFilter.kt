package org.urielserv.uriel.game.navigator.searches.filters

import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList

class NavigatorAnythingSearchFilter : NavigatorSearchFilter {

    override val id = "anything"

    override fun applyFilter(data: String, searchResultLists: MutableList<NavigatorSearchResultList>) {
        for (searchResultList in searchResultLists) {
            searchResultList.rooms.retainAll {
                val tags = it.info.tags.split(";")

                it.info.name.startsWith(data, true)
                        || it.info.ownerHabboInfo.username.startsWith(data, true)
                        || tags.any { tag -> tag.startsWith(data, true) }
                // TODO: Add support for searching by group name
            }
        }

        searchResultLists.removeIf { it.rooms.isEmpty() }
    }

}