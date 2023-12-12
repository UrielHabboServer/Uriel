package org.urielserv.uriel.extensions

import org.urielserv.uriel.Localizer

fun text(key: String, vararg args: Pair<String, String>): String =
    Localizer.getString(key, *args)