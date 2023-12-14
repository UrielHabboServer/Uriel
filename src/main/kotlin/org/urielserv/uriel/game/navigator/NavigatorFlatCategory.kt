package org.urielserv.uriel.game.navigator

import org.ktorm.entity.Entity

interface NavigatorFlatCategory : Entity<NavigatorFlatCategory> {

    val id: Int
    var caption: String

    var maximumUsers: Int

    var isPublic: Boolean
    var allowTrading: Boolean

    var orderNum: Int

}