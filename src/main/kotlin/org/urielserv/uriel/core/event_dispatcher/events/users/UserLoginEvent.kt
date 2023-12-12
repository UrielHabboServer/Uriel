package org.urielserv.uriel.core.event_dispatcher.events.users

import org.urielserv.uriel.core.event_dispatcher.CancellableEvent
import org.urielserv.uriel.game.habbos.Habbo

data class UserLoginEvent(
    val habbo: Habbo,
    val ssoTicket: String
) : CancellableEvent()