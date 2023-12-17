package org.urielserv.uriel.core.event_dispatcher.events.users

import org.urielserv.uriel.core.event_dispatcher.CancellableEvent
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender

class UserUpdateLookEvent(
    val habbo: Habbo,
    var newLook: String,
    var newGender: HabboGender,
    var previousLook: String,
    var previousGender: HabboGender
) : CancellableEvent()