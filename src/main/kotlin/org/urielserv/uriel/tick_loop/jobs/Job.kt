package org.urielserv.uriel.tick_loop.jobs

open class Job internal constructor(
    val id: Int,
    val delay: Int,
    private val task: suspend () -> Unit
) {

    var isCancelled = false

    suspend fun run() {
        if (isCancelled) return

        task()
    }

}