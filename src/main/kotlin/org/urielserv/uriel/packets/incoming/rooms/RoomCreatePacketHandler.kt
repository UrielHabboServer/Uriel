package org.urielserv.uriel.packets.incoming.rooms

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

        val room =
            RoomManager.createRoom(client.habbo!!, roomName, roomDescription, model, category, maxVisitors, tradeType)!!

        RoomCreatedPacket(room.info.id, room.info.name).send(client)
    }

}