package org.urielserv.uriel.game.navigator.searches.filters

import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList

class NavigatorRoomNameSearchFilter : NavigatorSearchFilter {

    override val id = "roomname"

    override fun applyFilter(data: String, searchResultLists: MutableList<NavigatorSearchResultList>) {
        for (searchResultList in searchResultLists) {
            searchResultList.rooms.retainAll {
                it.info.name.startsWith(data, true)
            }
        }

        searchResultLists.removeIf { it.rooms.isEmpty() }
    }

}