package org.urielserv.uriel.game.command_system.commands.admin

import org.urielserv.uriel.extensions.text
import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.command_system.annotations.SubCommand
import org.urielserv.uriel.game.habbos.Habbo

@Command("give")
object GiveCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo) {
        sender.notifications.chatAlert(text("uriel.commands.give.available_options"))
    }

    @SubCommand("badge")
    suspend fun badge(sender: Habbo, target: Habbo? = null, badge: String) {
        val targetHabbo = target ?: sender

        if (targetHabbo.inventory.badges.hasBadge(badge)) {
            sender.notifications.chatAlert(text("uriel.commands.give.badge_already_existing"))
            return
        }

        targetHabbo.inventory.badges.addBadge(badge)

        targetHabbo.notifications.chatAlert(text("uriel.commands.give.received_badge", "badge" to badge))
        if (sender != targetHabbo)
            sender.notifications.chatAlert(text("uriel.commands.give.gave_badge", "badge" to badge, "target" to targetHabbo.info.username))
    }

}