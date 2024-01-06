package org.urielserv.uriel.game.habbos.messenger

import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.core.database.schemas.messenger.MessengerFriendshipRequestsSchema
import org.urielserv.uriel.core.database.schemas.messenger.MessengerFriendshipsSchema
import org.urielserv.uriel.core.database.schemas.messenger.MessengerOfflineMessagesSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.habbos.messenger.interfaces.Friendship
import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerChatPacket
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerUpdatePacket

class HabboMessenger(
    val habbo: Habbo
) {

    private val friendships = MessengerManager.getFriendshipsByHabbo(habbo).toMutableList()
    private val friendshipRequests = MessengerManager.getFriendshipRequestsByHabbo(habbo).toMutableList()

    fun getFriendshipByHabboId(habboId: Int): Friendship? {
        return friendships.firstOrNull { it.other(habbo.info).id == habboId }
    }

    fun getFriendships(): List<Friendship> {
        return friendships
    }

    fun isFriend(habboInfo: HabboInfo): Boolean {
        return isFriend(habboInfo.id)
    }

    fun isFriend(habboId: Int): Boolean {
        return friendships.any { it.other(habbo.info).id == habboId }
    }

    suspend fun addFriendship(friendship: Friendship) {
        friendships.add(friendship)

        MessengerUpdatePacket(
            habbo = habbo,
            MessengerUpdatePacket.Action.ADD to friendship
        ).send(habbo)
    }

    suspend fun deleteFriendship(friendship: Friendship) {
        friendships.remove(friendship)

        MessengerUpdatePacket(
            habbo = habbo,
            MessengerUpdatePacket.Action.REMOVE to friendship
        ).send(habbo)
    }

    fun getFriendshipRequestByHabboId(habboId: Int): FriendshipRequest? {
        return friendshipRequests.firstOrNull { it.senderHabboInfo.id == habboId }
    }

    fun getFriendshipRequests(): List<FriendshipRequest> {
        return friendshipRequests
    }

    fun hasFriendshipRequest(habboInfo: HabboInfo): Boolean {
        return hasFriendshipRequest(habboInfo.id)
    }

    fun hasFriendshipRequest(habboId: Int): Boolean {
        return friendshipRequests.any { it.senderHabboInfo.id == habboId }
    }

    fun addFriendshipRequest(friendshipRequest: FriendshipRequest) {
        friendshipRequests.add(friendshipRequest)
    }

    fun deleteFriendshipRequest(friendshipRequest: FriendshipRequest) {
        friendshipRequests.remove(friendshipRequest)
    }

    suspend fun checkForOfflineMessages() {
        val offlineMessages = MessengerManager.getOfflineMessagesByHabbo(habbo)

        for (offlineMessage in offlineMessages) {
            MessengerChatPacket(offlineMessage).send(habbo)

            MessengerManager.deleteOfflineMessage(offlineMessage)
        }
    }

    suspend fun sendUpdateToFriends(action: MessengerUpdatePacket.Action = MessengerUpdatePacket.Action.UPDATE) {
        for (friendship in friendships) {
            val other = friendship.other(habbo.info)

            if (other.habbo != null) {
                MessengerUpdatePacket(
                    habbo = other.habbo!!,
                    action to friendship
                ).send(other.habbo!!)
            }
        }
    }

    suspend fun unload() {
        sendUpdateToFriends(MessengerUpdatePacket.Action.UPDATE)
    }

}