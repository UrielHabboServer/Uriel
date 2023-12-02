package org.urielserv.uriel.game.rooms

import org.ktorm.entity.Entity
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.game.habbos.HabboInfo

interface RoomInfo : Entity<RoomInfo> {

    val id: Int
    var ownerHabboInfo: HabboInfo

    var name: String
    var description: String
    var category: Int
    var tags: String

    var model: String
    var isCustomModel: Boolean

    var accessType: RoomAccessType
    var password: String

    var users: Int
    var maxUsers: Int

    var score: Int

    var isPublic: Boolean
    var isStaffPicked: Boolean

    var creationTimestamp: Int

    var wallpaper: String
    var floorPattern: String
    var landscape: String

    var wallThickness: Int
    var floorThickness: Int
    var wallHeight: Int

    var areWallsHidden: Boolean
    var areWiredsHidden: Boolean

    var canWalkthrough: Boolean
    var canMoveDiagonally: Boolean

    var allowOtherPets: Boolean
    var allowOtherPetsToEat: Boolean

    var chatMode: Int
    var chatWeight: Int
    var chatScrollingSpeed: Int
    var chatHearingDistance: Int
    var chatFloodProtection: Int

    var whoCanKick: Int
    var whoCanBan: Int
    var whoCanMute: Int

    var tradingMode: Int

    var rollerSpeed: Int

    var isPromoted: Boolean
    var isForSale: Boolean

    val room: Room?
        get() = RoomManager.getRoomById(id)

}