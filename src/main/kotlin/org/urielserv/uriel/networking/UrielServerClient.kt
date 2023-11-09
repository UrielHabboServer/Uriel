package org.urielserv.uriel.networking

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.Server
import org.urielserv.uriel.game.habbos.Habbo

/**
 * UrielServerClient represents a client connected to the server.
 *
 * @property ip The IP address of the client.
 * @property port The port of the client.
 * @property socketServerSession The WebSocket server session associated with the client.
 * @property habbo The associated Habbo object for the client.
 */
class UrielServerClient(
    val ip: String,
    val port: Int,
    val socketServerSession: WebSocketServerSession
) {

    var habbo: Habbo? = null

    /**
     * Suspends the execution and disposes the resources used by the client.
     *
     * This method closes the socket server session, removes the client from the server, and unloads the associated Habbo, if any.
     *
     * @throws Exception if an error occurs while disposing the resources.
     */
    suspend fun dispose() {
        socketServerSession.close()

        Server.disposeClient(this)
        if (habbo != null)
            HabboManager.unloadHabbo(habbo!!)
    }

}