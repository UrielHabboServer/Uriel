package org.urielserv.uriel.packets.outgoing

@Suppress("ConstPropertyName")
object Outgoing {

    // Handshake
    const val Authenticated = 2491
    const val ClientPing = 3928

    // Users
    const val UserHomeRoom = 2875
    const val UserEffectList = 340
    const val UserInfo = 2725
    const val UserSubscription = 954
    const val UserCredits = 3475
    const val UserCurrency = 2018
    const val UserWardrobePage = 3315
    const val UserFigure = 2429

    const val NoobnessLevel = 3738

    // Navigator
    const val NavigatorMetadata = 3052

    const val NavigatorEventCategories = 3244
    const val NavigatorSearch = 2690

}