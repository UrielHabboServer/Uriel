package org.urielserv.uriel.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.enum
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.database.enums.Bool

@Suppress("unused")
object UsersSchema : Table<Nothing>("users") {

    val id = int("id").primaryKey()

    val username = varchar("username")
    val password = varchar("password")

    val ssoTicket = varchar("sso_ticket")

    val email = varchar("email")
    val isEmailVerified = enum<Bool>("is_email_verified")

    val rankId = int("rank")

    val accountCreated = int("account_creation_timestamp")

    val lastLogin = int("last_login_timestamp")
    val lastOnline = int("last_online_timestamp")
    val isOnline = enum<Bool>("is_online")

    val motto = varchar("motto")
    val look = varchar("look")
    val gender = enum<Gender>("gender")

    val credits = int("credits")
    val pixels = int("pixels")
    val points = int("points")

    val registrationIp = varchar("registration_ip")
    val currentIp = varchar("current_ip")

    val machineId = varchar("machine_id")

    val homeRoomId = int("home_room_id")

    enum class Gender {
        MALE,
        FEMALE;

        fun short(): String = this.name.substring(0, 1)
    }

}