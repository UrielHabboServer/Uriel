@file:Suppress("unused")

package org.urielserv.uriel.extensions

import java.nio.ByteBuffer

/**
 * Reads a String from the ByteBuffer (short length followed by bytes).
 *
 * @return the String read from the ByteBuffer.
 */
fun ByteBuffer.getString(): String {
    val length = this.getShort().toInt()
    val bytes = ByteArray(length)

    this.get(bytes, 0, length)

    return String(bytes)
}

/**
 * Reads a Boolean from the ByteBuffer (1 byte).
 *
 * @return the Boolean read from the ByteBuffer.
 */
fun ByteBuffer.getBoolean(): Boolean {
    return this.get() == 1.toByte()
}
