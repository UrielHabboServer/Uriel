package org.urielserv.uriel.extensions

import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.core.event_dispatcher.UrielEvent

fun on(eventType: String, callback: suspend (UrielEvent) -> Unit) {
    EventDispatcher.on(eventType, callback)
}