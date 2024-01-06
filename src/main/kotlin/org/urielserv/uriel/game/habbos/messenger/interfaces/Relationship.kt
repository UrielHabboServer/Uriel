package org.urielserv.uriel.game.habbos.messenger.interfaces

import org.ktorm.entity.Entity

interface Relationship : Entity<Relationship> {

    val id: Int
    val nitroId: Int

}