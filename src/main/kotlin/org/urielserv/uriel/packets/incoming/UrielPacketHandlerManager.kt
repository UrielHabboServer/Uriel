package org.urielserv.uriel.packets.incoming

import io.klogging.logger
import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.handshake.ClientPongPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.ReleaseVersionPacketHandler
import org.urielserv.uriel.packets.incoming.handshake.SecurityTicketPacketHandler
import org.urielserv.uriel.packets.incoming.landingview.GetPromoArticlesPacketHandler
import org.urielserv.uriel.packets.incoming.navigator.NavigatorInitPacketHandler
import org.urielserv.uriel.packets.incoming.navigator.NavigatorSearchPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.*
import org.urielserv.uriel.packets.incoming.rooms.user_unit.RoomUnitLookPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.user_unit.RoomUnitWalkPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.user_unit.chat.RoomUnitChatPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.user_unit.chat.RoomUnitStopTypingPacketHandler
import org.urielserv.uriel.packets.incoming.rooms.user_unit.chat.RoomUnitTypingPacketHandler
import org.urielserv.uriel.packets.incoming.users.DesktopViewPacketHandler
import org.urielserv.uriel.packets.incoming.users.UserInfoPacketHandler
import org.urielserv.uriel.packets.incoming.users.UserProfilePacketHandler
import org.urielserv.uriel.packets.incoming.users.messenger.MessengerRelationshipsPacketHandler
import org.urielserv.uriel.packets.incoming.users.currencies.UserCurrencyPacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.UserFigurePacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.wardrobe.GetWardrobePacketHandler
import org.urielserv.uriel.packets.incoming.users.looks.wardrobe.SaveWardrobeOutfitPacketHandler
import org.urielserv.uriel.packets.incoming.users.messenger.*
import org.urielserv.uriel.packets.incoming.users.subscriptions.UserSubscriptionPacketHandler
import java.nio.BufferOverflowException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

/**
 * A manager class for handling Habbo/Nitro packets received from clients.
 */
class UrielPacketHandlerManager {

    private val logger = logger(UrielPacketHandlerManager::class)

    private val packetHandlers = mutableMapOf<Short, PacketHandler>()

    init {
        registerAllPacketHandlers()
    }

    private fun registerAllPacketHandlers() {
        registerHandshakePacketHandlers()
        registerUserPacketHandlers()
        registerMessengerPacketHandlers()
        registerLandingViewPacketHandlers()
        registerNavigatorPacketHandlers()
        registerRoomPacketHandlers()
    }

    private fun registerHandshakePacketHandlers() {
        registerPacket(Incoming.ReleaseVersion, ReleaseVersionPacketHandler())
        registerPacket(Incoming.SecurityTicket, SecurityTicketPacketHandler())
        registerPacket(Incoming.ClientPong, ClientPongPacketHandler())
    }

    private fun registerUserPacketHandlers() {
        registerPacket(Incoming.UserInfo, UserInfoPacketHandler())
        registerPacket(Incoming.UserSubscription, UserSubscriptionPacketHandler())
        registerPacket(Incoming.UserCurrency, UserCurrencyPacketHandler())
        registerPacket(Incoming.GetWardrobe, GetWardrobePacketHandler())
        registerPacket(Incoming.SaveWardrobeOutfit, SaveWardrobeOutfitPacketHandler())
        registerPacket(Incoming.UserFigure, UserFigurePacketHandler())
        registerPacket(Incoming.UserProfile, UserProfilePacketHandler())
    }

    private fun registerMessengerPacketHandlers() {
        registerPacket(Incoming.MessengerInit, MessengerInitPacketHandler())
        registerPacket(Incoming.MessengerChat, MessengerChatPacketHandler())
        registerPacket(Incoming.RequestFriend, RequestFriendPacketHandler())
        registerPacket(Incoming.AcceptFriend, AcceptFriendPacketHandler())
        registerPacket(Incoming.DeclineFriend, DeclineFriendPacketHandler())
        registerPacket(Incoming.RemoveFriend, RemoveFriendPacketHandler())
        registerPacket(Incoming.FriendListUpdate, FriendListUpdatePacketHandler())
        registerPacket(Incoming.GetFriendRequests, GetFriendRequestsPacketHandler())
        registerPacket(Incoming.FollowFriend, FollowFriendPacketHandler())
        registerPacket(Incoming.SetRelationshipStatus, SetRelationshipStatusPacketHandler())
        registerPacket(Incoming.MessengerRelationships, MessengerRelationshipsPacketHandler())
        registerPacket(Incoming.MessengerFindFriends, MessengerFindFriendsPacketHandler())
        registerPacket(Incoming.SendRoomInvite, SendRoomInvitePacketHandler())
    }

