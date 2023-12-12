package org.urielserv.uriel.packets.outgoing.landingview

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.game.landingview.LandingViewPromoArticle

class PromoArticlesPacket(
    private val articles: List<LandingViewPromoArticle>
) : Packet() {
    
    override val packetId = Outgoing.PromoArticles

    override suspend fun construct() {
        appendInt(articles.size)

        for (article in articles) {
            appendInt(article.id)
            appendString(article.title)
            appendString(article.bodyText)
            appendString(article.buttonText)
            appendInt(0) // link type not used in nitro
            appendString(article.buttonAction)
            appendString(article.imageUrl)
        }
    }

}