package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomSettingsPacket(
    private val room: Room
) : Packet() {

    override val packetId = Outgoing.RoomSettings

    override suspend fun construct() {
        appendInt(room.info.id)
        appendString(room.info.name)
        appendString(room.info.description)
        appendInt(room.info.accessType.ordinal)
        appendInt(room.info.flatCategory.id)
        appendInt(room.info.maximumUsers)
        appendInt(room.info.maximumUsers)

        val tags = room.info.tags.split(";")
        appendInt(tags.size)
        for (tag in tags) {
            appendString(tag)
        }

        appendInt(room.info.tradingMode)
        appendInt(if (room.info.allowOtherPets) 1 else 0)
        appendInt(if (room.info.allowOtherPetsToEat) 1 else 0)
        appendInt(if (room.info.allowWalkthrough) 1 else 0)

        appendInt(if (room.info.areWallsHidden) 1 else 0)
        appendInt(room.info.wallThickness)
        appendInt(room.info.floorThickness)

        appendInt(room.info.chatMode)
        appendInt(room.info.chatWeight)
        appendInt(room.info.chatScrollingSpeed)
        appendInt(room.info.chatHearingDistance)
        appendInt(room.info.chatFloodProtection)

        appendBoolean(false)

        appendInt(room.info.whoCanMute)
        appendInt(room.info.whoCanKick)
        appendInt(room.info.whoCanBan)
        //        this.response.appendInt(this.room.getId());
        //        this.response.appendString(this.room.getName());
        //        this.response.appendString(this.room.getDescription());
        //        this.response.appendInt(this.room.getState().getState());
        //        this.response.appendInt(this.room.getCategory());
        //        this.response.appendInt(this.room.getUsersMax());
        //        this.response.appendInt(this.room.getUsersMax());
        //
        //        if (!this.room.getTags().isEmpty()) {
        //            this.response.appendInt(this.room.getTags().split(";").length);
        //            for (String tag : this.room.getTags().split(";")) {
        //                this.response.appendString(tag);
        //            }
        //        } else {
        //            this.response.appendInt(0);
        //        }
        //
        //        //this.response.appendInt(this.room.getRights().size());
        //        this.response.appendInt(this.room.getTradeMode()); //Trade Mode
        //        this.response.appendInt(this.room.isAllowPets() ? 1 : 0);
        //        this.response.appendInt(this.room.isAllowPetsEat() ? 1 : 0);
        //        this.response.appendInt(this.room.isAllowWalkthrough() ? 1 : 0);
        //        this.response.appendInt(this.room.isHideWall() ? 1 : 0);
        //        this.response.appendInt(this.room.getWallSize());
        //        this.response.appendInt(this.room.getFloorSize());
        //
        //        this.response.appendInt(this.room.getChatMode());
        //        this.response.appendInt(this.room.getChatWeight());
        //        this.response.appendInt(this.room.getChatSpeed());
        //        this.response.appendInt(this.room.getChatDistance());
        //        this.response.appendInt(this.room.getChatProtection());
        //
        //        this.response.appendBoolean(false); //IDK?
        //
        //        this.response.appendInt(this.room.getMuteOption());
        //        this.response.appendInt(this.room.getKickOption());
        //        this.response.appendInt(this.room.getBanOption());

    }

}