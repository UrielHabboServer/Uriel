package org.urielserv.uriel.configuration

import kotlinx.serialization.Serializable

/**
 * Data class representing the Habbo Hotel settings for the Uriel server.
 *
 * @property hotel The Habbo hotel settings.
 * @property habbos The Habbo user settings.
 */
@Serializable
data class UrielHotelSettings(
    val hotel: Hotel,
    val habboClub: HabboClub,
    val habbos: Habbos
) {

    @Serializable
    data class Hotel(
        val defaultRoomId: Int
    )

    @Serializable
    data class HabboClub(
        val discountEnabled: Boolean,
        val discountDaysBeforeEnd: Int
    )

    @Serializable
    data class Habbos(
        val wardrobe: Wardrobe,
        val inventory: Inventory,
        val subscription: Subscription
    ) {

        @Serializable
        data class Wardrobe(
            val figureDataUrl: String,
            val validateLooksOnHabboClubExpire: Boolean,
            val validateLooksOnLogin: Boolean,
            val validateLooksOnChange: Boolean
            //val validateLooksOnFootballGate: Boolean
        )

        @Serializable
        data class Inventory(
            val storeFurniDataOnPickUp: Boolean
        )

        @Serializable
        data class Subscription(
            val deleteExpiredSubscriptions: Boolean
        )

    }

}