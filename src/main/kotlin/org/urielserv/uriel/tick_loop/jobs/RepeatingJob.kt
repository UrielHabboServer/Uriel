package org.urielserv.uriel.tick_loop.jobs

class RepeatingJob internal constructor(
    id: Int,
    delay: Int,
    val interval: Int,
    task: suspend () -> Unit
) : Job(id, delay, task)