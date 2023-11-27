package org.urielserv.uriel.game.habbos

enum class HabboGender {

    MALE,
    FEMALE;

    fun short(): String = this.name.substring(0, 1)

}