package org.urielserv.uriel.game.landing_view

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.landing_view.LandingViewPromoArticlesSchema

class UrielLandingViewManager {

    private val articles = mutableListOf<LandingViewPromoArticle>()
    
    init {
        loadPromos()
    }

    private fun loadPromos() {
        Database.sequenceOf(LandingViewPromoArticlesSchema)
            .forEach {
                articles.add(it)
            }
    }

    fun getPromoArticles(): List<LandingViewPromoArticle> {
        return articles
    }
}