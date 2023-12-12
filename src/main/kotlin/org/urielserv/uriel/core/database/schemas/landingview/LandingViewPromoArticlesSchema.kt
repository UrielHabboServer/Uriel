package org.urielserv.uriel.core.database.schemas.landingview

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.landingview.LandingViewPromoArticle

object LandingViewPromoArticleSchema : Table<LandingViewPromoArticle>("landingview_articles") {

    val id = int("id").primaryKey().bindTo { it.id }
    var title = varchar("title").primaryKey().bindTo { it.title }
    var bodyText = varchar("body_text").primaryKey().bindTo { it.bodyText }
    var buttonText = varchar("button_text").primaryKey().bindTo { it.buttonText }
    var buttonAction = varchar("button_action").primaryKey().bindTo { it.buttonAction }
    var imageUrl = varchar("image_url").primaryKey().bindTo { it.imageUrl }

}