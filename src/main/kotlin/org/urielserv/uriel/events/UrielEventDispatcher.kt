package org.urielserv.uriel.events

class UrielEventDispatcher {
    private val listeners: MutableMap<String, MutableList<(evt: UrielEvent?) -> Unit>> = mutableMapOf()

    fun on(eventType: String, callback: (UrielEvent?) -> Unit) {
        if (!listeners.containsKey(eventType)) {
            listeners[eventType] = mutableListOf()
        }

        listeners[eventType]?.add(callback)
    }

    fun off(eventType: String, callback: (UrielEvent?) -> Unit) {
        val eventListeners = listeners[eventType]

        if (eventListeners != null) {
            listeners[eventType] = eventListeners.filter { it != callback }.toMutableList()
        }
    }

    fun dispatch(eventType: String, event: UrielEvent? = null) {
        val eventListeners = listeners[eventType]

        eventListeners?.forEach { listener ->
            listener.invoke(event)
        }
    }
}