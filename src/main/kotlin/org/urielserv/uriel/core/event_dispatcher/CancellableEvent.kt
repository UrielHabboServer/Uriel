package org.urielserv.uriel.core.event_dispatcher

open class CancellableEvent : UrielEvent() {

    var isCancelled = false

}