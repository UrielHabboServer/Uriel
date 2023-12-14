package org.urielserv.uriel.core.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import org.urielserv.uriel.core.configuration.HotelSettingOverride

object HotelSettingOverridesSchema : Table<HotelSettingOverride>("uriel_hotel_setting_overrides") {

    val path = varchar("path").primaryKey().bindTo { it.path }
    val value = varchar("value").bindTo { it.value }

}