package org.urielserv.uriel.core.event_dispatcher

@Suppress("ConstPropertyName")
object Events {

    // Uriel
    const val Load = "ON_LOAD"

    // Users
    const val UserLogin = "ON_USER_LOGIN"
    const val UserUpdateLook = "ON_USER_UPDATE_LOOK"
    const val UserSaveLook = "ON_USER_SAVE_LOOK"

    // Rooms
    const val RoomCreate = "ON_ROOM_CREATE"
    const val RoomEnter = "ON_ROOM_ENTER"

}