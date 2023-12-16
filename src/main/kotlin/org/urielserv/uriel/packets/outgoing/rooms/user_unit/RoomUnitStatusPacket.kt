package org.urielserv.uriel.packets.outgoing.rooms.user_unit

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitStatusPacket(
    private val habbos: List<Habbo>
) : Packet() {

    override val packetId = Outgoing.RoomUnitStatus

    constructor(vararg habbos: Habbo) : this(habbos.toList())

    override suspend fun construct() {
        appendInt(habbos.size)

        for (habbo in habbos) {
            /*
                this.response.appendInt(habbo.getRoomUnit().getId());
                this.response.appendInt(habbo.getRoomUnit().getPreviousLocation().x);
                this.response.appendInt(habbo.getRoomUnit().getPreviousLocation().y);
                this.response.appendString(habbo.getRoomUnit().getPreviousLocationZ() + "");


                this.response.appendInt(habbo.getRoomUnit().getHeadRotation().getValue());
                this.response.appendInt(habbo.getRoomUnit().getBodyRotation().getValue());

                StringBuilder status = new StringBuilder("/");

                for (Map.Entry<RoomUnitStatus, String> entry : habbo.getRoomUnit().getStatusMap().entrySet()) {
                status.append(entry.getKey()).append(" ").append(entry.getValue()).append("/");
                }
                this.response.appendString(status.toString());
                habbo.getRoomUnit().setPreviousLocation(habbo.getRoomUnit().getCurrentLocation());
             */
            appendInt(habbo.roomUnit!!.id)

            appendInt(habbo.roomUnit!!.previousTile.x)
            appendInt(habbo.roomUnit!!.previousTile.y)
            appendString(habbo.roomUnit!!.previousTile.height.toString())

            appendInt(habbo.roomUnit!!.headRotation.ordinal)
            appendInt(habbo.roomUnit!!.bodyRotation.ordinal)

            val status = buildString {
                append("/")

                for ((key, value) in habbo.roomUnit!!.statuses) {
                    append("${key.key} $value/")
                }
            }
            appendString(status)

            habbo.roomUnit!!.previousTile = habbo.roomUnit!!.currentTile
        }
    }

}