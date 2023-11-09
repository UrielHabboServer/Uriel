package org.urielserv.uriel.packets

import io.klogging.logger
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.IncomingPacketIDs
import org.urielserv.uriel.packets.incoming.handshake.PongPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.ReleaseVersionPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.SecureLoginPacketHandler
import java.io.ByteArrayInputStream

/**
 * A manager class for handling Habbo/Nitro packets received from clients.
 */
class UrielPacketHandlerManager {

    private val logger = logger("uriel.packets.UrielPacketHandlerManager")

    private val packets = mutableMapOf<Short, PacketHandler>()

    init {
        registerAllPackets()
    }

    private fun registerAllPackets() {
        registerHandshakePackets()
    }

    private fun registerHandshakePackets() {
        registerPacket(IncomingPacketIDs.ReleaseVersion, ReleaseVersionPacketHandler())
        registerPacket(IncomingPacketIDs.SecureLogin, SecureLoginPacketHandler())
        registerPacket(IncomingPacketIDs.Pong, PongPacketHandler())
    }

    /**
     * Registers a packet with the specified packet ID and packet handler.
     *
     * @param packetId The ID of the packet to register.
     * @param packetHandler The handler for the registered packet.
     */
    fun registerPacket(packetId: Int, packetHandler: PacketHandler) {
        packets[packetId.toShort()] = packetHandler
    }

    /**
     * Handle a packet received from a client.
     *
     * @param packetId The ID of the packet.
     * @param client The UrielServerClient representing the client.
     * @param packet The packet data as a ByteArrayInputStream.
     * @throws NoSuchElementException if the packetId does not exist in the packets map.
     */
    suspend fun handlePacket(packetId: Short, client: UrielServerClient, packet: ByteArrayInputStream) {
        if (packets.containsKey(packetId)) {
            packets[packetId]?.handle(client, packet)
        } else {
            logger.debug("Unhandled packet: $packetId")
        }
    }

}