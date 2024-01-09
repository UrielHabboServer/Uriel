package org.urielserv.uriel.game.command_system

import io.klogging.noCoLogger
import org.urielserv.uriel.CommandManager
import org.urielserv.uriel.extensions.localise
import org.urielserv.uriel.extensions.text
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
        var mutableArgs = args.toMutableList()

        val executor = getExecutorFunction(mutableArgs)
        val executorFunction = executor?.first
        val executorClass = executor?.second

        if (executorFunction == null) {
            habbo.notifications.chatAlert(text("uriel.command_system.error.unknown_subcommand", "subcommand" to name))
            return
        }

        val parameters = mutableListOf<Any?>()
        var hasUsedFirstHabboAsSender = false
        val functionParameters = executorFunction.parameters.drop(1)
        for ((index, parameter) in functionParameters.withIndex()) {
            val type = parameter.type.classifier as KClass<*>
            val parameterBuilder = CommandManager.getParameterBuilder(type)

            if (type == Habbo::class && !hasUsedFirstHabboAsSender) {
                hasUsedFirstHabboAsSender = true
                parameters.add(habbo)
                continue
            }

            if (parameter.isOptional && mutableArgs.isEmpty()) {
                parameters.add(null)
                continue
            }

            if (parameterBuilder == null) {
                logger.error("No parameter builder found for type: $type")
                return
            }

            val clonedArgs = mutableArgs.toMutableList()

            try {
                val parameterValue = parameterBuilder(clonedArgs) // will consume the arguments as needed

                if (parameterValue == null && functionParameters.size == index) {
                    habbo.notifications.chatAlert(text("uriel.command_system.error.invalid_parameter", "parameter_name" to parameter.name!!))
                    return
                } else if (parameterValue == null && parameter.isOptional) {
                    parameters.add(null)
                    continue
                }

                mutableArgs = clonedArgs
                parameters.add(parameterValue)
            } catch (exc: Exception) {
                logger.error("Error building parameter: ${parameter.name}")
                exc.printStackTrace()
                return
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

    internal fun getMainCommandFunction(): KFunction<*>? {
        return this::class.functions
            .firstOrNull { function -> function.annotations.any { it is MainCommand } }
    }

    private fun getExecutorFunction(args: MutableList<String>): Pair<KFunction<*>?, Any?>? {
        val subCommand = args.getOrNull(0)

        return if (subCommand == null) {
            getMainCommandFunction() to this
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
                    return (getMainCommandFunction() to this)

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
