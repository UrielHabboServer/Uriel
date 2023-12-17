package org.urielserv.uriel.packets.incoming.users.looks.wardrobe

import io.klogging.logger
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.users.UserSaveLookEvent
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class SaveWardrobeOutfitPacketHandler : PacketHandler {

    private val logger = logger(SaveWardrobeOutfitPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        val slotId = packet.readInt()

        val habbo = client.habbo!!

        if (slotId < 0 || slotId > 9) {
            logger.warn("${habbo.info.username} attempted to save a look with an invalid slot ID")
            return
        }

        var look = packet.readString()
        val shortGender = packet.readString()

        val gender = try {
            HabboGender.tryFromShort(shortGender)
        } catch (exc: Exception) {
            logger.warn("${habbo.info.username} attempted to save a look with an invalid gender")
            return
        }

        if (HotelSettings.habbos.wardrobe.validateLooksOnSave) {
            look = ClothingValidator.validateLook(
                look,
                gender.short(),
                habbo.subscriptions.hasActiveHabboClubMembership(),
                listOf()
            )
        }

        val event = UserSaveLookEvent(habbo, slotId, look, gender)
        EventDispatcher.dispatch(Events.UserSaveLook, event)

        if (event.isCancelled) return

        habbo.inventory.savedLooks.setLook(
            event.slotId,
            event.look,
            event.gender
        )
    }

}