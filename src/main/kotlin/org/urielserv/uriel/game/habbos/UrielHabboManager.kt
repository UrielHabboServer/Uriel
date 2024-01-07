package org.urielserv.uriel.game.habbos

import io.klogging.noCoLogger
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList
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
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * The UrielHabboManager class is responsible for managing the Habbo users.
 */
@Suppress("unused")
class UrielHabboManager {

    private val logger = noCoLogger(UrielHabboManager::class)

    private val connectedHabbos = ConcurrentHashMap<Int, Habbo>()

    /**
     * Logs in a Habbo using the provided SSO ticket. If successful, a Habbo object representing the logged-in user is returned.
     *
     * @param ssoTicket The SSO ticket for the user to be logged in.
     * @param client The UrielServerClient object to be associated with the logged-in Habbo.
     * @return The logged-in Habbo object if successful, null otherwise.
     */
    suspend fun tryLoginHabbo(ssoTicket: String, client: UrielServerClient) {
        val habboInfo = Database.sequenceOf(UsersSchema)
            .find {
                it.ssoTicket eq ssoTicket
            }

        if (habboInfo == null) {
            logger.info("Failed to log in Habbo with SSO ticket $ssoTicket: No Habbo with that SSO ticket found in the database")
            return
        }

        val oldHabbo = connectedHabbos[habboInfo.id]

        if (oldHabbo != null) {
            oldHabbo.notifications.alert(
                message = text("uriel.error.login_elsewhere"),
                title = text("uriel.error.connection")
            )

            oldHabbo.disconnect()
        }

        habboInfo.isOnline = true
        habboInfo.lastLogin = currentUnixSeconds

        habboInfo.currentIp = client.ip

        val habbo = Habbo(habboInfo, client)

        client.habbo = habbo
        connectedHabbos[habboInfo.id] = habbo

        if (HotelSettings.habbos.wardrobe.validateLooksOnLogin) {
            // Make sure the player's look is valid
            val validatedLook = ClothingValidator.validateLook(habbo)
            habboInfo.look = validatedLook
        }

        if (Configuration.security.refreshSSOTicketOnLogin) {
            habboInfo.ssoTicket = generateSafeSSOToken()
        }

        habboInfo.flushChanges()

        val event = UserLoginEvent(habbo, ssoTicket)
        EventDispatcher.dispatch(Events.UserLogin, event)

        if (event.isCancelled) {
            habbo.disconnect()
            return
        }

        logger.info("${habboInfo.username} logged in from ${client.ip}:${client.port}")

        AuthenticatedPacket().send(client)
        UserHomeRoomPacket(
            homeRoomId = if (habboInfo.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habboInfo.homeRoomId,
            roomIdToEnter = if (habboInfo.homeRoomId == 0) HotelSettings.hotel.defaultRoomId else habboInfo.homeRoomId
        ).send(client)

        NoobnessLevelPacket(NoobnessLevelPacket.NEW_IDENTITY).send(client)
        UserPermissionsPacket(habbo).send(client)

        ClientPingPacket().send(client)
    }

    /**
     * Gets a Habbo object by their ID.
     *
     * @param id The ID of the Habbo to be found.
     * @return The Habbo object if found, otherwise null.
     */
    fun getConnectedHabbo(id: Int): Habbo? {
        return connectedHabbos[id]
    }

    /**
     * Gets a Habbo object by their username.
     *
     * @param username The username of the Habbo to be found.
     * @return The Habbo object if found, otherwise null.
     */
    fun getConnectedHabbo(username: String): Habbo? {
        return connectedHabbos.values.firstOrNull { it.info.username == username }
    }

    fun getHabboInfo(id: Int): HabboInfo? {
        return Database.sequenceOf(UsersSchema)
            .find {
                it.id eq id
            }
    }

    fun getHabboInfo(username: String): HabboInfo? {
        return Database.sequenceOf(UsersSchema)
            .find {
                it.username eq username
            }
    }

    fun searchHabboInfos(username: String, limit: Int = 50): List<HabboInfo> {
        return Database.sequenceOf(UsersSchema)
            .filter {
                it.username like "%$username%"
            }.toList().take(limit)
    }

    /**
     * Removes the specified Habbo from the list of loaded Habbo objects.
     * This method does not disconnect the Habbo (if they are connected).
     *
     * @param habbo The Habbo object to be removed.
     */
    fun unloadHabbo(habbo: Habbo) {
        connectedHabbos.remove(habbo.info.id)

        logger.info("${habbo.info.username} (${habbo.client.ip}:${habbo.client.port}) disconnected")
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