package org.urielserv.uriel.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import org.urielserv.uriel.configuration.HotelSettingOverride

object HotelSettingsOverridesSchema : Table<HotelSettingOverride>("uriel_hotel_settings_overrides") {

    val path = varchar("path").primaryKey().bindTo { it.path }
    val value = varchar("value").bindTo { it.value }

}