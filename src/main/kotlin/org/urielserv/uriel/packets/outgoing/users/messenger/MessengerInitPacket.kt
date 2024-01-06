package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerInitPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.MessengerInit

    override suspend fun construct() {
        if (habbo.hasPermission("uriel.messenger.unlimited_friends")) {
            appendInt(Int.MAX_VALUE)
            appendInt(0)
            appendInt(Int.MAX_VALUE)
        } else {
            appendInt(HotelSettings.habbos.messenger.maxFriends)
            appendInt(0)
            appendInt(HotelSettings.habbos.messenger.maxFriendsWithHabboClub)
        }

        appendInt(0) // Messenger categories, unimplemented in Nitro
    }

}