package org.urielserv.uriel.game.command_system.commands.users

import org.urielserv.uriel.HotelTickLoop
import org.urielserv.uriel.StartTime
import org.urielserv.uriel.Uriel.BuildConfig
import org.urielserv.uriel.extensions.averageMspt
import org.urielserv.uriel.extensions.averageTps
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.command_system.CommandBase
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.notifications.complex.PopUpNotification

@Command("about")
object AboutCommand : CommandBase() {

    @MainCommand
    suspend fun main(sender: Habbo) {
        val uptime = currentUnixSeconds - StartTime

        // format: 1 month, 12 days and 1 hour
        val uptimeString = buildString {
            val months = uptime / 2592000
            val days = (uptime % 2592000) / 86400
            val hours = (uptime % 86400) / 3600
            val minutes = (uptime % 3600) / 60

            if (months > 0) {
                append("$months month${if (months != 1) "s" else ""}, ")
            }

            if (days > 0) {
                append("$days day${if (days != 1) "s" else ""}, ")
            }

            if (hours > 0) {
                append("$hours hour${if (hours != 1) "s" else ""}, ")
            }

            append("$minutes minute${if (minutes != 1) "s" else ""}")
        }

        val runtime = Runtime.getRuntime()

        val notification = PopUpNotification(
            title = "Uriel Habbo Server",
            message = """
                <b>Server Information:</b>
                - Server Version: Uriel ${BuildConfig.VERSION}
                - Server Uptime: $uptimeString
                - Server Cores: ${runtime.availableProcessors()}
                - Server Memory: ${runtime.totalMemory() / 1024 / 1024} / ${runtime.maxMemory() / 1024 / 1024} MB
                
                <b>Tick Loops:</b>
                - Hotel TPS: ${HotelTickLoop.averageTps.toInt()} TPS
                - Hotel MSPT: ${HotelTickLoop.averageMspt.toInt()} MSPT
                - Current Room TPS: ${sender.room!!.tickLoop!!.averageTps.toInt()} TPS
                - Current Room MSPT: ${sender.room!!.tickLoop!!.averageMspt.toInt()} MSPT
                
                @dapping & @laynester
            """.trimIndent()
        )
            .withImage("https://i.imgur.com/TnpamZv.png")
            .withButtonLink("Project GitHub", "https://github.com/UrielHabboServer/Uriel")

        sender.notifications.sendNotification(notification)
    }

}