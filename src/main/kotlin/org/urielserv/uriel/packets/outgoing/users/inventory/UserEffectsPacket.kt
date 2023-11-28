package org.urielserv.uriel.packets.outgoing.users.inventory

import org.urielserv.uriel.game.habbos.inventory.effects.Effect
import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class UserEffectsPacket(
    private val effects: List<Effect>
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserEffects

    override suspend fun construct() {
        appendInt(effects.size)

        for (effect in effects) {
            appendInt(effect.effectId)
            appendInt(0) // type; 0 = hand, 1 = full
            appendInt(effect.duration)
            appendInt(if (effect.isActive) effect.quantity - 1 else effect.quantity)
            appendInt(if (effect.isActive) effect.timeLeft else -1)
            appendBoolean(effect.duration <= 0)
        }
    }

}