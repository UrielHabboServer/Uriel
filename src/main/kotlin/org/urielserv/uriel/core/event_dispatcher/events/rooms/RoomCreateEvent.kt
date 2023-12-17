package org.urielserv.uriel.core.event_dispatcher.events.rooms

import org.urielserv.uriel.core.event_dispatcher.CancellableEvent
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.navigator.NavigatorFlatCategory
import org.urielserv.uriel.game.rooms.RoomModel

class RoomCreateEvent(
    val habbo: Habbo,
    val name: String,
    val description: String,
    val model: RoomModel,
    val category: NavigatorFlatCategory,
    val maxVisitors: Int,
    val tradeType: Int
) : CancellableEvent()