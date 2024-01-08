package org.urielserv.uriel.game.habbos.messenger

import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.habbos.messenger.interfaces.Friendship
import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerChatPacket
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerUpdatePacket

class HabboMessenger(
    val habbo: Habbo
) {

    private val _friendships = MessengerManager.getFriendships(habbo).toMutableList()
    val friendships: List<Friendship>
        get() = _friendships.toList()

    private val _friendshipRequests = MessengerManager.getFriendshipRequests(habbo).toMutableList()
    val friendshipRequests: List<FriendshipRequest>
        get() = _friendshipRequests.toList()

    init {
        for (friendship in _friendships) {
            // Update habboInfo to habbo.info
            if (friendship.habboInfoOne.id == habbo.info.id) {
                friendship.habboInfoOne = habbo.info
            } else {
                friendship.habboInfoTwo = habbo.info
            }
        }
    }

    fun getFriendship(habbo: Habbo): Friendship? {
        return getFriendship(habbo.info)
    }

    fun getFriendship(habboInfo: HabboInfo): Friendship? {
        return getFriendship(habboInfo.id)
    }

    fun getFriendship(habboId: Int): Friendship? {
        return _friendships.firstOrNull { it.other(habbo.info).id == habboId }
    }

    fun isFriend(habboInfo: HabboInfo): Boolean {
        return isFriend(habboInfo.id)
    }

    fun isFriend(habboId: Int): Boolean {
        return _friendships.any { it.other(habbo.info).id == habboId }
    }

    suspend fun addFriendship(friendship: Friendship) {
        _friendships.add(friendship)

        MessengerUpdatePacket(
            habbo = habbo,
            MessengerUpdatePacket.Action.ADD to friendship
        ).send(habbo)
    }

    suspend fun deleteFriendship(friendship: Friendship) {
        _friendships.remove(friendship)

        MessengerUpdatePacket(
            habbo = habbo,
            MessengerUpdatePacket.Action.REMOVE to friendship
        ).send(habbo)
    }

    fun getFriendshipRequest(habbo: Habbo): FriendshipRequest? {
        return getFriendshipRequest(habbo.info)
    }

    fun getFriendshipRequest(habboInfo: HabboInfo): FriendshipRequest? {
        return getFriendshipRequest(habboInfo.id)
    }

    fun getFriendshipRequest(habboId: Int): FriendshipRequest? {
        return _friendshipRequests.firstOrNull { it.senderHabboInfo.id == habboId }
    }

    fun hasFriendshipRequest(habboInfo: HabboInfo): Boolean {
        return hasFriendshipRequest(habboInfo.id)
    }

    fun hasFriendshipRequest(habboId: Int): Boolean {
        return _friendshipRequests.any { it.senderHabboInfo.id == habboId }
    }

    fun addFriendshipRequest(friendshipRequest: FriendshipRequest) {
        _friendshipRequests.add(friendshipRequest)
    }

    fun deleteFriendshipRequest(friendshipRequest: FriendshipRequest) {
        _friendshipRequests.remove(friendshipRequest)
    }

    suspend fun checkForOfflineMessages() {
        val offlineMessages = MessengerManager.getOfflineMessages(habbo)

        for (offlineMessage in offlineMessages) {
            MessengerChatPacket(offlineMessage).send(habbo)

            MessengerManager.deleteOfflineMessage(offlineMessage)
        }
    }

    suspend fun sendUpdateToFriends() {
        for (friendship in _friendships) {
            val other = friendship.other(habbo.info)

            if (other.habbo != null) {
                MessengerUpdatePacket(
                    habbo = other.habbo!!,
                    MessengerUpdatePacket.Action.UPDATE to friendship
                ).send(other.habbo!!)
            }
        }
    }

    suspend fun unload() {
        sendUpdateToFriends()
    }

}