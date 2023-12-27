package org.urielserv.uriel.game.command_system.commands.users

import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.habbos.Habbo

@Command("lay")
object LayCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo) {
        if (sender.roomUnit == null) return

        if (sender.roomUnit!!.currentTile == sender.room!!.tileMap!!.doorTile) return

        sender.roomUnit!!.layOnFloor()
    }

}