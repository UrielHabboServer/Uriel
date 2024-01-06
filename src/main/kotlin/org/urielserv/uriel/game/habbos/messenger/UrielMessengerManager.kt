package org.urielserv.uriel.game.habbos.messenger

import org.ktorm.entity.add
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.messenger.MessengerFriendshipRequestsSchema
import org.urielserv.uriel.core.database.schemas.messenger.MessengerFriendshipsSchema
import org.urielserv.uriel.core.database.schemas.messenger.MessengerOfflineMessagesSchema
import org.urielserv.uriel.core.database.schemas.messenger.MessengerRelationshipsSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.habbos.messenger.interfaces.Friendship
import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest
import org.urielserv.uriel.game.habbos.messenger.interfaces.OfflineMessage
import org.urielserv.uriel.game.habbos.messenger.interfaces.Relationship

class UrielMessengerManager {

    private val relationships = mutableListOf<Relationship>()

    private val friendships = mutableListOf<Friendship>()
    private val friendshipRequests = mutableListOf<FriendshipRequest>()

    private val offlineMessages = mutableListOf<OfflineMessage>()

    init {
        Database.sequenceOf(MessengerRelationshipsSchema)
            .forEach {
                relationships.add(it)
            }

        Database.sequenceOf(MessengerFriendshipsSchema)
            .forEach {
                friendships.add(it)
            }

        Database.sequenceOf(MessengerFriendshipRequestsSchema)
            .forEach {
                friendshipRequests.add(it)
            }

        Database.sequenceOf(MessengerOfflineMessagesSchema)
            .forEach {
                offlineMessages.add(it)
            }
    }

    fun getRelationships(): List<Relationship> {
        return relationships
    }

    fun isValidRelationship(relationshipId: Int): Boolean {
        return relationships.any { it.id == relationshipId }
    }

    fun getRelationshipById(relationshipId: Int): Relationship? {
        return relationships.firstOrNull { it.id == relationshipId }
    }

    fun getFriendshipsByHabbo(habbo: Habbo): List<Friendship> {
        return getFriendshipsByHabboInfo(habbo.info)
    }

    fun getFriendshipsByHabboInfo(habboInfo: HabboInfo): List<Friendship> {
        return getFriendshipsByHabboId(habboInfo.id)
    }

    fun getFriendshipsByHabboId(habboId: Int): List<Friendship> {
        return friendships.filter { it.habboInfoOne.id == habboId || it.habboInfoTwo.id == habboId }
    }

    suspend fun addFriendship(friendship: Friendship) {
        friendships.add(friendship)

        friendship.habboInfoOne.habbo?.messenger?.addFriendship(friendship)
        friendship.habboInfoTwo.habbo?.messenger?.addFriendship(friendship)

        Database.sequenceOf(MessengerFriendshipsSchema)
            .add(friendship)
    }

    suspend fun deleteFriendship(friendship: Friendship) {
        friendships.remove(friendship)

        friendship.habboInfoOne.habbo?.messenger?.deleteFriendship(friendship)
        friendship.habboInfoTwo.habbo?.messenger?.deleteFriendship(friendship)

        friendship.delete()
    }

    fun getFriendshipRequestsByHabbo(habbo: Habbo): List<FriendshipRequest> {
        return getFriendshipRequestsByHabboInfo(habbo.info)
    }

    fun getFriendshipRequestsByHabboInfo(habboInfo: HabboInfo): List<FriendshipRequest> {
        return getFriendshipRequestsByHabboId(habboInfo.id)
    }

    fun getFriendshipRequestsByHabboId(habboId: Int): List<FriendshipRequest> {
        return friendshipRequests.filter { it.receiverHabboInfo.id == habboId }
    }

    fun addFriendshipRequest(friendshipRequest: FriendshipRequest) {
        friendshipRequests.add(friendshipRequest)

        friendshipRequest.receiverHabboInfo.habbo?.messenger?.addFriendshipRequest(friendshipRequest)

        Database.sequenceOf(MessengerFriendshipRequestsSchema)
            .add(friendshipRequest)
    }

    fun deleteFriendshipRequest(friendshipRequest: FriendshipRequest) {
        friendshipRequests.remove(friendshipRequest)

        friendshipRequest.receiverHabboInfo.habbo?.messenger?.deleteFriendshipRequest(friendshipRequest)

        friendshipRequest.delete()
    }

    fun getOfflineMessagesByHabbo(habbo: Habbo): List<OfflineMessage> {
        return getOfflineMessagesByHabboInfo(habbo.info)
    }

    fun getOfflineMessagesByHabboInfo(habboInfo: HabboInfo): List<OfflineMessage> {
        return getOfflineMessagesByHabboId(habboInfo.id)
    }

    fun getOfflineMessagesByHabboId(habboId: Int): List<OfflineMessage> {
        return offlineMessages.filter { it.receiverHabboInfo.id == habboId }
    }

    fun addOfflineMessage(offlineMessage: OfflineMessage) {
        offlineMessages.add(offlineMessage)

        Database.sequenceOf(MessengerOfflineMessagesSchema)
            .add(offlineMessage)
    }

    fun deleteOfflineMessage(offlineMessage: OfflineMessage) {
        offlineMessages.remove(offlineMessage)

        offlineMessage.delete()
    }

}