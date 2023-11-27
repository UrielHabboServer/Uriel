package org.urielserv.uriel.extensions

val currentUnixSeconds: Int
    get() = (System.currentTimeMillis() / 1000).toInt()