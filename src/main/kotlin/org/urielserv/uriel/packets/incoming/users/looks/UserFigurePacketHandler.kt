package org.urielserv.uriel.packets.incoming.users.looks

import io.klogging.logger
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.users.UserUpdateLookEvent
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitInfoPacket
import org.urielserv.uriel.packets.outgoing.users.looks.UserFigurePacket
import java.nio.ByteBuffer

class UserFigurePacketHandler : PacketHandler {

    private val logger = logger(UserFigurePacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) {
            return
        }

        val shortGender = packet.getString()
        var look = packet.getString()

        val habbo = client.habbo!!

        val gender = try {
            HabboGender.tryFromShort(shortGender)
        } catch (ignored: Exception) {
            logger.warn("${habbo.info.username} attempted to set an invalid gender")
            return
        }

        if (HotelSettings.habbos.wardrobe.validateLooksOnChange) {
            look = ClothingValidator.validateLook(
                look,
                gender.short(),
                habbo.subscriptions.hasActiveHabboClubMembership(),
                listOf()
            )
        }

        val event = UserUpdateLookEvent(habbo, look, gender, habbo.info.look, habbo.info.gender)
        EventDispatcher.dispatch(Events.UserUpdateLook, event)

        if (event.isCancelled) return

        habbo.info.look = event.newLook
        habbo.info.gender = event.newGender
        habbo.info.flushChanges()

        UserFigurePacket(habbo).send(client)

        if (habbo.room != null) {
            RoomUnitInfoPacket(habbo).broadcast(habbo.room!!)
        }

        habbo.messenger.sendUpdateToFriends()
    }

}