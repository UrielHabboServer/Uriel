package org.urielserv.uriel.game.habbos

import org.urielserv.uriel.packets.outgoing.notifications.SimpleAlertMessagePacket
import org.urielserv.uriel.packets.outgoing.notifications.NotificationDialogMessagePacket

class HabboNotifications(
    private val habbo: Habbo
) {

    /**
     * Sends an alert message to the Habbo
     *
     * @param message Message to show
     * @param title Without title, will default to "Message From Habbo Hotel"
     */
    suspend fun sendAlert(message: String, title: String? = null) {
        SimpleAlertMessagePacket(message, title).send(habbo)
    }

    /**
     * Sends the Habbo a notification, bubble alert, window, etc...
     *
     * @param type Key of notification
     * @param args display, title, message, linkTitle, linkUrl, image ... etc
     */
    suspend fun sendNotification(type: String, vararg args: Pair<String,String>) {
        NotificationDialogMessagePacket(type, *args).send(habbo)
    }

}