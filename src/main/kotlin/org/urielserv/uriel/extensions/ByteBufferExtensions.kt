@file:Suppress("unused")

package org.urielserv.uriel.extensions

import java.nio.ByteBuffer

/**
 * Reads a String from the ByteBuffer (short length followed by bytes).
 *
 * @return the String read from the ByteBuffer.
 */
fun ByteBuffer.getString(): String {
    return try {
        val length = this.getShort().toInt()
        val bytes = ByteArray(length)
        this.get(bytes, 0, length)
        String(bytes)
    } catch (ignored: Exception) {
        ""
    }
}
