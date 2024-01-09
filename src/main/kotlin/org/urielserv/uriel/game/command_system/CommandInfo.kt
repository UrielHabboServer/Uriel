package org.urielserv.uriel.game.command_system

import org.ktorm.entity.Entity
import org.urielserv.uriel.CommandManager
import org.urielserv.uriel.game.habbos.Habbo

interface CommandInfo : Entity<CommandInfo> {

    val id: Int

    val name: String
    val description: String

    val permission: String
        get() = "uriel.commands.$name"
    val enabled: Boolean

    val rawInvokers: String
    val invokers: List<String>
        get() = rawInvokers.split(",").map { it.trim() }

    val usage: String
        get() {
            val commandBase = CommandManager.getCommandBase(this) ?: return ":$name"

            val mainCommandFunction = commandBase.getMainCommandFunction() ?: return ":$name"

            val parameters = mainCommandFunction.parameters.toMutableList()
            parameters.removeAt(0)

            // get the parameters of type habbo and drop the first one as it is the sender
            val habboParameters = parameters.filter { it.type.classifier == Habbo::class }
            parameters.remove(habboParameters.firstOrNull())

            return ":$name ${parameters.joinToString(" ") { "<${it.name}>" }}"
        }

}