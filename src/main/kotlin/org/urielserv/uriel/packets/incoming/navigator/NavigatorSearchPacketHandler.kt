package org.urielserv.uriel.packets.incoming.navigator

import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.navigator.NavigatorSearchPacket
import java.nio.ByteBuffer

class NavigatorSearchPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        var searchCode = packet.getString()
        val data = packet.getString()

        if (searchCode == "query" || searchCode == "groups") {
            searchCode = "hotel_view"
        }

        val tab = NavigatorManager.getTab(searchCode) ?: return
        val searchResultLists = tab.getSearchResultListsForHabbo(habbo)

        if (data.isNotBlank()) {
            val filterId = if (data.contains(":")) {
                data.split(":")[0]
            } else {
                "anything"
            }

            val filterData = if (data.contains(":")) {
                data.split(":")[1]
            } else {
                data
            }

            NavigatorManager.getFilter(filterId)?.applyFilter(filterData, searchResultLists)
        }

        NavigatorSearchPacket(searchCode, data, searchResultLists).send(client)
    }

}