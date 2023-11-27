package org.urielserv.uriel.packets.outgoing

import org.urielserv.uriel.packets.outgoing.handshake.AuthenticationOKPacket

@Suppress("unused", "ConstPropertyName")
object OutgoingPacketIDs {

    // Handshake
    const val AuthenticationOK = 2491
    const val Ping = 3928

    // Users
    const val UserHomeRoom = 2875
    const val UserEffects = 340
    const val NewUserExperienceStatus = 3738
    
}