package org.urielserv.uriel.packets.incoming

import io.klogging.logger
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.handshake.ClientVersionPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.PongPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.SSOTicketPacketHandler
import org.urielserv.uriel.packets.incoming.users.RetrieveUserDataPacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.UserUpdateLookPacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.saved_looks.RetrieveUserSavedLooksPacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.saved_looks.UserAddSavedLookPacketHandler
import java.io.ByteArrayInputStream

/**
 * A manager class for handling Habbo/Nitro packets received from clients.
 */
class UrielPacketHandlerManager {

    private val logger = logger(UrielPacketHandlerManager::class)

    private val packets = mutableMapOf<Short, PacketHandler>()

    init {
        registerAllPackets()
    }

    private fun registerAllPackets() {
        registerHandshakePackets()
        registerUserPackets()
    }

    private fun registerHandshakePackets() {
        registerPacket(IncomingPacketIDs.ClientVersion, ClientVersionPacketHandler())
        registerPacket(IncomingPacketIDs.SSOTicket, SSOTicketPacketHandler())
        registerPacket(IncomingPacketIDs.Pong, PongPacketHandler())
    }

    private fun registerUserPackets() {
        registerPacket(IncomingPacketIDs.RetrieveUserData, RetrieveUserDataPacketHandler())
        registerPacket(IncomingPacketIDs.RetrieveUserSavedLooks, RetrieveUserSavedLooksPacketHandler())
        registerPacket(IncomingPacketIDs.UserAddSavedLook, UserAddSavedLookPacketHandler())
        registerPacket(IncomingPacketIDs.UserUpdateLook, UserUpdateLookPacketHandler())
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
            try {
                packets[packetId]?.handle(client, packet)
            } catch (exc: Exception) {
                logger.error("Error handling packet $packetId:")
                exc.printStackTrace()
            }
        } else {
            logger.debug("Unhandled packet: $packetId")
        }
    }

}