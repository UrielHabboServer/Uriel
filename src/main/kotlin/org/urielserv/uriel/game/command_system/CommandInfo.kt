package org.urielserv.uriel.game.command_system

import org.ktorm.entity.Entity
import org.urielserv.uriel.HotelSettings

interface CommandInfo : Entity<CommandInfo> {

    val id: Int

    val name: String
    val description: String
    val usage: String
    val formattedUsage: String
        get() = usage.ifEmpty {
            "${HotelSettings.commands.prefix}$name"
        }

    val permission: String
    val enabled: Boolean

    val rawInvokers: String
    val invokers: List<String>
        get() = rawInvokers.split(",").map { it.trim() }

}