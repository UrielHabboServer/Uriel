package org.urielserv.uriel.packets.incoming.landingview

import org.urielserv.uriel.LandingViewManager
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.landing_view.PromoArticlesPacket
import java.nio.ByteBuffer

class GetPromoArticlesPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        PromoArticlesPacket(LandingViewManager.getPromoArticles()).send(client)
    }

}