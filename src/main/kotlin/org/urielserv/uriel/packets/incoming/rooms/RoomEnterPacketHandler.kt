package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.rooms.RoomEnterEvent
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.landing_view.DesktopViewPacket
import java.nio.ByteBuffer

class RoomEnterPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val roomId = packet.getInt()

        val room = RoomManager.getRoom(roomId)

        if (room == null) {
            DesktopViewPacket().send(client)
            return
        }

        val event = RoomEnterEvent(habbo, room)
        EventDispatcher.dispatch(Events.RoomEnter, event)

        if (event.isCancelled) {
            DesktopViewPacket().send(client)
            return
        }

        room.enter(habbo)
    }

}