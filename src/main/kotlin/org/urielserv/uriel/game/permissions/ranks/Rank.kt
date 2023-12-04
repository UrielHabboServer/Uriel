package org.urielserv.uriel.game.permissions.ranks

import org.ktorm.entity.Entity

interface Rank : Entity<Rank> {

    val id: Int
    val name: String

    val weight: Int
    val parent: Rank?

    val badge: String?

    val prefix: String?
    val prefixColor: String?

}