package org.urielserv.uriel.game.navigator.tabs

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList

interface NavigatorTab {

    val id: String

    fun getSearchResultListsForHabbo(habbo: Habbo): MutableList<NavigatorSearchResultList>

}