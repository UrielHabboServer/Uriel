package org.urielserv.uriel.core.event_dispatcher.events.rooms

import org.urielserv.uriel.core.event_dispatcher.CancellableEvent
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.rooms.Room

class RoomEnterEvent(
    val habbo: Habbo,
    val room: Room
) : CancellableEvent()