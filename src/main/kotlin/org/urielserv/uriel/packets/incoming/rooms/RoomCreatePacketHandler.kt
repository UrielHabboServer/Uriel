package org.urielserv.uriel.packets.incoming.rooms

import io.klogging.logger
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.CanCreateRoomPacket
import org.urielserv.uriel.packets.outgoing.rooms.RoomCreatedPacket
import java.io.ByteArrayInputStream

class RoomCreatePacketHandler : PacketHandler {

    private val logger = logger(RoomCreatePacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        val roomLimit = if (client.habbo!!.subscriptions.hasActiveHabboClubMembership()) {
            HotelSettings.habbos.rooms.maxRoomsWithHabboClub
        } else {
            HotelSettings.habbos.rooms.maxRooms
        }

        if (RoomManager.getRoomsByOwner(client.habbo!!).size >= roomLimit) {
            CanCreateRoomPacket(false, roomLimit).send(client)
            return
        }

        val roomName = packet.readString()
        val roomDescription = packet.readString()
        val modelName = packet.readString()
        val categoryId = packet.readInt()
        val maxVisitors = packet.readInt()
        val tradeType = packet.readInt()

        val model = RoomManager.getRoomModelByName(modelName) ?: return
        val category = NavigatorManager.getFlatCategory(categoryId) ?: return

        logger.info("RoomCreatePacketHandler: roomName=$roomName, roomDescription=$roomDescription, modelName=$modelName, categoryId=$categoryId, maxVisitors=$maxVisitors, tradeType=$tradeType")

        val room =
            RoomManager.createRoom(client.habbo!!, roomName, roomDescription, model, category, maxVisitors, tradeType)!!

        logger.info("RoomCreatePacketHandler: room=$room")

        RoomCreatedPacket(room.info.id, room.info.name).send(client)
    }

}