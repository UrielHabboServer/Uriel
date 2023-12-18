@file:JvmName("Uriel")

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
import org.ktorm.entity.toList
import org.urielserv.uriel.Uriel.BuildConfig
import org.urielserv.uriel.core.configuration.UrielConfiguration
import org.urielserv.uriel.core.configuration.UrielHotelSettings
import org.urielserv.uriel.core.database.UrielDatabase
import org.urielserv.uriel.core.database.schemas.HotelSettingOverridesSchema
import org.urielserv.uriel.core.event_dispatcher.Events
import org.urielserv.uriel.core.event_dispatcher.UrielEvent
import org.urielserv.uriel.core.event_dispatcher.UrielEventDispatcher
import org.urielserv.uriel.core.locale.UrielLocalizer
import org.urielserv.uriel.core.plugin_loader.UrielPluginData
import org.urielserv.uriel.core.plugin_loader.UrielPluginLoader
import org.urielserv.uriel.game.currencies.UrielCurrencyManager
import org.urielserv.uriel.game.habbos.UrielHabboManager
import org.urielserv.uriel.game.landing_view.UrielLandingViewManager
import org.urielserv.uriel.game.navigator.UrielNavigatorManager
import org.urielserv.uriel.game.permissions.UrielPermissionManager
import org.urielserv.uriel.game.permissions.ranks.UrielRankManager
import org.urielserv.uriel.game.rooms.UrielRoomManager
import org.urielserv.uriel.game.rooms.chat.UrielChatBubblesManager
import org.urielserv.uriel.game.wardrobe.figure_data.UrielFigureDataManager
import org.urielserv.uriel.networking.UrielServer
import org.urielserv.uriel.packets.incoming.UrielPacketHandlerManager
import org.urielserv.uriel.tick_loop.TickLoop
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.system.exitProcess

private val logger = logger("org.urielserv.uriel.Main")

lateinit var Configuration: UrielConfiguration
lateinit var HotelSettings: UrielHotelSettings

lateinit var EventDispatcher: UrielEventDispatcher
lateinit var PluginLoader: UrielPluginLoader

lateinit var Database: UrielDatabase
lateinit var Localizer: UrielLocalizer

lateinit var RankManager: UrielRankManager
lateinit var PermissionManager: UrielPermissionManager

lateinit var CurrencyManager: UrielCurrencyManager
lateinit var FigureDataManager: UrielFigureDataManager
lateinit var HabboManager: UrielHabboManager

lateinit var LandingViewManager: UrielLandingViewManager
lateinit var RoomManager: UrielRoomManager
lateinit var NavigatorManager: UrielNavigatorManager
lateinit var ChatBubblesManager: UrielChatBubblesManager

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

    loadDatabaseHotelSettingsOverrides()

    measureInitialProcess("Event Dispatcher & Plugin Loader") {
        EventDispatcher = UrielEventDispatcher()
        PluginLoader = UrielPluginLoader()
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
            url = HotelSettings.habbos.wardrobe.figureDataUrl
        )
        HabboManager = UrielHabboManager()
    }

    measureInitialProcess("Landing View, Room Manager, Navigator Manager & Chat Bubble Manager") {
        LandingViewManager = UrielLandingViewManager()
        RoomManager = UrielRoomManager()
        NavigatorManager = UrielNavigatorManager()
        ChatBubblesManager = UrielChatBubblesManager()
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
    PluginLoader.startPlugins()
    EventDispatcher.dispatch(Events.Load, UrielEvent())

    measureInitialProcess("Tick Loop") {
        HotelTickLoop = TickLoop(
            name = "Hotel Tick Loop",
            ticksPerSecond = Configuration.tickLoops.hotelTicksPerSecond
        )
    }
}

private suspend fun shutdown() {
    if (!Ready) return

    measureProcessShutdown("Plugin Loader") {
        PluginLoader.shutdown()
    }

    measureProcessShutdown("Habbo Manager") {
        HabboManager.shutdown()
    }

    measureProcessShutdown("Room Manager") {
        RoomManager.shutdown()
    }

    measureProcessShutdown("Tick Loop") {
        HotelTickLoop.end()
    }

    measureProcessShutdown("Server") {
        Server.shutdown()
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

private suspend fun loadDatabaseHotelSettingsOverrides() {
    val hotelSettingsOverrides = Database.sequenceOf(HotelSettingOverridesSchema).toList()

    // use reflection to change the values in the HotelSettings object
    for (hotelSettingsOverride in hotelSettingsOverrides) {
        val parts = hotelSettingsOverride.path.split(".")

        var currentObject: Any = HotelSettings
        for (part in parts) {
            val field = currentObject::class.memberProperties.firstOrNull { it.name == part }

            if (field == null) {
                logger.error("Could not find field $part in HotelSettings override ${hotelSettingsOverride.path}")
                continue
            }

            field.isAccessible = true

            if (field !is KMutableProperty<*>) {
                logger.error("Field $part in HotelSettings override ${hotelSettingsOverride.path} is not mutable")
                continue
            }

            try {
                when (field.getter.call(currentObject)!!::class) {
                    String::class -> field.setter.call(currentObject, hotelSettingsOverride.value)
                    Int::class -> field.setter.call(currentObject, hotelSettingsOverride.value.toInt())
                    Long::class -> field.setter.call(currentObject, hotelSettingsOverride.value.toLong())
                    Double::class -> field.setter.call(currentObject, hotelSettingsOverride.value.toDouble())
                    Float::class -> field.setter.call(currentObject, hotelSettingsOverride.value.toFloat())
                    Boolean::class -> field.setter.call(currentObject, hotelSettingsOverride.value.toBoolean())
                    else ->
                        currentObject = field.getter.call(currentObject) ?: continue
                }
            } catch (exc: Exception) {
                logger.error("Failed to set field $part in HotelSettings override ${hotelSettingsOverride.path}:")
                exc.printStackTrace()
            }
        }
    }
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

private suspend fun measureProcessShutdown(name: String, block: suspend () -> Unit) {
    logger.info("Shutting down $name...")

    try {
        val start = System.currentTimeMillis()
        block()
        val timeTaken = System.currentTimeMillis() - start

        logger.info("Gracefully shut down $name in ${timeTaken}ms")
    } catch (exc: Exception) {
        logger.error("Failed to shut down $name:")
        exc.printStackTrace()
    }
}