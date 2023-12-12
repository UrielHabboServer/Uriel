package org.urielserv.uriel.game.habbos

import org.urielserv.uriel.packets.outgoing.notifications.SimpleAlertMessagePacket

class HabboNotifications(
    private val habbo: Habbo
) {

    suspend fun sendAlert(message: String, title: String? = null) {
        SimpleAlertMessagePacket(message, title).send(habbo)
    }

    //TODO: suspend fun sendNotification()

}