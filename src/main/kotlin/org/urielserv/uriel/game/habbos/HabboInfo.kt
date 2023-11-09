package org.urielserv.uriel.game.habbos

import org.ktorm.dsl.eq
import org.urielserv.uriel.Database
import org.urielserv.uriel.WardrobeManager
import org.urielserv.uriel.database.schemas.UsersSchema

class HabboInfo private constructor(
    val id: Int = 0
) {

    private var initialized = false

    var username: String = ""
        set(value) {
            if (initialized && value != field) {
                Database.update(UsersSchema) {
                    it.username to value
                    where {
                        it.id eq id
                    }
                }
            }

            field = value
        }
    var motto: String = ""
        set(value) {
            if (initialized && value != field) {
                Database.update(UsersSchema) {
                    it.motto to value
                    where {
                        it.id eq id
                    }
                }
            }

            field = value
        }

    var gender: UsersSchema.Gender = UsersSchema.Gender.MALE
        set(value) {
            if (initialized && value != field) {
                Database.update(UsersSchema) {
                    it.gender to value
                    where {
                        it.id eq id
                    }
                }
            }

            field = value
        }
    var look: String = ""
        set(value) {
            field = value

            val validatedLook = WardrobeManager.validateLook(habbo)

            if (validatedLook != look) {
                field = validatedLook
            }

            if (initialized && value != field) {
                Database.update(UsersSchema) {
                    it.look to value
                    where {
                        it.id eq id
                    }
                }
            }
        }


    var homeRoomId: Int = 0
        set(value) {
            if (initialized && value != field) {
                Database.update(UsersSchema) {
                    it.homeRoomId to value
                    where {
                        it.id eq id
                    }
                }
            }

            field = value
        }

    lateinit var habbo: Habbo

    class Builder(
        val id: Int
    ) {

        private var username: String = ""
        private var motto: String = ""

        private var gender: UsersSchema.Gender = UsersSchema.Gender.MALE
        private var look: String = ""

        private var homeRoomId: Int = 0

        fun username(username: String): Builder {
            this.username = username
            return this
        }

        fun motto(motto: String): Builder {
            this.motto = motto
            return this
        }

        fun gender(gender: UsersSchema.Gender): Builder {
            this.gender = gender
            return this
        }

        fun look(look: String): Builder {
            this.look = look
            return this
        }

        fun homeRoomId(homeRoomId: Int): Builder {
            this.homeRoomId = homeRoomId
            return this
        }

        fun build(): HabboInfo {
            assert(username.isNotEmpty()) { "Username cannot be empty" }
            assert(motto.isNotEmpty()) { "Motto cannot be empty" }
            assert(look.isNotEmpty()) { "Look cannot be empty" }

            return HabboInfo(id).apply {
                username = this@Builder.username
                motto = this@Builder.motto

                gender = this@Builder.gender
                look = this@Builder.look

                homeRoomId = this@Builder.homeRoomId

                initialized = true
            }
        }

    }

}