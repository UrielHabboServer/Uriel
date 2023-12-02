package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorSettingsPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = OutgoingPacketIDs.NavigatorSettings

    override suspend fun construct() {
        appendInt(habbo.navigatorWindowSettings.x)
        appendInt(habbo.navigatorWindowSettings.y)
        appendInt(habbo.navigatorWindowSettings.width)
        appendInt(habbo.navigatorWindowSettings.height)
        appendBoolean(habbo.navigatorWindowSettings.isLeftPanelOpen)
        appendInt(habbo.navigatorWindowSettings.resultsMode)
    }

}