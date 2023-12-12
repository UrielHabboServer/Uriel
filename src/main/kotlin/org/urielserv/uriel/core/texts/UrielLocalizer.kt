package org.urielserv.uriel.core.texts

import org.ktorm.entity.forEach
import io.klogging.noCoLogger
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.core.LocalTextsSchema

/** 
 * The Localizer is used to translate text from the database. 
 */
@Suppress("unused")
class UrielLocalizer {
    private val logger = noCoLogger(UrielLocalizer::class)

    private val texts = mutableMapOf<String,String>()

    init {
        Database.sequenceOf(LocalTextsSchema)
            .forEach { texts.set(it.key, it.value) }
    }

    fun getString(key: String): String {
        if(texts[key] == null) {
            logger.warn("Missing text for ${key}")
            return ""
        } else
            return texts[key]!!
    }
}