package org.urielserv.uriel.extensions

import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.events.UrielEvent

fun on(eventType: String, callback: (UrielEvent) -> Unit) {
    EventDispatcher.on(eventType, callback)
}