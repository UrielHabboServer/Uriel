package org.urielserv.uriel.core.plugin_loader

interface UrielPlugin {

    fun onLoad() = Unit
    fun onStart() = Unit

    fun onShutdown() = Unit
    fun onUnload() = Unit

}