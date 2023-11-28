package org.urielserv.uriel.packets.outgoing

@Suppress("unused", "ConstPropertyName")
object OutgoingPacketIDs {

    // Handshake
    const val AuthenticationOK = 2491
    const val Ping = 3928

    // Users
    const val UserHomeRoom = 2875
    const val UserEffects = 340
    const val UserData = 2725
    const val UserSavedLooks = 3315
    const val UpdateUserLook = 2429

    const val NewUserExperienceStatus = 3738
    
}