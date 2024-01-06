package org.urielserv.uriel.game.habbos.messenger.interfaces

import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.urielserv.uriel.Database
import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.core.database.schemas.messenger.MessengerOfflineMessagesSchema
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.packets.outgoing.rooms.RoomForwardPacket
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerChatPacket

interface Friendship : Entity<Friendship> {

    companion object : Entity.Factory<Friendship>()

    val id: Int

    var habboInfoOne: HabboInfo
    var habboInfoTwo: HabboInfo

    var relationshipHabboOne: Relationship?
    var relationshipHabboTwo: Relationship?

    var creationTimestamp: Int

    fun other(habboInfo: HabboInfo): HabboInfo {
        return if (habboInfoOne.id == habboInfo.id)
            habboInfoTwo
        else
            habboInfoOne
    }

    fun otherRelationship(habboInfo: HabboInfo): Relationship? {
        return if (habboInfoOne.id == habboInfo.id)
            relationshipHabboTwo
        else
            relationshipHabboOne
    }

    fun selfRelationship(habboInfo: HabboInfo): Relationship? {
        return if (habboInfoOne.id == habboInfo.id)
            relationshipHabboOne
        else
            relationshipHabboTwo
    }

    fun setRelationship(habboInfo: HabboInfo, relationship: Relationship) {
        if (habboInfoOne.id == habboInfo.id) {
            relationshipHabboOne = relationship
        } else {
            relationshipHabboTwo = relationship
        }
    }

    suspend fun sendMessage(sender: Habbo, message: String) {
        val other = other(sender.info)

        if (other.habbo != null) {
            MessengerChatPacket(
                sender = sender,
                message = message
            ).send(other.habbo!!)

            return
        }

        val offlineMessage = OfflineMessage {
            this.senderHabboInfo = sender.info
            this.receiverHabboInfo = other
            this.message = message
            this.timestamp = currentUnixSeconds
        }

        MessengerManager.addOfflineMessage(offlineMessage)
    }

    suspend fun sendInvite(sender: Habbo, message: String) {
        val other = other(sender.info)

        if (other.habbo != null) {
            MessengerChatPacket(
                sender = sender,
                message = message
            ).send(other.habbo!!)

            return
        }
    }

    suspend fun follow(sender: Habbo) {
        val target = other(sender.info)

        if (target.habbo == null) return

        if (target.habbo!!.room == null) return

        val room = target.habbo!!.room!!

        if (sender.room == room) return

        RoomForwardPacket(room).send(sender)
    }

    suspend fun remove() {
        MessengerManager.deleteFriendship(this)
    }

}