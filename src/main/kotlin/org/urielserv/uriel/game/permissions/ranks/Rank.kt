package org.urielserv.uriel.game.permissions.ranks

import org.ktorm.entity.Entity
import org.urielserv.uriel.RankManager

interface Rank : Entity<Rank> {

    val id: Int
    val nitroId: Int
    val name: String

    val weight: Int
    val parentId: Int
    val parent: Rank?
        get() = RankManager.getRank(parentId)

    val badge: String?

    val prefix: String?
    val prefixColor: String?

}