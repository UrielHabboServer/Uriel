package org.urielserv.uriel.game.navigator.tabs

import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchAction
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList
import org.urielserv.uriel.game.navigator.searches.display.NavigatorDisplayMode
import org.urielserv.uriel.game.navigator.searches.display.NavigatorListMode
import org.urielserv.uriel.game.rooms.RoomAccessType

class NavigatorHotelViewTab : NavigatorTab {

    override val id = "hotel_view"

    override fun getSearchResultListsForHabbo(habbo: Habbo): MutableList<NavigatorSearchResultList> {
        val canSeeInvisibleRooms = habbo.hasPermission("uriel.navigator.can_see_invisible_rooms")

        val searchResultLists = mutableListOf<NavigatorSearchResultList>()

        searchResultLists.add(
            NavigatorSearchResultList(
                "popular",
                "",
                NavigatorSearchAction.NONE,
                NavigatorDisplayMode.EXPANDED,
                NavigatorListMode.FORCE_THUMBNAIL,
                RoomManager.getPopularRooms().filter {
                    it.info.accessType != RoomAccessType.INVISIBLE || canSeeInvisibleRooms
                }.toMutableList()
            )
        )

        for (category in NavigatorManager.getFlatCategories()) {
            searchResultLists.add(
                NavigatorSearchResultList(
                    category.caption,
                    category.caption,
                    NavigatorSearchAction.NONE,
                    NavigatorDisplayMode.EXPANDED,
                    NavigatorListMode.FORCE_THUMBNAIL,
                    RoomManager.getRooms(category).filter {
                        it.info.accessType != RoomAccessType.INVISIBLE || canSeeInvisibleRooms
                    }.sortedByDescending { it.info.users }.toMutableList()
                )
            )
        }

        return searchResultLists
    }

}