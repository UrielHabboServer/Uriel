package org.urielserv.uriel.database.schemas.rooms

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object RoomMoodlightsSchema : Table<Nothing>("room_moodlights") {

    val id = int("id").primaryKey()
    val roomId = int("room_id")

    val isEnabled = boolean("is_enabled")
    val isBackgroundOnly = boolean("is_background_only")

    val color = varchar("color")
    val intensity = int("intensity")

}