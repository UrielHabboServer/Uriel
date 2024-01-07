package org.urielserv.uriel.game.command_system

import io.klogging.noCoLogger
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.core.database.schemas.CommandsSchema
import org.urielserv.uriel.game.command_system.commands.users.*
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.rooms.Room
import kotlin.reflect.KClass

class UrielCommandManager {

    private val logger = noCoLogger(UrielCommandManager::class)

    private val commandInfos = mutableListOf<CommandInfo>()
    private val commandMap = mutableMapOf<CommandInfo, CommandBase>()

    private val parameterBuilders = mutableMapOf<KClass<*>, (argumentConsumer: MutableList<String>) -> Any?>()

    init {
        Database.sequenceOf(CommandsSchema).forEach {
            if (!it.enabled) return@forEach

            commandInfos.add(it)
        }

        registerAllCommands()
        registerAllParameterBuilders()
    }

    private fun registerAllCommands() {
        registerCommand(AboutCommand)
        registerCommand(CommandsCommand)

        registerFunCommands()
    }

    private fun registerFunCommands() {
        registerCommand(SitCommand)
        registerCommand(LayCommand)
        registerCommand(StandCommand)
        registerCommand(InviteCommand)
    }

    private fun registerAllParameterBuilders() {
        registerParameterBuilder(Habbo::class) {
            HabboManager.getConnectedHabbo(it.removeAt(0))
        }

        registerParameterBuilder(HabboInfo::class) {
            HabboManager.getHabboInfo(it.removeAt(0))
        }

        registerParameterBuilder(Room::class) {
            RoomManager.getRoom(it.removeAt(0).toIntOrNull() ?: 0)
        }

        registerParameterBuilder(Array<Habbo>::class) {
            val habbos = mutableListOf<Habbo>()

            while (it.isNotEmpty()) {
                val habbo = HabboManager.getConnectedHabbo(it.removeAt(0))

                if (habbo != null) {
                    habbos.add(habbo)
                }
            }

            habbos.toTypedArray()
        }

        registerParameterBuilder(Array<HabboInfo>::class) {
            val habboInfos = mutableListOf<HabboInfo>()

            while (it.isNotEmpty()) {
                val habboInfo = HabboManager.getHabboInfo(it.removeAt(0))

                if (habboInfo != null) {
                    habboInfos.add(habboInfo)
                }
            }

            habboInfos.toTypedArray()
        }

        registerParameterBuilder(Short::class) {
            it.removeAt(0).toShortOrNull()
        }

        registerParameterBuilder(Int::class) {
            it.removeAt(0).toIntOrNull()
        }

        registerParameterBuilder(Long::class) {
            it.removeAt(0).toLongOrNull()
        }

        registerParameterBuilder(Float::class) {
            it.removeAt(0).toFloatOrNull()
        }

        registerParameterBuilder(Double::class) {
            it.removeAt(0).toDoubleOrNull()
        }

        registerParameterBuilder(Boolean::class) {
            it.removeAt(0).toBoolean()
        }

        registerParameterBuilder(String::class) {
            // use the rest of the arguments as the string
            val str = it.joinToString(" ")

            it.clear()

            str
        }
    }

    fun registerCommand(commandBase: CommandBase) {
        // find commandInfo for the commandBase name
        val commandInfo = commandInfos.find { it.name == commandBase.name }

        if (commandInfo == null) {
            logger.error("Couldn't find any Command Information for Command: ${commandBase.name}")
            return
        }

        commandMap[commandInfo] = commandBase
    }

    fun registerParameterBuilder(clazz: KClass<*>, builder: (argumentConsumer: MutableList<String>) -> Any?) {
        parameterBuilders[clazz] = builder
    }

    fun getCommandInfo(commandBase: CommandBase): CommandInfo? {
        return commandInfos.find { it.name == commandBase.name }
    }

    fun getCommandBase(commandInfo: CommandInfo): CommandBase? {
        return commandMap[commandInfo]
    }

    fun getCommandByInvoker(invoker: String): Pair<CommandInfo, CommandBase>? {
        val commandInfo = commandInfos.find { invoker in it.invokers }
        val commandBase = commandMap[commandInfo]

        if (commandInfo == null || commandBase == null) return null

        return Pair(commandInfo, commandBase)
    }

    fun getCommands(): List<CommandInfo> {
        return commandInfos
    }

    fun getParameterBuilder(clazz: KClass<*>): ((argumentConsumer: MutableList<String>) -> Any?)? {
        return parameterBuilders[clazz]
    }

}