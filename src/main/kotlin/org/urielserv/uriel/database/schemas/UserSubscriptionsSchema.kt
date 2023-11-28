package org.urielserv.uriel.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserSubscriptionsSchema : Table<Nothing>("user_subscriptions") {

    val id = int("id").primaryKey()
    val userId = int("user_id")

    val subscriptionType = varchar("subscription_type")

    val subscriptionStart = int("start_timestamp")
    val subscriptionEnd = int("end_timestamp")

    val isActive = boolean("is_active")

}