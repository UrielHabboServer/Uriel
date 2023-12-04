package org.urielserv.uriel.game.permissions

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.ranks.RankPermissionsSchema
import org.urielserv.uriel.database.schemas.users.UserPermissionsSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.permissions.ranks.Rank

class UrielPermissionManager {

    private val userPermissions = mutableListOf<Permission>()
    private val rankPermissions = mutableListOf<Permission>()

    init {
        Database.sequenceOf(UserPermissionsSchema)
            .forEach { userPermissions.add(it) }

        Database.sequenceOf(RankPermissionsSchema)
            .forEach { rankPermissions.add(it) }
    }

    fun hasPermission(habbo: Habbo, permission: String): Boolean {
        // Attempt user permissions first
        val userPermission = userPermissions
            .firstOrNull { it.entityId == habbo.info.id && it.permission == permission }

        if (userPermission != null) {
            return userPermission.allow
        }

        // Fallback to rank permissions
        return hasPermission(habbo.info.rank, permission)
    }

    fun hasPermission(rank: Rank, permission: String): Boolean {
        val rankPermission = rankPermissions
            .firstOrNull { it.entityId == rank.id && it.permission == permission }

        if (rankPermission != null) {
            return rankPermission.allow
        }

        return false
    }

    fun setPermission(habbo: Habbo, permission: String, allow: Boolean) {
        val userPermission = userPermissions
            .firstOrNull { it.entityId == habbo.info.id && it.permission == permission }

        if (userPermission != null) {
            userPermission.allow = allow
            userPermission.flushChanges()
            return
        }

        val newPermission = Permission {
            this.entityId = habbo.info.id
            this.permission = permission
            this.allow = allow
        }

        userPermissions.add(newPermission)
        newPermission.flushChanges()
    }

    fun setPermission(rank: Rank, permission: String, allow: Boolean) {
        val rankPermission = rankPermissions
            .firstOrNull { it.entityId == rank.id && it.permission == permission }

        if (rankPermission != null) {
            rankPermission.allow = allow
            rankPermission.flushChanges()
            return
        }

        val newPermission = Permission {
            this.entityId = rank.id
            this.permission = permission
            this.allow = allow
        }

        rankPermissions.add(newPermission)
        newPermission.flushChanges()
    }

}