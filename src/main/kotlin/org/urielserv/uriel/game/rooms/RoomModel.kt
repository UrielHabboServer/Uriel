package org.urielserv.uriel.game.rooms

import org.ktorm.entity.Entity

interface RoomModel : Entity<RoomModel> {

    val id: Int
    var name: String

    var heightmap: String

    var isClubOnly: Boolean
    var isCustom: Boolean

    var doorX: Int
    var doorY: Int
    var doorDirection: Int

}