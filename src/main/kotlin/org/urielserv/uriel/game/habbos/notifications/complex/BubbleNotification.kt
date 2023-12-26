package org.urielserv.uriel.game.habbos.notifications.complex

class BubbleNotification(
    override val type: String,
    val message: String,
) : ComplexNotification {

    private var linkUrl = ""
    private var imageUrl = ""

    fun withImage(url: String): BubbleNotification {
        imageUrl = url
        return this
    }

    fun withLink(url: String): BubbleNotification {
        linkUrl = url
        return this
    }

    override fun build(): List<Pair<String, String>> {
        return listOf(
            "display" to "BUBBLE",
            "message" to message,
            "linkUrl" to linkUrl,
            "image" to imageUrl
        )
    }

}