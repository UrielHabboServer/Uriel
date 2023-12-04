package org.urielserv.uriel.game.permissions

import org.ktorm.entity.Entity

interface Permission : Entity<Permission> {

    companion object : Entity.Factory<Permission>()

    val id: Int
    var entityId: Int

    var permission: String
    var allow: Boolean

}