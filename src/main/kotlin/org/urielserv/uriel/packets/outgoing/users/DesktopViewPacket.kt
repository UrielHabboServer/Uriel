package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class DesktopViewPacket : Packet() {

    override val packetId = Outgoing.DesktopView

    override suspend fun construct() = Unit

}