package org.urielserv.uriel.core.plugin_loader

import com.akuleshov7.ktoml.Toml
import io.klogging.noCoLogger
import kotlinx.serialization.decodeFromString
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

class UrielPluginLoader {

    private val logger = noCoLogger(UrielPluginLoader::class)

    private val plugins = mutableMapOf<UrielPluginData, UrielPlugin>()

    init {
        val pluginsFolder = File("plugins")

        if (!pluginsFolder.exists()) {
            pluginsFolder.mkdir()
        }

        val pluginFiles = pluginsFolder.listFiles()

        val pluginData = mutableListOf<UrielPluginData>()
        for (pluginFile in pluginFiles!!) {
            if (!pluginFile.name.endsWith(".jar")) continue

            val pluginInfo = loadPluginData(pluginFile) ?: continue

            pluginData.add(pluginInfo)
        }

        pluginData.sortBy {
            it.entry.dependencies.firstOrNull { dependency ->
                pluginData.any { plugin -> plugin.information.name == dependency }
            }
        }

        // Load plugins
        for (plugin in pluginData) {
            loadPlugin(plugin)
        }
    }

    private fun loadPluginData(pluginFile: File): UrielPluginData? {
        try {
            val jarFile = JarFile(pluginFile)
            val jarEntry = jarFile.getJarEntry("plugin.toml")

            if (jarEntry == null) {
                logger.error("Plugin ${pluginFile.name} does not contain a plugin.toml file")
                return null
            }

            val pluginInfoContents = jarFile.getInputStream(jarEntry).use {
                it.bufferedReader().readText()
            }

            val pluginInfo = Toml.decodeFromString<UrielPluginData>(pluginInfoContents)
            pluginInfo.pluginFile = pluginFile
            pluginInfo.jarFile = jarFile

            return pluginInfo
        } catch (exc: Exception) {
            logger.error("Unable to load plugin data for ${pluginFile.name}:")
            exc.printStackTrace()
        }

        return null
    }

    private fun loadPlugin(pluginData: UrielPluginData) {
        logger.info("Loading plugin ${pluginData.information.name} v${pluginData.information.version} by ${pluginData.information.author}...")

        try {
            val classLoader = URLClassLoader(arrayOf(pluginData.pluginFile.toURI().toURL()), this::class.java.classLoader)
            val pluginClass = classLoader.loadClass(pluginData.entry.mainClass)

            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as UrielPlugin

            plugins[pluginData] = pluginInstance

            logger.info("Successfully loaded plugin ${pluginData.information.name}")

            pluginInstance.onLoad()
        } catch (exc: Exception) {
            logger.error("Failed to load plugin ${pluginData.information.name}:")
            exc.printStackTrace()
        }
    }

    internal fun startPlugins() {
        for ((_, plugin) in plugins) {
            plugin.onStart()
        }
    }

    fun getPlugin(pluginName: String): UrielPlugin? {
        return plugins.keys.firstOrNull { it.information.name == pluginName }?.let { plugins[it] }
    }

    fun getPluginData(pluginName: String): UrielPluginData? {
        return plugins.keys.firstOrNull { it.information.name == pluginName }
    }

    fun unloadPlugin(pluginName: String) {
        val plugin = getPlugin(pluginName) ?: return
        val pluginData = getPluginData(pluginName) ?: return

        plugin.onUnload()

        plugins.remove(pluginData)
    }

    fun shutdown() {
        for ((pluginData, _) in plugins) {
            unloadPlugin(pluginData.information.name)
        }
    }

}