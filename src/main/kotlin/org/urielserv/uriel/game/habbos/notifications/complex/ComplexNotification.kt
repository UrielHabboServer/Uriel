package org.urielserv.uriel.game.habbos.notifications.complex

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.rooms.Room

interface ComplexNotification {

    val type: String

    fun build(): List<Pair<String, String>>

    suspend fun send(habbo: Habbo) {
        habbo.notifications.notifyComplex(this)
    }

    suspend fun broadcast(habbos: List<Habbo>) {
        habbos.forEach { send(it) }
    }

    suspend fun broadcast(vararg habbos: Habbo) {
        broadcast(habbos.toList())
    }

    suspend fun broadcast(room: Room) {
        broadcast(room.habbos)
    }

    suspend fun broadcast() {
        broadcast(HabboManager.onlineHabbos)
    }

}