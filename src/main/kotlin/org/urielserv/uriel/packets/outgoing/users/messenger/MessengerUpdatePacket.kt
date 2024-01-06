package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.messenger.interfaces.Friendship
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerUpdatePacket(
    private val habbo: Habbo,
    private val friendshipUpdates: Map<Action, Friendship>
) : Packet() {

    override val packetId = Outgoing.MessengerUpdate

    constructor(habbo: Habbo, vararg updates: Pair<Action, Friendship>) : this(habbo, mapOf(*updates))

    override suspend fun construct() {
        appendInt(0) // Categories size, unimplemented in Nitro

        appendInt(friendshipUpdates.size)
        for ((action, friendship) in friendshipUpdates) {
            val other = friendship.other(habbo.info)

            appendInt(action.id)
            appendInt(other.id)

            if (action == Action.REMOVE) continue

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
            appendShort(friendship.otherRelationship(habbo.info)?.nitroId?.toShort() ?: 0)
        }
    }

    enum class Action(val id: Int) {
        ADD(1),
        UPDATE(0),
        REMOVE(-1)
    }

}