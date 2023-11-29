package org.urielserv.uriel.packets.incoming.users.looks

import io.klogging.logger
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.looks.UpdateUserLookPacket
import java.io.ByteArrayInputStream

class UserUpdateLookPacketHandler : PacketHandler {

    private val logger = logger(UserUpdateLookPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) {
            return
        }

        val genderString = packet.readString()
        var look = packet.readString()

        val gender = try {
            HabboGender.tryFromShort(genderString)
        } catch (ignored: Exception) {
            logger.warn("${client.habbo!!.info.username} attempted to set an invalid gender")
            return
        }

        if (HotelSettings.habbos.wardrobe.validateLooksOnChange) {
            look = ClothingValidator.validateLook(
                look,
                gender.short(),
                client.habbo!!.subscriptions.hasActiveHabboClubMembership(),
                listOf()
            )
        }

        client.habbo!!.info.look = look
        client.habbo!!.info.gender = gender
        client.habbo!!.info.flushChanges()

        UpdateUserLookPacket(client.habbo!!).send(client)
    }

}