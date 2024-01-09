package org.urielserv.uriel.packets.incoming.users.looks.wardrobe

import io.klogging.logger
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.users.UserSaveLookEvent
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class SaveWardrobeOutfitPacketHandler : PacketHandler {

    private val logger = logger(SaveWardrobeOutfitPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val slotId = packet.getInt()

        if (slotId < 0 || slotId > HotelSettings.habbos.wardrobe.maxSlots) {
            logger.warn("${habbo.info.username} attempted to save a look with an invalid slot ID")
            return
        }

        var look = packet.getString()
        val shortGender = packet.getString()

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