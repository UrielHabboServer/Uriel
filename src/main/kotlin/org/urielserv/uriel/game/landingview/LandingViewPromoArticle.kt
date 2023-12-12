package org.urielserv.uriel.game.landingview

import org.ktorm.entity.Entity

interface LandingViewPromoArticle : Entity<LandingViewPromoArticle> {

    val id: Int
    var title: String
    var bodyText: String
    var buttonText: String
    var buttonAction: String
    var imageUrl: String
}