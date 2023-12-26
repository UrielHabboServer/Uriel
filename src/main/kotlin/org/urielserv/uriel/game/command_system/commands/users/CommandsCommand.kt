package org.urielserv.uriel.game.command_system.commands.users

import org.urielserv.uriel.CommandManager
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.notifications.complex.PopUpNotification

@Command("commands")
object CommandsCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo) {
        val commands = CommandManager.getCommands()
            .filter {
                (it.permission.isBlank() || sender.hasPermission(it.permission)) && it.name != "commands"
            }
            .sortedBy { it.name }
            .sortedByDescending { it.permission.length }

        val commandString = buildString {
            appendLine("${commands.size} commands available")
            for (command in commands) {
                appendLine("<b>${command.formattedUsage}</b> - ${command.description}")
            }
        }

        sender.notifications.sendComplexNotification(
            PopUpNotification(
                type = "commands",
                title = "Commands",
                message = commandString
            )
        )
    }

}