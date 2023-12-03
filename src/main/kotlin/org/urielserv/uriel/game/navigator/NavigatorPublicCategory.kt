package org.urielserv.uriel.game.navigator

import org.ktorm.entity.Entity

interface NavigatorPublicCategory : Entity<NavigatorPublicCategory> {

    val id: Int
    var name: String

    var hasImage: Boolean
    var isVisible: Boolean
    var order: Int

}