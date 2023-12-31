package org.urielserv.uriel.core.plugin_loader

import io.klogging.noCoLogger

abstract class UrielPlugin {

    val logger = noCoLogger(this::class)

    open suspend fun onLoad() = Unit
    open suspend fun onStart() = Unit

    open suspend fun onUnload() = Unit

}