    private fun registerLandingViewPacketHandlers() {
        registerPacket(Incoming.DesktopView, DesktopViewPacketHandler())
        registerPacket(Incoming.GetPromoArticles, GetPromoArticlesPacketHandler())
    }

    private fun registerNavigatorPacketHandlers() {
        registerPacket(Incoming.NavigatorInit, NavigatorInitPacketHandler())
        registerPacket(Incoming.NavigatorSearch, NavigatorSearchPacketHandler())
    }

    private fun registerRoomPacketHandlers() {
        registerPacket(Incoming.GetUserFlatCats, GetUserFlatCatsPacketHandler())
        registerPacket(Incoming.RoomCreate, RoomCreatePacketHandler())
        registerPacket(Incoming.RoomEnter, RoomEnterPacketHandler())
        registerPacket(Incoming.RoomModel, RoomModelPacketHandler()) // -> finishes loading the room
        registerPacket(Incoming.FurnitureAliases, RoomModelPacketHandler()) // -> finishes loading the room
        registerPacket(Incoming.GetGuestRoom, GetGuestRoomPacketHandler()) // -> finishes loading the room

        registerPacket(Incoming.RoomUnitWalk, RoomUnitWalkPacketHandler())
        registerPacket(Incoming.RoomUnitLook, RoomUnitLookPacketHandler())
        registerPacket(Incoming.RoomUnitTyping, RoomUnitTypingPacketHandler())
        registerPacket(Incoming.RoomUnitStopTyping, RoomUnitStopTypingPacketHandler())
        registerPacket(Incoming.RoomUnitChat, RoomUnitChatPacketHandler(RoomChatMessage.ChatType.TALK))
        registerPacket(Incoming.RoomUnitChatShout, RoomUnitChatPacketHandler(RoomChatMessage.ChatType.SHOUT))
        registerPacket(Incoming.RoomUnitChatWhisper, RoomUnitChatPacketHandler(RoomChatMessage.ChatType.WHISPER))
    }

    /**
     * Registers a packet with the specified packet ID and packet handler.
     *
     * @param packetId The ID of the packet to register.
     * @param packetHandler The handler for the registered packet.
     */
    fun registerPacket(packetId: Int, packetHandler: PacketHandler) {
        packetHandlers[packetId.toShort()] = packetHandler
    }

    /**
     * Unregisters a packet with the specified packet ID.
     *
     * @param packetId The ID of the packet to unregister.
     */
    fun unregisterPacket(packetId: Int) {
        packetHandlers.remove(packetId.toShort())
    }

    /**
     * Handle a packet received from a client.
     *
     * @param packetId The ID of the packet.
     * @param client The UrielServerClient representing the client.
     * @param packet The packet data as a ByteArrayInputStream.
     * @throws NoSuchElementException if the packetId does not exist in the packets map.
     */
    suspend fun handlePacket(packetId: Short, client: UrielServerClient, packet: ByteBuffer) {
        if (packetId in packetHandlers) {
            try {
                packetHandlers[packetId]?.handle(client, packet)
            } catch (exc: Exception) {
                when (exc) {
                    is BufferUnderflowException, is BufferOverflowException -> {
                        logger.warn("Client ${client.ip}:${client.port} sent a malformed packet $packetId")

                        if (client.habbo != null) {
                            client.habbo!!.disconnect()
                        } else {
                            client.dispose()
                        }
                    }
                    else -> {
                        logger.error("Error handling packet $packetId:")
                        exc.printStackTrace()
                    }
                }
            }
        } else {
            logger.debug("Unhandled packet: $packetId")
        }
    }

}