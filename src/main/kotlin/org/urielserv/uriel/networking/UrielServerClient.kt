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

    var nitroInformation: NitroInformation? = null

    /**
     * Suspends the execution and disposes the resources used by the client.
     *
     * This method closes the socket server session, removes the client from the server, and unloads the associated Habbo, if any.
     */
    suspend fun dispose() {
        try {
            socketServerSession.close()
        } catch (ignored: Exception) { }

        Server.disposeClient(this)
        if (habbo != null)
            HabboManager.unloadHabbo(habbo!!)
    }

    suspend fun send(bytes: ByteArray) {
        socketServerSession.send(bytes)
    }

    data class NitroInformation(
        val releaseVersion: String,
        val type: String,
        val platform: Platform,
        val deviceCategory: DeviceCategory
    ) {

        var time: Int = 0

        enum class Platform {

            UNKNOWN,
            FLASH,
            HTML5;

            companion object {

                fun getFromNumber(number: Int): Platform {
                    return when (number) {
                        1 -> FLASH
                        2 -> HTML5
                        else -> UNKNOWN
                    }
                }

            }

        }

        enum class DeviceCategory {

            UNKNOWN,
            BROWSER;

            companion object {

                fun getFromNumber(number: Int): DeviceCategory {
                    return when (number) {
                        1 -> BROWSER
                        else -> UNKNOWN
                    }
                }

            }

        }

    }

}