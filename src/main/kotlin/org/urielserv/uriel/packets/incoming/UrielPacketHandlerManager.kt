package org.urielserv.uriel.packets.incoming

import io.klogging.logger
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.handshake.ReleaseVersionPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.ClientPongPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.SecurityTicketPacketHandler
import org.urielserv.uriel.packets.incoming.navigator.NavigatorInitPacketHandler
import org.urielserv.uriel.packets.incoming.users.UserInfoPacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.UserFigurePacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.wardrobe.GetWardrobePacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.wardrobe.SaveWardrobeOutfitPacketHandler
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
        registerNavigatorPackets()
    }

    private fun registerHandshakePackets() {
        registerPacket(IncomingPacketIDs.ReleaseVersion, ReleaseVersionPacketHandler())
        registerPacket(IncomingPacketIDs.SecurityTicket, SecurityTicketPacketHandler())
        registerPacket(IncomingPacketIDs.ClientPong, ClientPongPacketHandler())
    }

    private fun registerUserPackets() {
        registerPacket(IncomingPacketIDs.UserInfo, UserInfoPacketHandler())
        registerPacket(IncomingPacketIDs.GetWardrobe, GetWardrobePacketHandler())
        registerPacket(IncomingPacketIDs.SaveWardrobeOutfit, SaveWardrobeOutfitPacketHandler())
        registerPacket(IncomingPacketIDs.UserFigure, UserFigurePacketHandler())
    }

    private fun registerNavigatorPackets() {
        registerPacket(IncomingPacketIDs.NavigatorInit, NavigatorInitPacketHandler())
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
     * Unregisters a packet with the specified packet ID.
     *
     * @param packetId The ID of the packet to unregister.
     */
    fun unregisterPacket(packetId: Int) {
        packets.remove(packetId.toShort())
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