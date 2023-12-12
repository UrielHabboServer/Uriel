package org.urielserv.uriel.configuration

import org.ktorm.entity.Entity

interface HotelSettingOverride : Entity<HotelSettingOverride> {

    val path: String
    val value: String

}