package org.urielserv.uriel.packets.incoming

import io.klogging.logger
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.handshake.ClientPongPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.ReleaseVersionPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.SecurityTicketPacketHandler
import org.urielserv.uriel.packets.incoming.navigator.NavigatorInitPacketHandler
import org.urielserv.uriel.packets.incoming.navigator.NavigatorSearchPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.GetUserFlatCatsPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.RoomCreatePacketHandler
import org.urielserv.uriel.packets.incoming.rooms.RoomEnterPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.RoomModelPacketHandler
import org.urielserv.uriel.packets.incoming.users.DesktopViewPacketHandler
import org.urielserv.uriel.packets.incoming.users.UserInfoPacketHandler
import org.urielserv.uriel.packets.incoming.users.currencies.UserCurrencyPacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.UserFigurePacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.wardrobe.GetWardrobePacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.wardrobe.SaveWardrobeOutfitPacketHandler
import org.urielserv.uriel.packets.incoming.users.subscriptions.UserSubscriptionPacketHandler
import org.urielserv.uriel.packets.incoming.landingview.GetPromoArticlesPacketHandler
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
        registerLandingViewPackets()
        registerNavigatorPackets()
        registerRoomPackets()
    }

    private fun registerHandshakePackets() {
        registerPacket(Incoming.ReleaseVersion, ReleaseVersionPacketHandler())
        registerPacket(Incoming.SecurityTicket, SecurityTicketPacketHandler())
        registerPacket(Incoming.ClientPong, ClientPongPacketHandler())
    }

    private fun registerUserPackets() {
        registerPacket(Incoming.UserInfo, UserInfoPacketHandler())
        registerPacket(Incoming.UserSubscription, UserSubscriptionPacketHandler())
        registerPacket(Incoming.UserCurrency, UserCurrencyPacketHandler())
        registerPacket(Incoming.GetWardrobe, GetWardrobePacketHandler())
        registerPacket(Incoming.SaveWardrobeOutfit, SaveWardrobeOutfitPacketHandler())
        registerPacket(Incoming.UserFigure, UserFigurePacketHandler())
    }

    private fun registerLandingViewPackets() {
        registerPacket(Incoming.DesktopView, DesktopViewPacketHandler())
        registerPacket(Incoming.GetPromoArticles, GetPromoArticlesPacketHandler())
    }

    private fun registerNavigatorPackets() {
        registerPacket(Incoming.NavigatorInit, NavigatorInitPacketHandler())
        registerPacket(Incoming.NavigatorSearch, NavigatorSearchPacketHandler())
    }

    private fun registerRoomPackets() {
        registerPacket(Incoming.GetUserFlatCats, GetUserFlatCatsPacketHandler())
        registerPacket(Incoming.RoomCreate, RoomCreatePacketHandler())
        registerPacket(Incoming.RoomEnter, RoomEnterPacketHandler())
        registerPacket(Incoming.RoomModel, RoomModelPacketHandler()) // -> finishes loading the room
        registerPacket(Incoming.FurnitureAliases, RoomModelPacketHandler()) // -> finishes loading the room
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
        if (packetId in packets) {
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