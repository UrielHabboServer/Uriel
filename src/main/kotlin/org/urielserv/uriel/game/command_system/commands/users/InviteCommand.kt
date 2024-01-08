package org.urielserv.uriel.game.command_system.commands.users

import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.habbos.Habbo

@Command("invite")
object InviteCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo, target: Habbo? = null, message: String = " ") {
        if (sender.roomUnit == null) return

        val targetFriendships = if (target == null) {
            sender.messenger.friendships
        } else {
            listOf(sender.messenger.getFriendship(target) ?: return)
        }

        for (friendship in targetFriendships) {
            friendship.sendInvite(sender, message)
        }
    }

}