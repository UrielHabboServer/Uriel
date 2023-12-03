package org.urielserv.uriel.game.navigator.filters

import org.urielserv.uriel.game.habbos.Habbo

interface NavigatorFilter {

    fun getRoomsForHabbo(habbo: Habbo): List<Int>

}