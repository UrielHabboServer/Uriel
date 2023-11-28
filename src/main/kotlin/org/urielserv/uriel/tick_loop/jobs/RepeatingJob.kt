package org.urielserv.uriel.tick_loop.jobs

class RepeatingJob internal constructor(
    id: Int,
    start: Int,
    val interval: Int,
    task: () -> Unit
) : Job(id, start, task)