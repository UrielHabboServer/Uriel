package org.urielserv.uriel.extensions

import org.urielserv.uriel.PermissionManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.permissions.ranks.Rank

fun Habbo.hasPermission(permission: String): Boolean
    = PermissionManager.hasPermission(this, permission)

fun Rank.hasPermission(permission: String): Boolean
    = PermissionManager.hasPermission(this, permission)