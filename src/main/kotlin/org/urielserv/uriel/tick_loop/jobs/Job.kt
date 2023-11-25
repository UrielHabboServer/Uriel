package org.urielserv.uriel.tick_loop.jobs

open class Job internal constructor(
    val id: Int,
    val start: Int,
    private val task: () -> Unit
) {

    fun run() {
        task()
    }

}