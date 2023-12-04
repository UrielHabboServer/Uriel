package org.urielserv.uriel.packets.incoming

@Suppress("unused", "ConstPropertyName")
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

    // Navigator
    const val NavigatorInit = 2110
    const val NavigatorSearch = 249

}