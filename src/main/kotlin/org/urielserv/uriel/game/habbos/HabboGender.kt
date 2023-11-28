package org.urielserv.uriel.game.habbos

enum class HabboGender {

    MALE,
    FEMALE;

    fun short(): String = this.name.substring(0, 1)

    companion object {

        fun tryFromShort(gender: String): HabboGender {
            return if (gender.equals("M", ignoreCase = true)) {
                MALE
            } else if (gender.equals("F", ignoreCase = true)) {
                FEMALE
            } else {
                throw IllegalArgumentException("No gender found from short $gender")
            }
        }

    }

}