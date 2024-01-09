package org.urielserv.uriel.packets.incoming

@Suppress("ConstPropertyName")
object Incoming {

    // Handshake
    const val ReleaseVersion = 4000
    const val SecurityTicket = 2419

    const val ClientPong = 2596

    // Users
    const val UserInfo = 357
    const val UserSubscription = 3166
    const val UserCurrency = 273
    const val GetWardrobe = 2742
    const val SaveWardrobeOutfit = 800
    const val UserFigure = 2730
    const val UserProfile = 3265
    const val UserBadges = 2769
    const val UserBadgesCurrentUpdate = 644
    const val UserBadgesCurrent = 2091

    // Landing View
    const val DesktopView = 105
    const val GetPromoArticles = 1827

    // Messenger
    const val MessengerInit = 2781
    const val MessengerChat = 3567
    const val RequestFriend = 3157
    const val AcceptFriend = 137
    const val DeclineFriend = 2890
    const val RemoveFriend = 1689
    const val FriendListUpdate = 1419
    const val GetFriendRequests = 2448
    const val FollowFriend = 3997
    const val SetRelationshipStatus = 3768
    const val MessengerRelationships = 2138
    const val MessengerFindFriends = 1210
    const val SendRoomInvite = 1276

    // Navigator
    const val NavigatorInit = 2110
    const val NavigatorSearch = 249

    // Rooms
    const val GetUserFlatCats = 3027
    const val RoomCreate = 2752
    const val RoomEnter = 2312
    const val RoomModel = 2300
    const val GetGuestRoom = 2230

    const val RoomUnitWalk = 3320
    const val RoomUnitLook = 3301
    const val RoomUnitTyping = 1597
    const val RoomUnitStopTyping = 1474
    const val RoomUnitChat = 1314
    const val RoomUnitChatShout = 2085
    const val RoomUnitChatWhisper = 1543

    const val FurnitureAliases = 3898

}