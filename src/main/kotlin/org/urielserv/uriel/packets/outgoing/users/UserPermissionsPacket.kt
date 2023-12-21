package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserPermissionsPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.UserPermissions

    override suspend fun construct() {
        appendInt(habbo.subscriptions.getHabboClubSubscription()?.level ?: 0)
        appendInt(0) // Security level, unused in Nitro
        appendBoolean(habbo.data.isAmbassador)
    }

}