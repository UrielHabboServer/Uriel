package org.urielserv.uriel.game.navigator.searches.filters

import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList

interface NavigatorSearchFilter {

    val id: String

    fun applyFilter(data: String, searchResultLists: MutableList<NavigatorSearchResultList>)

}