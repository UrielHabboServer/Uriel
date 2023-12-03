package org.urielserv.uriel.game.navigator

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.navigator.NavigatorPublicCategoriesSchema

class UrielNavigatorManager {

    private val publicCategories = mutableListOf<NavigatorPublicCategory>()

    init {
        loadPublicCategories()
    }

    private fun loadPublicCategories() {
        Database.sequenceOf(NavigatorPublicCategoriesSchema)
            .forEach {
                publicCategories.add(it)
            }
    }

}