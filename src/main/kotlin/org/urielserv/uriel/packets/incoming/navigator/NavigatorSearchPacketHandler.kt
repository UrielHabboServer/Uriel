package org.urielserv.uriel.packets.incoming.navigator

import io.klogging.logger
import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.navigator.NavigatorSearchPacket
import java.io.ByteArrayInputStream

class NavigatorSearchPacketHandler : PacketHandler {

    private val logger = logger(NavigatorSearchPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        var searchCode = packet.readString()
        val data = packet.readString()

        logger.info("Search code: $searchCode | Data: $data")

        if (searchCode == "query" || searchCode == "groups") {
            searchCode = "hotel_view"
        }

        val tab = NavigatorManager.getTab(searchCode) ?: return
        val searchResultLists = tab.getSearchResultListsForHabbo(client.habbo!!)

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

            logger.info("Filter ID: $filterId | Filter data: $filterData")
            logger.info("Filter found: ${NavigatorManager.getFilter(filterId) != null}")

            NavigatorManager.getFilter(filterId)?.applyFilter(filterData, searchResultLists)
        }

        NavigatorSearchPacket(searchCode, data, searchResultLists).send(client)
    }

}