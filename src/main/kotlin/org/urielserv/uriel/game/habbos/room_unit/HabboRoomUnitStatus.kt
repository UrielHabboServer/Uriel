package org.urielserv.uriel.game.habbos.room_unit

enum class HabboRoomUnitStatus(
    val key: String
) {

    MOVE("mv"),

    SIT_IN("sit-in"),
    SIT("sit"),
    SIT_OUT("sit-out"),

    LAY_IN("lay-in"),
    LAY("lay"),
    LAY_OUT("lay-out"),

    FLAT_CONTROL("flatctrl"),
    SIGN("sign"),
    GESTURE("gst"),
    WAVE("wav"),
    TRADING("trd"),

    DIP("dip"),

    EAT_IN("eat-in"),
    EAT("eat"),
    EAT_OUT("eat-out"),

    BEG("beg"),

    DEAD_IN("ded-in"),
    DEAD("ded"),
    DEAD_OUT("ded-out"),

    JUMP_IN("jmp-in"),
    JUMP("jmp"),
    JUMP_OUT("jmp-out"),

    PLAY_IN("pla-in"),
    PLAY("pla"),
    PLAY_OUT("pla-out"),

    SPEAK("spk"),
    CROAK("crk"),
    RELAX("rlx"),
    WINGS("wng"),
    FLAME("flm"),
    RIP("rip"),
    GROW("grw"),
    GROW_1("grw1"),
    GROW_2("grw2"),
    GROW_3("grw3"),
    GROW_4("grw4"),
    GROW_5("grw5"),
    GROW_6("grw6"),
    GROW_7("grw7"),

    KICK("kck"),
    WAG_TAIL("wag"),
    DANCE("dan"),
    AMS("ams"),
    SWIM("swm"),
    TURN("trn"),

    SRP("srp"),
    SRP_IN("srp-in"),

    SLEEP_IN("slp-in"),
    SLEEP("slp"),
    SLEEP_OUT("slp-out");

}