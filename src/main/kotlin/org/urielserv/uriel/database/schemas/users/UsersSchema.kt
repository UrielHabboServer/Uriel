package org.urielserv.uriel.database.schemas.users

import org.ktorm.schema.*
import org.urielserv.uriel.database.schemas.ranks.RanksSchema
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.HabboInfo

@Suppress("unused")
object UsersSchema : Table<HabboInfo>("users") {

    val id = int("id").primaryKey().bindTo { it.id }

    val username = varchar("username").bindTo { it.username }
    val password = text("password")

    val ssoTicket = varchar("sso_ticket")

    val email = varchar("email")
    val isEmailVerified = boolean("is_email_verified")

    val rankId = int("rank_id").references(RanksSchema) { it.rank }

    val accountCreated = int("account_creation_timestamp")

    val lastLogin = int("last_login_timestamp")
    val lastOnline = int("last_online_timestamp")
    val isOnline = boolean("is_online")

    val motto = varchar("motto").bindTo { it.motto }
    val look = text("look").bindTo { it.look }
    val gender = enum<HabboGender>("gender").bindTo { it.gender }

    val registrationIp = varchar("registration_ip")
    val currentIp = varchar("current_ip")

    val machineId = varchar("machine_id")

    val homeRoomId = int("home_room_id").bindTo { it.homeRoomId }

}