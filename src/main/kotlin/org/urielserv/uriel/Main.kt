package org.urielserv.uriel

import com.akuleshov7.ktoml.Toml
import io.klogging.Level
import io.klogging.config.loggingConfiguration
import io.klogging.logger
import io.klogging.rendering.RENDER_ANSI
import io.klogging.sending.STDOUT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import org.urielserv.uriel.Uriel.BuildConfig
import org.urielserv.uriel.configuration.UrielConfiguration
import org.urielserv.uriel.configuration.UrielHotelSettings
import org.urielserv.uriel.database.UrielDatabase
import org.urielserv.uriel.extensions.schedule
import org.urielserv.uriel.extensions.scheduleRepeating
import org.urielserv.uriel.game.currencies.UrielCurrencyManager
import org.urielserv.uriel.game.habbos.UrielHabboManager
import org.urielserv.uriel.game.habbos.wardrobe.figure_data.UrielFigureDataManager
import org.urielserv.uriel.game.navigator.UrielNavigatorManager
import org.urielserv.uriel.game.permissions.UrielPermissionManager
import org.urielserv.uriel.game.permissions.ranks.UrielRankManager
import org.urielserv.uriel.game.rooms.UrielRoomManager
import org.urielserv.uriel.networking.UrielServer
import org.urielserv.uriel.packets.incoming.UrielPacketHandlerManager
import org.urielserv.uriel.tick_loop.TickLoop
import org.urielserv.uriel.locale.UrielLocalizer
import org.urielserv.uriel.events.UrielEventDispatcher
import org.urielserv.uriel.events.Event
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private val logger = logger("org.urielserv.uriel.Main")

lateinit var Configuration: UrielConfiguration
lateinit var HotelSettings: UrielHotelSettings

lateinit var EventDispatcher: UrielEventDispatcher

lateinit var Database: UrielDatabase
lateinit var Localizer: UrielLocalizer

lateinit var RankManager: UrielRankManager
lateinit var PermissionManager: UrielPermissionManager

lateinit var CurrencyManager: UrielCurrencyManager
lateinit var FigureDataManager: UrielFigureDataManager
lateinit var HabboManager: UrielHabboManager

lateinit var RoomManager: UrielRoomManager
lateinit var NavigatorManager: UrielNavigatorManager

lateinit var Server: UrielServer
lateinit var PacketHandlerManager: UrielPacketHandlerManager

lateinit var HotelTickLoop: TickLoop

var Ready = false

suspend fun main() = runBlocking {
    println()
    println("Uriel Habbo Server | v${BuildConfig.VERSION}")
    println("https://github.com/notdap/Uriel")
    println()

    loggingConfiguration {
        sink("stdout", RENDER_ANSI, STDOUT)

        logging {
            fromLoggerBase("org.urielserv", stopOnMatch = true)
            fromMinLevel(Level.DEBUG) {
                toSink("stdout")
            }
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            shutdown()
        }
    })

    EventDispatcher = UrielEventDispatcher()

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

    measureInitialProcess("Localizer") {
        Localizer = UrielLocalizer()
    }

    measureInitialProcess("Rank Manager & Permission Manager") {
        RankManager = UrielRankManager()
        PermissionManager = UrielPermissionManager()
    }

    measureInitialProcess("Currency Manager, Figure Data Manager & Habbo Manager") {
        CurrencyManager = UrielCurrencyManager()
        FigureDataManager = UrielFigureDataManager(
            pathUri = HotelSettings.habbos.wardrobe.figureDataUrl
        )
        HabboManager = UrielHabboManager()
    }

    measureInitialProcess("Room Manager & Navigator Manager") {
        RoomManager = UrielRoomManager()
        NavigatorManager = UrielNavigatorManager()
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

    EventDispatcher.dispatch(Event.OnLoad)

    Ready = true

    measureInitialProcess("Tick Loop") {
        HotelTickLoop = TickLoop(
            ticksPerSecond = Configuration.tickLoops.hotelTicksPerSecond
        )
    }
}

private suspend fun shutdown() {
    logger.info("Shutting down Uriel...")

    HabboManager.shutdown()
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