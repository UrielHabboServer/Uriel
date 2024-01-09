package org.urielserv.uriel.game.habbos.notifications.complex

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PopUpNotification(
    val title: String,
    val message: String,
) : ComplexNotification {

    override var type = "pop_up"
        private set

    private var buttonLinkTitle = ""
    private var buttonLinkUrl = ""

    private var imageUrl = ""

    fun withButtonLink(title: String, url: String): PopUpNotification {
        buttonLinkTitle = title
        buttonLinkUrl = url
        return this
    }

    fun withImage(url: String): PopUpNotification {
        imageUrl = url
        return this
    }

    fun searchable(): PopUpNotification {
        type = "search"
        return this
    }

    override fun build(): List<Pair<String, String>> {
        val pairs = listOf(
            "title" to title,
            "linkTitle" to buttonLinkTitle,
            "linkUrl" to buttonLinkUrl,
            "image" to imageUrl
        )

        if (type == "search") {
            val entries = message.split("\n")
            val entriesJson = Json.encodeToString(entries)

            return pairs + ("message" to entriesJson)
        } else {
            return pairs + ("message" to message)
        }
    }

}