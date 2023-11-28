package org.urielserv.uriel

import com.akuleshov7.ktoml.Toml
import io.klogging.Level
import io.klogging.config.loggingConfiguration
import io.klogging.logger
import io.klogging.rendering.RENDER_ANSI
import io.klogging.sending.STDOUT
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import org.urielserv.uriel.Uriel.BuildConfig
import org.urielserv.uriel.configuration.UrielConfiguration
import org.urielserv.uriel.configuration.UrielHotelSettings
import org.urielserv.uriel.database.UrielDatabase
import org.urielserv.uriel.game.habbos.UrielHabboManager
import org.urielserv.uriel.game.habbos.wardrobe.ClothingValidator
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.UrielFigureDataManager
import org.urielserv.uriel.networking.UrielServer
import org.urielserv.uriel.packets.incoming.UrielPacketHandlerManager
import org.urielserv.uriel.tick_loop.UrielTickLoop
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.system.exitProcess

private val logger = logger("uriel.Main")

lateinit var Configuration: UrielConfiguration
lateinit var HotelSettings: UrielHotelSettings

lateinit var Database: UrielDatabase

lateinit var HabboManager: UrielHabboManager
lateinit var FigureDataManager: UrielFigureDataManager

lateinit var Server: UrielServer
lateinit var PacketHandlerManager: UrielPacketHandlerManager

lateinit var TickLoop: UrielTickLoop

var Ready = false

suspend fun main() = runBlocking {
    println()
    println("Uriel Habbo Server | v${BuildConfig.VERSION}")
    println("https://git.krews.org/dap/uriel")
    println()

    loggingConfiguration {
        sink("stdout", RENDER_ANSI, STDOUT)

        logging {
            fromLoggerBase("uriel", stopOnMatch = true)
            fromMinLevel(Level.DEBUG) {
                toSink("stdout")
            }
        }
    }

    logger.info("Starting Uriel, please wait...")

    measureInitialProcess("Configuration & Hotel Settings") {
        Configuration = loadTomlFile("uriel.toml")
        HotelSettings = loadTomlFile("uriel_hotel_settings.toml")
    }

    measureInitialProcess("Database") {
        Database = UrielDatabase(
            host = Configuration.database.host,
            port = Configuration.database.port,
            username = Configuration.database.username,
            password = Configuration.database.password,
            databaseName = Configuration.database.databaseName
        )
    }

    measureInitialProcess("Habbo Manager & Figure Data Manager") {
        HabboManager = UrielHabboManager()
        FigureDataManager = UrielFigureDataManager(
            pathUri = HotelSettings.habbos.wardrobe.figureDataUrl
        )
    }

    measureInitialProcess("Server & Packet Handler Manager") {
        Server = UrielServer(
            host = Configuration.server.ip,
            port = Configuration.server.port,
            routePath = Configuration.server.route
        )
        PacketHandlerManager = UrielPacketHandlerManager()
    }

    launch {
        Server.start()
    }

    Ready = true

    measureInitialProcess("Tick Loop") {
        TickLoop = UrielTickLoop(
            ticksPerSecond = Configuration.tickLoops.hotelTicksPerSecond
        )
    }
}

private suspend inline fun <reified T> loadTomlFile(pathString: String): T {
    val path = Path(pathString)

    if (!path.exists()) {
        val classLoader = Thread.currentThread().contextClassLoader
        val resource = classLoader.getResource(pathString)

        if (resource === null) {
            logger.error("Could not find $pathString in the current directory or in the resources folder")
            exitProcess(1)
        } else {
            // copy resource to current directory
            val resourcePath = Path(resource.path)
            resourcePath.copyTo(path)

            logger.info("Copied $pathString from resources to current directory")
        }
    }

    val contents = path.readText()

    return Toml.decodeFromString<T>(contents)
}

private suspend fun measureInitialProcess(name: String, block: suspend () -> Unit) {
    logger.info("Initialising $name...")

    try {
        val start = System.currentTimeMillis()
        block()
        val timeTaken = System.currentTimeMillis() - start

        logger.info("Initialised $name in ${timeTaken}ms")
    } catch (exc: Exception) {
        logger.error("Failed to initialise $name:")
        exc.printStackTrace()
        exitProcess(1)
    }
}