package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.rooms.RoomEnterEvent
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.landing_view.DesktopViewPacket
import java.io.ByteArrayInputStream

class RoomEnterPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        val roomId = packet.readInt()

        val room = RoomManager.getRoomById(roomId)

        if (room == null) {
            DesktopViewPacket().send(client)
            return
        }

        val event = RoomEnterEvent(client.habbo!!, room)
        EventDispatcher.dispatch(Events.RoomEnter, event)

        if (event.isCancelled) {
            DesktopViewPacket().send(client)
            return
        }

        room.enter(client.habbo!!)
    }

}