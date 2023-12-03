package org.urielserv.uriel.database.schemas.navigator

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.navigator.NavigatorPublicCategory

object NavigatorPublicCategoriesSchema : Table<NavigatorPublicCategory>("navigator_public_categories") {

    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }

    val hasImage = boolean("has_image").bindTo { it.hasImage }
    val isVisible = boolean("is_visible").bindTo { it.isVisible }
    val orderNum = int("order_num").bindTo { it.order }

}