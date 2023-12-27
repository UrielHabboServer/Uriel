package org.urielserv.uriel.game.habbos

import io.klogging.noCoLogger
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.urielserv.uriel.Configuration
import org.urielserv.uriel.Database
import org.urielserv.uriel.EventDispatcher
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.core.database.schemas.users.UsersSchema
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.events.users.UserLoginEvent
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.extensions.text
import org.urielserv.uriel.game.wardrobe.ClothingValidator
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.outgoing.handshake.AuthenticatedPacket
import org.urielserv.uriel.packets.outgoing.handshake.ClientPingPacket
import org.urielserv.uriel.packets.outgoing.users.NoobnessLevelPacket
import org.urielserv.uriel.packets.outgoing.users.UserHomeRoomPacket
import org.urielserv.uriel.packets.outgoing.users.UserPermissionsPacket
import org.urielserv.uriel.packets.outgoing.users.inventory.UserEffectListPacket
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket
import java.security.SecureRandom
import java.util.*

/**
 * The UrielHabboManager class is responsible for managing the Habbo users.
 */
@Suppress("unused")
class UrielHabboManager {

    private val logger = noCoLogger(UrielHabboManager::class)

    private val connectedHabbos = mutableMapOf<Int, Habbo>()

    /**
     * Logs in a Habbo using the provided SSO ticket. If successful, a Habbo object representing the logged-in user is returned.
     *
     * @param ssoTicket The SSO ticket for the user to be logged in.
     * @param client The UrielServerClient object to be associated with the logged-in Habbo.
     * @return The logged-in Habbo object if successful, null otherwise.
     */
    suspend fun tryLoginHabbo(ssoTicket: String, client: UrielServerClient) {
        val habbo = buildHabbo(ssoTicket, client) ?: return

        val oldHabbo = connectedHabbos[habbo.info.id]

        if (oldHabbo != null) {
            oldHabbo.notifications.sendAlert(
                text("uriel.error.login_elsewhere"),
                text("uriel.error.connection")
            )

            oldHabbo.disconnect()
        }

        client.habbo = habbo
        connectedHabbos[habbo.info.id] = habbo

        habbo.info.isOnline = true
        habbo.info.lastLogin = currentUnixSeconds

        habbo.info.currentIp = client.ip

        if (HotelSettings.habbos.wardrobe.validateLooksOnLogin) {
            // Make sure the player's look is valid
            val validatedLook = ClothingValidator.validateLook(habbo)
            habbo.info.look = validatedLook
        }

        if (Configuration.security.refreshSSOTicketOnLogin) {
            habbo.info.ssoTicket = generateSafeSSOToken()
        }

        habbo.info.flushChanges()

        val event = UserLoginEvent(habbo, ssoTicket)
        EventDispatcher.dispatch(Events.UserLogin, event)

        if (event.isCancelled) {
            habbo.disconnect()
            return
        }

        AuthenticatedPacket().send(client)
        UserHomeRoomPacket(
            homeRoomId = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId,
            roomIdToEnter = if (habbo.info.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habbo.info.homeRoomId
        ).send(client)

        NoobnessLevelPacket(NoobnessLevelPacket.NEW_IDENTITY).send(client)
        UserPermissionsPacket(habbo).send(client)

        ClientPingPacket().send(client)
    }

    /**
     * Builds a Habbo object based on the provided SSO ticket.
     *
     * @param ssoTicket The SSO ticket used to identify the user.
     * @param client The UrielServerClient object to be associated with the logged-in Habbo.
     * @return A Habbo object if a user with the provided SSO ticket is found in the database, otherwise null.
     */
    private fun buildHabbo(ssoTicket: String, client: UrielServerClient): Habbo? {
        try {
            val habboInfo = Database.sequenceOf(UsersSchema)
                .find {
                    it.ssoTicket eq ssoTicket
                } ?: return null

            return Habbo(habboInfo, client)
        } catch (exc: Exception) {
            logger.error("Failed to build Habbo object for SSO ticket $ssoTicket:")
            exc.printStackTrace()
        }

        return null
    }

    /**
     * Gets a Habbo object by their ID.
     *
     * @param id The ID of the Habbo to be found.
     * @return The Habbo object if found, otherwise null.
     */
    fun getConnectedHabboById(id: Int): Habbo? {
        return connectedHabbos[id]
    }

    /**
     * Gets a Habbo object by their username.
     *
     * @param username The username of the Habbo to be found.
     * @return The Habbo object if found, otherwise null.
     */
    fun getConnectedHabboByUsername(username: String): Habbo? {
        return connectedHabbos.values.firstOrNull { it.info.username == username }
    }

    fun getHabboInfoById(id: Int): HabboInfo? {
        return Database.sequenceOf(UsersSchema)
            .find {
                it.id eq id
            }
    }

    fun getHabboInfoByUsername(username: String): HabboInfo? {
        return Database.sequenceOf(UsersSchema)
            .find {
                it.username eq username
            }
    }

    /**
     * Removes the specified Habbo from the list of loaded Habbo objects.
     * This method does not disconnect the Habbo (if they are connected).
     *
     * @param habbo The Habbo object to be removed.
     */
    fun unloadHabbo(habbo: Habbo) {
        connectedHabbos.remove(habbo.info.id)
    }

    fun getHabbos(): List<Habbo> {
        return connectedHabbos.values.toList()
    }

    internal suspend fun shutdown() {
        for (habbo in connectedHabbos.values) {
            habbo.disconnect()
        }
    }

    companion object {

        /**
         * Generates a safe Single Sign-On (SSO) token.
         *
         * Uses a secure random number generator to generate a byte array of length 48.
         * Then, encodes the byte array using Base64 encoding without padding to obtain the SSO token.
         *
         * @return The generated SSO token as a String.
         */
        private fun generateSafeSSOToken(): String {
            val random = SecureRandom()
            val bytes = ByteArray(48)

            random.nextBytes(bytes)

            val encoder: Base64.Encoder? = Base64.getUrlEncoder().withoutPadding()

            return if (encoder != null) {
                encoder.encodeToString(bytes)
            } else {
                ""
            }
        }

    }

}