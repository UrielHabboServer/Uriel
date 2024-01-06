package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerRelationshipsPacket(
    private val habboInfo: HabboInfo
) : Packet() {

    override val packetId = Outgoing.MessengerRelationships

    constructor(habbo: Habbo) : this(habbo.info)

    override suspend fun construct() {
        val friendships = if (habboInfo.isOnline) {
            habboInfo.habbo!!.messenger.getFriendships()
        } else {
            MessengerManager.getFriendshipsByHabboInfo(habboInfo)
        }

        // filter out friendships that are never present in any friendship
        val relationships = MessengerManager.getRelationships()
            .filter { relationship ->
                friendships.any { friendship ->
                    friendship.relationship?.id == relationship.id
                }
            }

        appendInt(habboInfo.id)
        appendInt(relationships.size)

        for (relationship in relationships) {
            val friendshipsInRelationship = friendships.filter { friendship ->
                friendship.relationship?.id == relationship.id
            }
            val randomFriendship = friendshipsInRelationship.random()

            appendInt(relationship.nitroId)
            appendInt(friendshipsInRelationship.size)
            appendInt(randomFriendship.other(habboInfo).id)
            appendString(randomFriendship.other(habboInfo).username)
            appendString(randomFriendship.other(habboInfo).look)
        }
    }

}