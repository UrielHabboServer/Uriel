package org.urielserv.uriel.game.navigator.tabs

import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.navigator.searches.display.NavigatorDisplayMode
import org.urielserv.uriel.game.navigator.searches.display.NavigatorListMode
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchAction
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList
import org.urielserv.uriel.game.rooms.RoomAccessType

class NavigatorOfficialViewTab : NavigatorTab {

    override val id = "official_view"

    override fun getSearchResultListsForHabbo(habbo: Habbo): MutableList<NavigatorSearchResultList> {
        val canSeeInvisibleRooms = habbo.hasPermission("uriel.navigator.can_see_invisible_rooms")

        val searchResultLists = mutableListOf<NavigatorSearchResultList>()

        searchResultLists.add(
            NavigatorSearchResultList(
                "official-root",
                "",
                NavigatorSearchAction.NONE,
                NavigatorDisplayMode.EXPANDED,
                NavigatorListMode.FORCE_THUMBNAIL,
                RoomManager.getPublicRooms().toMutableList()
            )
        )

        for (category in NavigatorManager.getPublicCategories()) {
            val rooms = RoomManager.getRoomsByPublicCategory(category).filter {
                it.info.accessType != RoomAccessType.INVISIBLE || canSeeInvisibleRooms
            }

            if (rooms.isEmpty()) {
                continue
            }

            searchResultLists.add(
                NavigatorSearchResultList(
                    "",
                    category.name,
                    NavigatorSearchAction.NONE,
                    NavigatorDisplayMode.COLLAPSED,
                    NavigatorListMode.FORCE_THUMBNAIL,
                    rooms.toMutableList()
                )
            )
        }

        return searchResultLists
    }

}