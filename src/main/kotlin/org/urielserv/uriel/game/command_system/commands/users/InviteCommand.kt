package org.urielserv.uriel.game.command_system.commands.users

import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.habbos.Habbo

@Command("invite")
object InviteCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo, vararg habbos: Habbo) {
        if (sender.roomUnit == null) return

        for (habbo in habbos) {
            val friendship = sender.messenger.getFriendship(habbo.info.id) ?: continue

            friendship.sendInvite(sender, "")
        }
    }

}