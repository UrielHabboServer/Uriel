package org.urielserv.uriel.game.habbos.messenger.interfaces

import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.urielserv.uriel.Database
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.core.database.schemas.messenger.MessengerFriendshipsSchema
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.habbos.notifications.complex.BubbleNotification

interface FriendshipRequest : Entity<FriendshipRequest> {

    companion object : Entity.Factory<FriendshipRequest>()

    val id: Int

    var senderHabboInfo: HabboInfo
    var receiverHabboInfo: HabboInfo

    suspend fun accept(ignoreLimit: Boolean = false) {
        if (receiverHabboInfo.habbo != null && !ignoreLimit) {
            val receiverHabbo = receiverHabboInfo.habbo!!

            val friendLimit = if (receiverHabbo.subscriptions.hasActiveHabboClubMembership()) {
                HotelSettings.habbos.messenger.maxFriendsWithHabboClub
            } else {
                HotelSettings.habbos.messenger.maxFriends
            }

            if (!receiverHabbo.hasPermission("uriel.messenger.unlimited_friends")
                && receiverHabbo.messenger.getFriendships().size >= friendLimit) return
        }

        val friendship = Friendship {
            habboInfoOne = senderHabboInfo
            habboInfoTwo = receiverHabboInfo

            relationshipHabboOne = null
            relationshipHabboTwo = null

            creationTimestamp = currentUnixSeconds
        }

        MessengerManager.addFriendship(friendship)
        MessengerManager.deleteFriendshipRequest(this)
    }

    suspend fun decline() {
        MessengerManager.deleteFriendshipRequest(this)
    }

}