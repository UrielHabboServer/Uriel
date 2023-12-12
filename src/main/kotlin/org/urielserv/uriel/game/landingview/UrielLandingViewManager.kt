package org.urielserv.uriel.game.landingview

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.landingview.LandingViewPromoArticleSchema

class UrielLandingViewManager {

    private val articles = mutableListOf<LandingViewPromoArticle>()
    
    init {
        loadPromos()
    }

    private fun loadPromos() {
        Database.sequenceOf(LandingViewPromoArticleSchema)
            .forEach {
                articles.add(it)
            }
    }

    fun getPromoArticles(): List<LandingViewPromoArticle> {
        return articles
    }
}