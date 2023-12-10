package org.urielserv.uriel.game.navigator.searches.filters

import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList

class NavigatorOwnerSearchFilter : NavigatorSearchFilter {

    override val id = "owner"

    override fun applyFilter(data: String, searchResultLists: MutableList<NavigatorSearchResultList>) {
        for (searchResultList in searchResultLists) {
            searchResultList.rooms.retainAll {
                it.info.ownerHabboInfo.username.startsWith(data, true)
            }
        }

        searchResultLists.removeIf { it.rooms.isEmpty() }
    }

}