package org.urielserv.uriel.packets.outgoing

@Suppress("ConstPropertyName")
object Outgoing {

    // Handshake
    const val Authenticated = 2491
    const val ClientPing = 3928
    const val DisconnectReason = 4000

    // Users
    const val UserHomeRoom = 2875
    const val UserEffectList = 340
    const val UserInfo = 2725
    const val UserSubscription = 954
    const val UserPermissions = 411
    const val UserCredits = 3475
    const val UserCurrency = 2018
    const val UserWardrobePage = 3315
    const val UserFigure = 2429
    const val UserProfile = 3898

    const val NoobnessLevel = 3738

    // Messenger
    const val MessengerInit = 1605
    const val MessengerFriends = 3130
    const val MessengerUpdate = 2800
    const val MessengerChat = 1587
    const val MessengerRequest = 2219
    const val MessengerRequests = 280
    const val MessengerRelationships = 2016
    const val MessengerSearch = 973
    const val MessengerInvite = 3870

    // Notifications
    const val GenericAlert = 3801 // TODO: Figure out the difference between this and SimpleAlert
    const val SimpleAlert = 5100
    const val ActivityPointNotification = 2275
    const val NotificationList = 1992
    const val MotdMessages = 2035
    const val InClientLink = 2023
    const val ConnectionError = 1004

    // Landing View
    const val DesktopView = 122
    const val PromoArticles = 286

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
    const val RoomForward = 160

    const val RoomUnit = 374
    const val RoomUnitStatus = 1640
    const val RoomUnitInfo = 3920
    const val RoomUnitRemove = 2661

    const val RoomUnitTyping = 1717
    const val RoomUnitChat = 1446
    const val RoomUnitChatShout = 1036
    const val RoomUnitChatWhisper = 2704

}