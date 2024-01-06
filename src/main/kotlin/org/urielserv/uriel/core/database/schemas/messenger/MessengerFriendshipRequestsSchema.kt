package org.urielserv.uriel.core.database.schemas.messenger

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.urielserv.uriel.core.database.schemas.users.UsersSchema
import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest

object MessengerFriendshipRequestsSchema : Table<FriendshipRequest>("messenger_friendship_requests") {

    val id = int("id").primaryKey().bindTo { it.id }

    val senderId = int("sender_id").references(UsersSchema) { it.senderHabboInfo }
    val receiverId = int("receiver_id").references(UsersSchema) { it.receiverHabboInfo }

}