package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorSearchesPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.NavigatorSearches

    override suspend fun construct() {
        val searches = habbo.navigatorSearches.getSavedSearches()

        appendInt(searches.size)
        for (search in searches) {
            appendInt(search.id)
            appendString(search.searchCode)
            appendString(search.filter)
            appendString("")
        }
    }

}