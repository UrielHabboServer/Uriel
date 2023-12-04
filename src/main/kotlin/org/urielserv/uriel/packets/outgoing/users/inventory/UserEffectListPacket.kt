package org.urielserv.uriel.packets.outgoing.users.inventory

import org.urielserv.uriel.game.habbos.inventory.effects.Effect
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class UserEffectListPacket(
    private val effects: List<Effect>
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserEffectList

    override suspend fun construct() {
        appendInt(effects.size)
        for (effect in effects) {
            appendInt(effect.effectId)
            appendInt(0) // subType; 0 = hand, 1 = full
            appendInt(effect.duration)
            appendInt(if (effect.isActive) effect.quantity - 1 else effect.quantity) // inactiveEffectsInInventory
            appendInt(if (effect.isActive) effect.timeLeft else -1) // secondsLeftIfActive
            appendBoolean(effect.duration <= 0) // isPermanent
        }
    }

}