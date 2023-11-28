package org.urielserv.uriel.game.habbos

import io.klogging.noCoLogger
import org.ktorm.database.iterator
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.urielserv.uriel.Configuration
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.UsersSchema
import org.urielserv.uriel.networking.UrielServerClient
import java.security.SecureRandom
import java.util.*

/**
 * The UrielHabboManager class is responsible for managing the Habbo users.
 */
@Suppress("unused")
class UrielHabboManager {

    private val logger = noCoLogger("uriel.game.habbos.UrielHabboManager")

    private val habbos = mutableListOf<Habbo>()

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
        habbos.add(habbo)

        if (Configuration.security.refreshSSOTicketOnLogin) {
            Database.update(UsersSchema) {
                set(it.ssoTicket, generateSafeSSOToken())
                where {
                    it.id eq habbo.id
                }
            }
        }

        return habbo
    }

    /**
     * Builds a Habbo object based on the given user ID.
     * Acts as a wrapper for the buildHabbo method that accepts an SSO ticket.
     *
     * @param id The user ID.
     * @return The Habbo object, or null if no user with the given ID is found.
     */
    fun buildHabbo(id: Int): Habbo? {
        val resultIterator = Database.from(UsersSchema)
            .select()
            .where(UsersSchema.id eq id)
            .rowSet.iterator()

        if (!resultIterator.hasNext()) {
            return null
        }

        val result = resultIterator.next()

        return buildHabbo(result[UsersSchema.ssoTicket] ?: "")
    }

    /**
     * Builds a Habbo object based on the provided SSO ticket.
     *
     * @param ssoTicket The SSO ticket used to identify the user.
     * @return A Habbo object if a user with the provided SSO ticket is found in the database, otherwise null.
     */
    fun buildHabbo(ssoTicket: String): Habbo? {
        val resultIterator = Database.from(UsersSchema)
            .select()
            .where(UsersSchema.ssoTicket eq ssoTicket)
            .rowSet.iterator()

        if (!resultIterator.hasNext()) {
            return null
        }

        val result = resultIterator.next()

        try {
            val habboInfo = HabboInfo.Builder(result[UsersSchema.id] ?: 0)
                .username(result[UsersSchema.username] ?: "")
                .motto(result[UsersSchema.motto] ?: "")
                .gender(result[UsersSchema.gender] ?: HabboGender.MALE)
                .look(result[UsersSchema.look] ?: "")
                .homeRoomId(result[UsersSchema.homeRoomId] ?: 0)
                .build()

            val habbo = Habbo(
                id = result[UsersSchema.id] ?: 0,
                info = habboInfo
            )

            habboInfo.habbo = habbo

            return habbo
        } catch (exc: Exception) {
            logger.error("Failed to build Habbo object for SSO ticket $ssoTicket:")
            exc.printStackTrace()
        }

        return null
    }

    /**
     * Removes the specified Habbo from the list of loaded Habbo objects.
     * This method does not disconnect the Habbo (if they are connected).
     *
     * @param habbo The Habbo object to be removed.
     */
    fun unloadHabbo(habbo: Habbo) {
        habbos.remove(habbo)
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