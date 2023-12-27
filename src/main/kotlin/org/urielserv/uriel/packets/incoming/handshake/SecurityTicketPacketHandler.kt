package org.urielserv.uriel.packets.incoming.handshake

import io.klogging.logger
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.users.UserLoginEvent
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.handshake.ClientPingPacket
import org.urielserv.uriel.packets.outgoing.users.NoobnessLevelPacket
import org.urielserv.uriel.packets.outgoing.users.UserHomeRoomPacket
import org.urielserv.uriel.packets.outgoing.users.UserPermissionsPacket
import org.urielserv.uriel.packets.outgoing.users.inventory.UserEffectListPacket
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket
import java.nio.ByteBuffer

class SecurityTicketPacketHandler : PacketHandler {

    private val logger = logger(SecurityTicketPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val ticket = packet.getString()
        val time = packet.getInt()

        if (client.nitroInformation != null) {
            client.nitroInformation!!.time = time
        }

        if (ticket.isEmpty()) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent an empty SSO ticket")
            return
        }

        if (client.habbo != null) {
            client.dispose()
            logger.warn("Client ${client.ip}:${client.port} sent a second SSO ticket")
            return
        }

        HabboManager.tryLoginHabbo(ticket, client)
    }

}