package org.urielserv.uriel.core.database.schemas.messenger

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.core.database.schemas.users.UsersSchema
import org.urielserv.uriel.game.habbos.messenger.interfaces.OfflineMessage

object MessengerOfflineMessagesSchema : Table<OfflineMessage>("messenger_offline_messages") {

    val id = int("id").primaryKey().bindTo { it.id }

    val senderId = int("sender_id").references(UsersSchema) { it.senderHabboInfo }
    val receiverId = int("receiver_id").references(UsersSchema) { it.receiverHabboInfo }

    val timestamp = int("timestamp").bindTo { it.timestamp }

    val message = varchar("message").bindTo { it.message }

}