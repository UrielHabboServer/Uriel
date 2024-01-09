package org.urielserv.uriel.packets.incoming.rooms

import io.klogging.logger
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.rooms.RoomCreateEvent
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.CanCreateRoomPacket
import org.urielserv.uriel.packets.outgoing.rooms.RoomCreatedPacket
import java.nio.ByteBuffer

class RoomCreatePacketHandler : PacketHandler {

    val logger = logger(RoomCreatePacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val roomLimit = if (habbo.subscriptions.hasActiveHabboClubMembership()) {
            HotelSettings.habbos.rooms.maxRoomsWithHabboClub
        } else {
            HotelSettings.habbos.rooms.maxRooms
        }

        if (RoomManager.getRooms(habbo).size >= roomLimit) {
            CanCreateRoomPacket(false, roomLimit).send(client)
            return
        }

        val roomName = packet.getString()
        val roomDescription = packet.getString()
        val modelName = packet.getString()
        val categoryId = packet.getInt()
        val maxVisitors = packet.getInt()
        val tradeType = packet.getInt()

        val model = RoomManager.getRoomModel(modelName)

        if (model == null) {
            logger.warn("${habbo.info.username} attempted to create a room with a non-existent model: $modelName")
            return
        }

        val category = NavigatorManager.getFlatCategory(categoryId)

        if (category == null) {
            logger.warn("${habbo.info.username} attempted to create a room with a non-existent category: $categoryId")
            return
        }

        val event = RoomCreateEvent(habbo, roomName, roomDescription, model, category, maxVisitors, tradeType)
        EventDispatcher.dispatch(Events.RoomCreate, event)

        if (event.isCancelled) return

        val room =
            RoomManager.createRoom(
                habbo,
                event.name,
                event.description,
                event.model,
                event.category,
                event.maxVisitors,
                event.tradeType
            )!!

        RoomCreatedPacket(room.info.id, room.info.name).send(client)
    }

}