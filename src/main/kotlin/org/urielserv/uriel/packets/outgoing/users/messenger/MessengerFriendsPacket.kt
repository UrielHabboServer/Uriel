package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.messenger.interfaces.Friendship
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerFriendsPacket(
    private val habbo: Habbo,
    private val totalFragments: Int,
    private val fragmentNumber: Int,
    private val friendships: List<Friendship>
) : Packet() {

    override val packetId = Outgoing.MessengerFriends

    override suspend fun construct() {
        appendInt(totalFragments)
        appendInt(fragmentNumber)

        appendInt(friendships.size)
        for (friendship in friendships) {
            val other = friendship.other(habbo.info)

            appendInt(other.id)
            appendString(other.username)
            appendInt(if (other.gender == HabboGender.MALE) 0 else 1)
            appendBoolean(other.isOnline)
            appendBoolean(other.habbo?.room != null)
            appendString(other.look)
            appendInt(0) // Category, unimplemented in Nitro
            appendString(other.motto)
            appendString(other.lastOnline.toString())
            appendString("") // Real name, unimplemented in Nitro
            appendBoolean(false)
            appendBoolean(false)
            appendBoolean(false)
            appendShort(friendship.selfRelationship(habbo.info)?.nitroId?.toShort() ?: 0)
        }
    }

}