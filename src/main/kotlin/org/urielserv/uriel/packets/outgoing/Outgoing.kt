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

    const val DesktopView = 122

    // Navigator
    const val NavigatorCategories = 1562
    const val NavigatorMetadata = 3052
    const val NavigatorEventCategories = 3244
    const val NavigatorSearch = 2690

    // Rooms
    const val CanCreateRoom = 378
    const val RoomCreated = 1304
    const val RoomOpen = 758
    const val RoomModelName = 2031
    const val RoomPaint = 2454
    const val RoomInfoOwner = 749
    const val RoomThickness = 3547
    const val RoomInfo = 687
    const val RoomHeightmap = 2753
    const val RoomModel = 1301

    // Notifications
    const val GenericAlert = 3801
    const val SimpleAlert = 5100
    const val ActivityPointNotification = 2275
}