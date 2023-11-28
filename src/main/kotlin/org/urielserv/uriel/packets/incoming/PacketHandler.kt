package org.urielserv.uriel.packets.incoming

import org.urielserv.uriel.networking.UrielServerClient
import java.io.ByteArrayInputStream

/**
 * This interface represents a packet handler that is responsible for handling incoming packets from clients.
 */
interface PacketHandler {

    /**
     * Handles a client request packet.
     *
     * @param client The UrielServerClient object representing the client making the request.
     * @param packet The ByteArrayInputStream containing the client request packet.
     */
    suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream)

}