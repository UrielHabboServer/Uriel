package org.urielserv.uriel.database.schemas.users.navigator

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.urielserv.uriel.database.schemas.users.UsersSchema
import org.urielserv.uriel.game.habbos.navigator.saved_searches.HabboNavigatorSavedSearch

object UserNavigatorSavedSearchesSchema : Table<HabboNavigatorSavedSearch>("user_navigator_saved_searches") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val searchCode = text("search_code").bindTo { it.searchCode }
    val filter = text("filter").bindTo { it.filter }

}