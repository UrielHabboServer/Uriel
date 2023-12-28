package org.urielserv.uriel.game.habbos.notifications

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.notifications.complex.ComplexNotification
import org.urielserv.uriel.packets.outgoing.users.notifications.InClientLinkPacket
import org.urielserv.uriel.packets.outgoing.users.notifications.MotdMessagesPacket
import org.urielserv.uriel.packets.outgoing.users.notifications.NotificationDialogMessagePacket
import org.urielserv.uriel.packets.outgoing.users.notifications.SimpleAlertMessagePacket

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
    suspend fun sendNotification(type: String, vararg args: Pair<String, String>) {
        NotificationDialogMessagePacket(type, *args).send(habbo)
    }

    /**
     * Sends the Habbo a complex notification (wrapper around [sendNotification])
     * This is a more convenient way to send a complex notification
     *
     * @param complexNotification Complex notification to send
     */
    suspend fun sendNotification(complexNotification: ComplexNotification) {
        NotificationDialogMessagePacket(complexNotification.type, *complexNotification.build().toTypedArray()).send(
            habbo
        )
    }

    /**
     * Sends the Habbo a list of MOTD messages that can contain HTML
     *
     * @param messages List of messages to send
     */
    suspend fun sendMotdMessages(vararg messages: String) {
        MotdMessagesPacket(*messages).send(habbo)
    }

    /**
     * Opens a link in the client for the Habbo
     *
     * @param link Link to open
     */
    suspend fun sendInClientLink(link: String) {
        InClientLinkPacket(link).send(habbo)
    }

}