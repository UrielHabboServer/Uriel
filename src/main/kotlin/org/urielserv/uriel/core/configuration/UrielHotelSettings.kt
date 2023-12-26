package org.urielserv.uriel.core.configuration

import kotlinx.serialization.Serializable

/**
 * Data class representing the Habbo Hotel settings for the Uriel server.
 *
 * @property hotel The Habbo hotel settings.
 * @property habbos The Habbo user settings.
 */
@Serializable
data class UrielHotelSettings(
    var hotel: Hotel,
    var habboClub: HabboClub,
    var habbos: Habbos,
    var rooms: Rooms,
    var commands: Commands
) {

    @Serializable
    data class Hotel(
        var defaultRoomId: Int
    )

    @Serializable
    data class HabboClub(
        var discountEnabled: Boolean,
        var discountDaysBeforeEnd: Int
    )

    @Serializable
    data class Habbos(
        var alertChatBubbleId: Int,

        var wardrobe: Wardrobe,
        var inventory: Inventory,
        var subscription: Subscription,
        var rooms: Rooms
    ) {

        @Serializable
        data class Rooms(
            var maxRoomsWithHabboClub: Int,
            var maxRooms: Int
        )

        @Serializable
        data class Wardrobe(
            var figureDataUrl: String,
            var validateLooksOnHabboClubExpire: Boolean,
            var validateLooksOnLogin: Boolean,
            var validateLooksOnChange: Boolean,
            var validateLooksOnSave: Boolean
            //var validateLooksOnFootballGate: Boolean
        )

        @Serializable
        data class Inventory(
            var storeFurniDataOnPickUp: Boolean
        )

        @Serializable
        data class Subscription(
            var deleteExpiredSubscriptions: Boolean
        )

    }

    @Serializable
    data class Rooms(
        var noDelayOnWalk: Boolean,
    )

    @Serializable
    data class Commands(
        var prefix: String
    )

}