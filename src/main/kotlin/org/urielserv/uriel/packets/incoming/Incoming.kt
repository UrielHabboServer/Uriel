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

    const val DesktopView = 105

    // Navigator
    const val NavigatorInit = 2110
    const val NavigatorSearch = 249

    // Rooms
    const val GetUserFlatCats = 3027
    const val RoomCreate = 2752
    const val RoomEnter = 2312
    const val RoomModel = 2300

    const val FurnitureAliases = 3898

}