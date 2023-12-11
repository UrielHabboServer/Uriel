package org.urielserv.uriel.database.schemas.navigator

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.navigator.NavigatorFlatCategory

object NavigatorFlatCategoriesSchema : Table<NavigatorFlatCategory>("navigator_flat_categories") {

    val id = int("id").primaryKey().bindTo { it.id }
    val caption = varchar("caption").bindTo { it.caption }

    val minimumRankWeight = int("minimum_rank_weight").bindTo { it.minimumRankWeight }
    val maximumUsers = int("maximum_users").bindTo { it.maximumUsers }

    val isPublic = boolean("is_public").bindTo { it.isPublic }
    val allowTrading = boolean("allow_trading").bindTo { it.allowTrading }

    val orderNum = int("order_num").bindTo { it.orderNum }

}