package org.urielserv.uriel.game.habbos.notifications.complex

class PopUpNotification(
    override val type: String,
    val title: String,
    val message: String,
) : ComplexNotification {

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

    override fun build(): List<Pair<String, String>> {
        return listOf(
            "display" to "POP_UP",
            "title" to title,
            "message" to message,
            "linkTitle" to buttonLinkTitle,
            "linkUrl" to buttonLinkUrl,
            "image" to imageUrl
        )
    }

}