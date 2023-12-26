package org.urielserv.uriel.game.command_system

import io.klogging.noCoLogger
import org.urielserv.uriel.CommandManager
import org.urielserv.uriel.extensions.localise
import org.urielserv.uriel.game.command_system.annotations.Command
import org.urielserv.uriel.game.command_system.annotations.MainCommand
import org.urielserv.uriel.game.command_system.annotations.SubCommand
import org.urielserv.uriel.game.habbos.Habbo
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.functions

abstract class CommandBase {

    private val logger = noCoLogger(this::class)

    val name: String
        get() = this::class.annotations
            .filterIsInstance<Command>()
            .map { it.name }
            .ifEmpty {
                return this::class.annotations
                    .filterIsInstance<SubCommand>()
                    .map { it.invokers.firstOrNull() }
                    .firstOrNull() ?: "unknown"
            }
            .firstOrNull() ?: "unknown"

    internal suspend fun execute(habbo: Habbo, args: List<String>) {
        val mutableArgs = args.toMutableList()

        val executor = getExecutorFunction(mutableArgs)
        val executorFunction = executor?.first
        val executorClass = executor?.second

        if (executorFunction == null) {
            habbo.roomUnit!!.sendAlert("Unknown sub-command: ${args.first()}")
            return
        }

        val parameters = mutableListOf<Any?>()
        var hasUsedFirstHabboAsSender = false
        for (parameter in executorFunction.parameters.drop(1)) {
            val type = parameter.type.classifier as KClass<*>
            val parameterBuilder = CommandManager.getParameterBuilder(type)

            if (type == Habbo::class && !hasUsedFirstHabboAsSender) {
                hasUsedFirstHabboAsSender = true
                parameters.add(habbo)
                continue
            }

            if (parameterBuilder == null) {
                logger.error("No parameter builder found for type: $type")
                return
            }

            val parameterValue = parameterBuilder(mutableArgs) // will consume the arguments as needed

            if (parameterValue == null) {
                habbo.roomUnit!!.sendAlert("Invalid parameter: ${parameter.name}")
                return
            }

            parameters.add(parameterValue)

            if (mutableArgs.isEmpty()) {
                break
            }
        }

        try {
            if (executorFunction.isSuspend) {
                executorFunction.callSuspend(executorClass, *parameters.toTypedArray())
            } else {
                executorFunction.call(executorClass, *parameters.toTypedArray())
            }
        } catch (exc: Exception) {
            logger.error("Error executing command: $name")
            exc.printStackTrace()
        }

        return
    }

    private fun getExecutorFunction(args: MutableList<String>): Pair<KFunction<*>?, Any?>? {
        val subCommand = args.getOrNull(0)

        return if (subCommand == null) {
            this::class.functions
                .firstOrNull { function -> function.annotations.any { it is MainCommand } } to this
        } else {
            val subCommandFunction = this::class.functions
                .firstOrNull { function ->
                    function.annotations.any { it is SubCommand && subCommand.lowercase() in it.invokers.localise() }
                }

            if (subCommandFunction != null) {
                args.removeAt(0)

                subCommandFunction to this
            } else {
                val subCommandClass = findSubCommandClass(subCommand.lowercase())
                    ?: // use main command, since there's no sub-command with that name, we can assume they're parameters
                    return this::class.functions
                        .firstOrNull { function -> function.annotations.any { it is MainCommand } } to this

                args.removeAt(0)

                subCommandClass.getExecutorFunction(args)
            }
        }
    }

    private fun findSubCommandClass(subCommand: String): CommandBase? {
        return this::class.nestedClasses
            .find {
                val objectInstance = it.objectInstance
                objectInstance is CommandBase && objectInstance.name.equals(subCommand, ignoreCase = true)
            }
            ?.objectInstance as? CommandBase
    }

}
