package org.urielserv.uriel.packets.incoming.landingview

import org.urielserv.uriel.LandingViewManager
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.landing_view.PromoArticlesPacket
import java.io.ByteArrayInputStream

class GetPromoArticlesPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        PromoArticlesPacket(LandingViewManager.getPromoArticles()).send(client)
    }

}