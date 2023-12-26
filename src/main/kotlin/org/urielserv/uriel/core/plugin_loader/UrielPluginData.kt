package org.urielserv.uriel.core.plugin_loader

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File
import java.util.jar.JarFile

@Serializable
data class UrielPluginData(
    val information: Information,
    val entry: Entry
) {

    @Transient
    internal lateinit var pluginFile: File
    @Transient
    internal lateinit var jarFile: JarFile

    @Serializable
    data class Information(
        val name: String = "No name provided",
        val description: String = "No description provided",
        val author: String = "Unknown",
        val version: String = "1.0.0"
    )

    @Serializable
    data class Entry(
        val mainClass: String,
        val dependencies: List<String>
    )

}
