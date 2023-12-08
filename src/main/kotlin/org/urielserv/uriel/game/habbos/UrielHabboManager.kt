package org.urielserv.uriel.game.habbos

import io.klogging.noCoLogger
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.urielserv.uriel.Configuration
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.UsersSchema
import org.urielserv.uriel.networking.UrielServerClient
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
    fun loginHabbo(ssoTicket: String, client: UrielServerClient): Habbo? {
        val habbo = buildHabbo(ssoTicket) ?: return null

        habbo.client = client
        connectedHabbos[habbo.info.id] = habbo

        if (Configuration.security.refreshSSOTicketOnLogin) {
            Database.update(UsersSchema) {
                set(it.ssoTicket, generateSafeSSOToken())
                where {
                    it.id eq habbo.info.id
                }
            }
        }

        return habbo
    }

    /**
     * Builds a Habbo object based on the provided SSO ticket.
     *
     * @param ssoTicket The SSO ticket used to identify the user.
     * @return A Habbo object if a user with the provided SSO ticket is found in the database, otherwise null.
     */
    private fun buildHabbo(ssoTicket: String): Habbo? {
        try {
            val habboInfo = Database.sequenceOf(UsersSchema)
                .find {
                    it.ssoTicket eq ssoTicket
                } ?: return null
            
            return Habbo(habboInfo)
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

    /**
     * Removes the specified Habbo from the list of loaded Habbo objects.
     * This method does not disconnect the Habbo (if they are connected).
     *
     * @param habbo The Habbo object to be removed.
     */
    fun unloadHabbo(habbo: Habbo) {
        connectedHabbos.remove(habbo.info.id)
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