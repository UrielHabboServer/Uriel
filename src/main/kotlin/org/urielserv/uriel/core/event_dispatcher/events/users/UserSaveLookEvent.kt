package org.urielserv.uriel.core.event_dispatcher.events.users

import org.urielserv.uriel.core.event_dispatcher.CancellableEvent
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender

class UserSaveLookEvent(
    val habbo: Habbo,
    var slotId: Int,
    var look: String,
    var gender: HabboGender
) : CancellableEvent()