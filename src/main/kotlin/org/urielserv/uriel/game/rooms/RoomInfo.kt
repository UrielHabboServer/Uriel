package org.urielserv.uriel.game.rooms

import org.ktorm.entity.Entity
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.navigator.NavigatorFlatCategory
import org.urielserv.uriel.game.navigator.NavigatorPublicCategory

interface RoomInfo : Entity<RoomInfo> {

    companion object : Entity.Factory<RoomInfo>()

    val id: Int
    var ownerHabboInfo: HabboInfo

    var name: String
    var description: String
    var publicCategory: NavigatorPublicCategory?
    var flatCategory: NavigatorFlatCategory
    var tags: String

    var model: RoomModel

    var accessType: RoomAccessType
    var password: String

    var users: Int
    var maximumUsers: Int

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

    var allowWalkthrough: Boolean
    var allowDiagonalMovement: Boolean

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