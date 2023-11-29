package org.urielserv.uriel.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.habbos.subscriptions.Subscription

object UserSubscriptionsSchema : Table<Subscription>("user_subscriptions") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val subscriptionType = varchar("subscription_type").bindTo { it.type }

    val subscriptionStart = int("start_timestamp").bindTo { it.start }
    val subscriptionEnd = int("end_timestamp").bindTo { it.end }

    val isActive = boolean("is_active").bindTo { it.isActive }

}