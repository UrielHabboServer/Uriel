package org.urielserv.uriel.core.database.schemas.messenger

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.urielserv.uriel.core.database.schemas.users.UsersSchema
import org.urielserv.uriel.game.habbos.messenger.interfaces.Friendship

object MessengerFriendshipsSchema : Table<Friendship>("messenger_friendships") {

    val id = int("id").primaryKey().bindTo { it.id }

    val userOneId = int("user_one_id").references(UsersSchema) { it.habboInfoOne }
    val userTwoId = int("user_two_id").references(UsersSchema) { it.habboInfoTwo }

    val userOneRelationshipId = int("user_one_relationship_id").references(MessengerRelationshipsSchema) { it.relationshipHabboOne }
    val userTwoRelationshipId = int("user_two_relationship_id").references(MessengerRelationshipsSchema) { it.relationshipHabboTwo }

    val friendshipCreationTimestamp = int("friendship_creation_timestamp").bindTo { it.creationTimestamp }

}