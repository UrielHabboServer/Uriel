package org.urielserv.uriel

import com.akuleshov7.ktoml.Toml
import io.klogging.Level
import io.klogging.config.loggingConfiguration
import io.klogging.logger
import io.klogging.rendering.*
import io.klogging.sending.STDOUT
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import org.urielserv.uriel.Uriel.BuildConfig
import org.urielserv.uriel.configuration.UrielConfiguration
import org.urielserv.uriel.configuration.UrielHotelSettings
import org.urielserv.uriel.database.UrielDatabase
import org.urielserv.uriel.game.habbos.UrielHabboManager
import org.urielserv.uriel.game.habbos.wardrobe.UrielWardrobeManager
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.UrielFigureDataManager
import org.urielserv.uriel.networking.UrielServer
import org.urielserv.uriel.packets.UrielPacketHandlerManager
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
lateinit var WardrobeManager: UrielWardrobeManager

lateinit var Server: UrielServer
lateinit var PacketHandlerManager: UrielPacketHandlerManager

var Ready = false

suspend fun main() = runBlocking {
    println()
    println("Uriel Habbo Server | v${BuildConfig.VERSION} | @dapping")
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

    logger.info("Uriel is starting...")

    Configuration = loadTomlFile("uriel.toml")
    HotelSettings = loadTomlFile("uriel_hotel_settings.toml")

    Database = UrielDatabase(
        host = Configuration.database.host,
        port = Configuration.database.port,
        username = Configuration.database.username,
        password = Configuration.database.password,
        databaseName = Configuration.database.databaseName
    )

    HabboManager = UrielHabboManager()
    FigureDataManager = UrielFigureDataManager(
        pathUri = HotelSettings.habbos.wardrobe.figureDataUrl
    )
    WardrobeManager = UrielWardrobeManager()

    Server = UrielServer(
        host = Configuration.server.ip,
        port = Configuration.server.port,
        routePath = Configuration.server.route
    )
    PacketHandlerManager = UrielPacketHandlerManager()

    Ready = true

    launch {
        Server.start()
    }

    logger.info("Uriel initialization successful. The server should start shortly")
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