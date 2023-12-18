package org.urielserv.uriel.core.event_dispatcher

class UrielEventDispatcher {

    private val listeners: MutableMap<String, MutableList<suspend (UrielEvent) -> Unit>> = mutableMapOf()

    fun on(eventType: String, callback: suspend (UrielEvent) -> Unit) {
        if (!listeners.containsKey(eventType)) {
            listeners[eventType] = mutableListOf()
        }

        listeners[eventType]?.add(callback)
    }

    fun off(eventType: String, callback: suspend (UrielEvent) -> Unit) {
        val eventListeners = listeners[eventType]

        if (eventListeners != null) {
            listeners[eventType] = eventListeners.filter { it != callback }.toMutableList()
        }
    }

    suspend fun dispatch(eventType: String, event: UrielEvent) {
        val eventListeners = listeners[eventType]

        eventListeners?.forEach { listener ->
            listener.invoke(event)
        }
    }

}