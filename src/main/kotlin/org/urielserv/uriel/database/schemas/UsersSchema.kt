package org.urielserv.uriel.database.schemas

import org.ktorm.schema.*
import org.urielserv.uriel.database.enums.Bool
import org.urielserv.uriel.game.habbos.HabboGender

@Suppress("unused")
object UsersSchema : Table<Nothing>("users") {

    val id = int("id").primaryKey()

    val username = varchar("username")
    val password = varchar("password")

    val ssoTicket = varchar("sso_ticket")

    val email = varchar("email")
    val isEmailVerified = enum<Bool>("is_email_verified")

    val rankId = int("rank")

    val accountCreated = long("account_creation_timestamp")

    val lastLogin = long("last_login_timestamp")
    val lastOnline = long("last_online_timestamp")
    val isOnline = enum<Bool>("is_online")

    val motto = varchar("motto")
    val look = varchar("look")
    val gender = enum<HabboGender>("gender")

    val credits = int("credits")
    val pixels = int("pixels")
    val points = int("points")

    val registrationIp = varchar("registration_ip")
    val currentIp = varchar("current_ip")

    val machineId = varchar("machine_id")

    val homeRoomId = int("home_room_id")

}