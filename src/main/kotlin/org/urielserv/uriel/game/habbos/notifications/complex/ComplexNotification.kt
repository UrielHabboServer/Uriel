package org.urielserv.uriel.game.habbos.notifications.complex

interface ComplexNotification {

    val type: String

    fun build(): List<Pair<String, String>>

}