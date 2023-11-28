package org.urielserv.uriel.database.schemas

import org.ktorm.schema.*
import org.urielserv.uriel.game.habbos.HabboGender

@Suppress("unused")
object UsersSchema : Table<Nothing>("users") {

    val id = int("id").primaryKey()

    val username = varchar("username")
    val password = text("password")

    val ssoTicket = varchar("sso_ticket")

    val email = varchar("email")
    val isEmailVerified = boolean("is_email_verified")

    val rankId = int("rank")

    val accountCreated = timestamp("account_creation_timestamp")

    val lastLogin = timestamp("last_login_timestamp")
    val lastOnline = timestamp("last_online_timestamp")
    val isOnline = boolean("is_online")

    val motto = varchar("motto")
    val look = text("look")
    val gender = enum<HabboGender>("gender")

    val credits = int("credits")
    val pixels = int("pixels")
    val points = int("points")

    val registrationIp = varchar("registration_ip")
    val currentIp = varchar("current_ip")

    val machineId = varchar("machine_id")

    val homeRoomId = int("home_room_id")

}