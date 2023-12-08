package org.urielserv.uriel.game.permissions.ranks

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.ranks.RanksSchema

class UrielRankManager {

    private val ranks = mutableListOf<Rank>()

    init {
        Database.sequenceOf(RanksSchema)
            .forEach { ranks.add(it) }
    }

    fun getRank(id: Int): Rank? {
        return ranks.firstOrNull { it.id == id }
    }

}