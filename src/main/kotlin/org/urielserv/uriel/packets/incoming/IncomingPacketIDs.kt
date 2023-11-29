package org.urielserv.uriel.packets.incoming

@Suppress("unused", "ConstPropertyName")
object IncomingPacketIDs {

    // Handshake
    const val ClientVersion = 4000
    const val SSOTicket = 2419

    const val Pong = 2596

    // Users
    const val RetrieveUserData = 357
    const val RetrieveUserSavedLooks = 2742
    const val UserAddSavedLook = 800
    const val UserUpdateLook = 2730

}