package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.game.navigator.searches.NavigatorSearchResultList
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorSearchPacket(
    private val searchCode: String,
    private val data: String,
    private val searchResultLists: List<NavigatorSearchResultList>
) : Packet() {

    override val packetId = Outgoing.NavigatorSearch

    override suspend fun construct() {
        appendString(searchCode)
        appendString(data)

        appendInt(searchResultLists.size)
        for (searchResultList in searchResultLists) {
            searchResultList.appendToPacket(this)
        }
    }

}