package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomInfoPacket(
    private val room: Room,
    private val roomForward: Boolean,
    private val roomEnter: Boolean
) : Packet() {

    override val packetId = Outgoing.RoomInfo

    override suspend fun construct() {
        appendBoolean(roomEnter)

        appendInt(room.info.id)
        appendString(room.info.name)

        if (room.info.isPublic) {
            appendInt(0)
            appendString("")
        } else {
            appendInt(room.info.ownerHabboInfo.id)
            appendString(room.info.ownerHabboInfo.username)
        }

        appendInt(room.info.accessType.ordinal)

        appendInt(room.info.users)
        appendInt(room.info.maximumUsers)

        appendString(room.info.description)

        appendInt(room.info.tradingMode)
        appendInt(room.info.score)
        appendInt(2)

        appendInt(room.info.flatCategory.id)

        val tags = room.info.tags.split(";")
        appendInt(tags.size)
        for (tag in tags) {
            appendString(tag)
        }

        var base = 0

        /*
        TODO: Add Guild to base
        if (room.info.guildId > 0) {
            base = base or 2
        }
         */

        if (!room.info.isPublic) {
            base = base or 8
        }

        if (room.info.isPromoted) {
            base = base or 4
        }

        if (room.info.allowOtherPets) {
            base = base or 16
        }

        appendInt(base)

        // TODO: Add Guild to packet

        // TODO: Add Promotion Info to packet

        appendBoolean(roomForward)

        appendBoolean(room.info.isStaffPicked)
        appendBoolean(false) // TODO: Is Habbo Guild Member?
        appendBoolean(false) // TODO: Is Room muted?

        /*
                this.response.appendInt(this.room.getMuteOption());
        this.response.appendInt(this.room.getKickOption());
        this.response.appendInt(this.room.getBanOption());

        this.response.appendBoolean(this.room.hasRights(this.habbo)); //mute all button

        this.response.appendInt(this.room.getChatMode());
        this.response.appendInt(this.room.getChatWeight());
        this.response.appendInt(this.room.getChatSpeed());
        this.response.appendInt(this.room.getChatDistance());
        this.response.appendInt(this.room.getChatProtection());
         */

        appendInt(room.info.whoCanMute)
        appendInt(room.info.whoCanKick)
        appendInt(room.info.whoCanBan)

        appendBoolean(false) // TODO: Room Rights

        appendInt(room.info.chatMode)
        appendInt(room.info.chatWeight)
        appendInt(room.info.chatScrollingSpeed)
        appendInt(room.info.chatHearingDistance)
        appendInt(room.info.chatFloodProtection)
    }

}