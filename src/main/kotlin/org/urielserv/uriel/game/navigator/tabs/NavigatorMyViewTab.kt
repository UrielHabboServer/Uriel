package org.urielserv.uriel.game.navigator.tabs

import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchAction
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList
import org.urielserv.uriel.game.navigator.searches.display.NavigatorDisplayMode
import org.urielserv.uriel.game.navigator.searches.display.NavigatorListMode

class NavigatorMyViewTab : NavigatorTab {

    override val id = "myworld_view"

    override fun getSearchResultListsForHabbo(habbo: Habbo): MutableList<NavigatorSearchResultList> {
        val searchResultLists = mutableListOf<NavigatorSearchResultList>()

        searchResultLists.add(
            NavigatorSearchResultList(
                "my",
                "",
                NavigatorSearchAction.NONE,
                NavigatorDisplayMode.EXPANDED,
                NavigatorListMode.FORCE_THUMBNAIL,
                RoomManager.getRooms(habbo).toMutableList()
            )
        )

        // TODO: Add missing search result lists

        return searchResultLists
    }

}