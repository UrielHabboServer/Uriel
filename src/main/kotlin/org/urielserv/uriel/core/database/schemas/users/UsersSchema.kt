package org.urielserv.uriel.core.database.schemas.users

import org.ktorm.schema.*
import org.urielserv.uriel.core.database.schemas.ranks.RanksSchema
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.HabboInfo

@Suppress("unused")
object UsersSchema : Table<HabboInfo>("users") {

    val id = int("id").primaryKey().bindTo { it.id }

    val username = varchar("username").bindTo { it.username }
    val password = text("password").bindTo { it.password }

    val ssoTicket = varchar("sso_ticket").bindTo { it.ssoTicket }

    val email = varchar("email").bindTo { it.email }
    val isEmailVerified = boolean("is_email_verified").bindTo { it.isEmailVerified }

    val rankId = int("rank_id").references(RanksSchema) { it.rank }

    val accountCreation = int("account_creation_timestamp").bindTo { it.accountCreation }

    val lastLogin = int("last_login_timestamp").bindTo { it.lastLogin }
    val lastOnline = int("last_online_timestamp").bindTo { it.lastOnline }
    val isOnline = boolean("is_online").bindTo { it.isOnline }

    val motto = varchar("motto").bindTo { it.motto }
    val look = text("look").bindTo { it.look }
    val gender = enum<HabboGender>("gender").bindTo { it.gender }

    val registrationIp = varchar("registration_ip").bindTo { it.registrationIp }
    val currentIp = varchar("current_ip").bindTo { it.currentIp }

    val homeRoomId = int("home_room_id").bindTo { it.homeRoomId }

}