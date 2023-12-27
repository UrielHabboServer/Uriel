package org.urielserv.uriel.packets.incoming

import org.urielserv.uriel.networking.UrielServerClient
import java.nio.ByteBuffer

/**
 * This interface represents a packet handler that is responsible for handling incoming packets from clients.
 */
interface PacketHandler {

    /**
     * Handles an incoming packet.
     *
     * @param client The UrielServerClient object representing the packet sender.
     * @param packet The ByteBuffer containing the incoming packet.
     */
    suspend fun handle(client: UrielServerClient, packet: ByteBuffer)

}