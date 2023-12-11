package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorCategoriesPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.NavigatorCategories

    override suspend fun construct() {
        val flatCategories = NavigatorManager.getFlatCategories().filter {
            it.minimumRankWeight <= habbo.info.rank.weight
        }

        appendInt(flatCategories.size)
        for (category in flatCategories) {
            appendInt(category.id)
            appendString(category.caption)
            appendBoolean(true)
            appendBoolean(false)
            appendString(category.caption)

            if (category.caption.startsWith("\${")) {
                appendString("category.caption")
            } else {
                appendString(category.caption)
            }

            appendBoolean(false)
        }
    }

}