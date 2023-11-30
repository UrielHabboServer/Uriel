package org.urielserv.uriel.packets.incoming.users.looks.wardrobe

import io.klogging.logger
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class SaveWardrobeOutfitPacketHandler : PacketHandler {

    private val logger = logger(SaveWardrobeOutfitPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        val slotId = packet.readInt()

        if (slotId < 0 || slotId > 9) {
            logger.warn("${client.habbo!!.info.username} attempted to save a look with an invalid slot ID")
            return
        }

        var look = packet.readString()
        val shortGender = packet.readString()

        val gender = try {
            HabboGender.tryFromShort(shortGender)
        } catch (exc: Exception) {
            logger.warn("${client.habbo!!.info.username} attempted to save a look with an invalid gender")
            return
        }

        if (HotelSettings.habbos.wardrobe.validateLooksOnSave) {
            look = ClothingValidator.validateLook(
                look,
                gender.short(),
                client.habbo!!.subscriptions.hasActiveHabboClubMembership(),
                listOf()
            )
        }

        client.habbo!!.inventory.savedLooks.setLook(
            slotId,
            look,
            gender
        )
    }

}