package org.urielserv.uriel.game.command_system.commands.users

import org.urielserv.uriel.CommandManager
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.extensions.text
import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.notifications.complex.PopUpNotification

@Command("commands")
object CommandsCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo) {
        val commands = CommandManager.commands.keys
            .filter {
                (it.permission.isBlank() || sender.hasPermission(it.permission)) && it.name != "commands"
            }
            .sortedBy { it.name }

        val commandString = buildString {
            for (command in commands) {
                appendLine("${command.usage} - ${command.description}")
            }
        }

        PopUpNotification(
            title = text("uriel.commands.commands.title",
                "command_amount" to commands.size.toString()
            ),
            message = commandString
        ).searchable().send(sender)
    }

}