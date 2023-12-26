package org.urielserv.uriel.extensions

import org.urielserv.uriel.Localizer

fun text(key: String, vararg args: Pair<String, String>): String =
    Localizer.getString(key, *args)

fun String.localise(vararg args: Pair<String, String>): String =
    Localizer.getString(this, *args)

fun Array<out String>.localise(): List<String> {
    return this
        .map {
            if (it.startsWith("l:")) {
                it.substring(2)
            } else {
                it
            }
        }
}

fun Collection<String>.localise(): List<String> {
    return this
        .map {
            if (it.startsWith("l:")) {
                it.substring(2)
            } else {
                it
            }
        }
}
