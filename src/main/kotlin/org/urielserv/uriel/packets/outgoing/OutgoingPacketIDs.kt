package org.urielserv.uriel.packets.outgoing

@Suppress("unused", "ConstPropertyName")
object OutgoingPacketIDs {

    // Handshake
    const val Authenticated = 2491
    const val ClientPing = 3928

    // Users
    const val UserHomeRoom = 2875
    const val UserEffectList = 340
    const val UserInfo = 2725
    const val UserSubscription = 954
    const val UserWardrobePage = 3315
    const val UserFigure = 2429

    const val NoobnessLevel = 3738

    // Navigator
    const val NavigatorSettings = 518
    const val NavigatorMetadata = 3052
    const val NavigatorLifted = 3104
    const val NavigatorCollapsed = 1543
    const val NavigatorSearches = 3984
    const val NavigatorEventCategories = 3244

}