package org.urielserv.uriel.database.schemas.users.navigator

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.urielserv.uriel.database.schemas.users.UsersSchema
import org.urielserv.uriel.game.habbos.navigator.HabboNavigatorWindowSettings

object UserNavigatorWindowSettingsSchema
    : Table<HabboNavigatorWindowSettings>("user_navigator_window_settings") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val x = int("x").bindTo { it.x }
    val y = int("y").bindTo { it.y }
    val width = int("width").bindTo { it.width }
    val height = int("height").bindTo { it.height }

    val isLeftPanelOpen = boolean("is_left_panel_open").bindTo { it.isLeftPanelOpen }
    val resultsMode = int("results_mode").bindTo { it.resultsMode }

}