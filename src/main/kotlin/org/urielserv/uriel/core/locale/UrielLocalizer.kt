package org.urielserv.uriel.core.locale

import org.ktorm.entity.forEach
import io.klogging.noCoLogger
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.LocalTextsSchema

/** 
 * The Localizer is used to translate text from the database. 
 */
@Suppress("unused")
class UrielLocalizer {

    private val logger = noCoLogger(UrielLocalizer::class)

    private val texts = mutableMapOf<String,String>()

    init {
        Database.sequenceOf(LocalTextsSchema)
            .forEach { texts[it.key] = it.value }
    }

    fun getString(key: String, vararg args: Pair<String, String>): String {
        if (key !in texts) {
            logger.warn("Couldn't find text: $key")
            return key
        }

        val text = texts[key]!!

        return format(text, *args)
    }

    private fun format(text: String, vararg args: Pair<String, String>): String {
        var formatted = text

        for (arg in args) {
            formatted = formatted.replace("%${arg.first}%", arg.second)
        }

        return formatted
    }

}