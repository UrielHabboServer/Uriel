package org.urielserv.uriel.packets

import io.ktor.websocket.*
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.networking.UrielServerClient

/**
 * Interface representing a packet.
 */
@Suppress("unused")
interface Packet {

    val packetId: Int
    val byteArray: ByteArrayBuilder
        get() = ByteArrayBuilder()

    /**
     * Construct method.
     *
     * This method is a suspend function that constructs and initializes a packet.
     * It is designed to be called from a coroutine context.
     */
    suspend fun construct()

    /**
     * Sends a message to the specified Habbo.
     * Acts as a wrapper for the send method that accepts a Uriel server client.
     *
     * @param habbo The Habbo to send the message to.
     */
    suspend fun send(habbo: Habbo) {
        send(habbo.client!!)
    }

    /**
     * Sends a packet to the given Uriel server client.
     *
     * @param client The Uriel server client to send the packet to.
     */
    suspend fun send(client: UrielServerClient) {
        byteArray.appendShortAtBeginning(packetId.toShort())
        construct()
        client.send(byteArray.toByteArray())
    }

}