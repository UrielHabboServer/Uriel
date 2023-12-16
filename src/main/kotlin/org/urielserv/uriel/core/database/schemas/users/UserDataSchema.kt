package org.urielserv.uriel.core.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.urielserv.uriel.core.database.schemas.rooms.ChatBubblesSchema
import org.urielserv.uriel.game.habbos.HabboData

object UserDataSchema : Table<HabboData>("user_data") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val chatBubbleId = int("chat_bubble_id").references(ChatBubblesSchema) { it.chatBubble }

    val isAmbassador = boolean("is_ambassador").bindTo { it.isAmbassador }

}