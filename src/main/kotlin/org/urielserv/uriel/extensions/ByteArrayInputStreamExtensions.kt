@file:Suppress("unused")

package org.urielserv.uriel.extensions

import java.io.ByteArrayInputStream

/**
 * Reads two bytes from the input stream and interprets them as a short value.
 *
 * @return the short value read from the input stream, or 0 if an exception occurs.
 */
fun ByteArrayInputStream.readShort(): Short {
    return try {
        val byte1 = this.read()
        val byte2 = this.read()
        ((byte1 shl 8) + (byte2 shl 0)).toShort()
    } catch (ignored: Exception) {
        0
    }
}

/**
 * Reads an unsigned short value from the ByteArrayInputStream.
 *
 * @return The unsigned short value read from the ByteArrayInputStream.
 */
fun ByteArrayInputStream.readUShort(): UShort {
    return try {
        val byte1 = this.read()
        val byte2 = this.read()
        ((byte1 shl 8) + (byte2 shl 0)).toUShort()
    } catch (ignored: Exception) {
        0u
    }
}

/**
 * Reads four bytes from this ByteArrayInputStream and returns the integer value.
 *
 * @return the integer value read from the ByteArrayInputStream, or 0 if an exception occurs
 */
fun ByteArrayInputStream.readInt(): Int {
    return try {
        val byte1 = this.read()
        val byte2 = this.read()
        val byte3 = this.read()
        val byte4 = this.read()
        (byte1 shl 24) + (byte2 shl 16) + (byte3 shl 8) + (byte4 shl 0)
    } catch (ignored: Exception) {
        0
    }
}

/**
 * Reads four bytes from the ByteArrayInputStream and converts them to an unsigned integer.
 * The most significant byte is read first.
 * If an exception occurs during the read operation, the method returns 0.
 *
 * @return The unsigned integer read from the ByteArrayInputStream.
 */
fun ByteArrayInputStream.readUInt(): UInt {
    return try {
        val byte1 = this.read()
        val byte2 = this.read()
        val byte3 = this.read()
        val byte4 = this.read()
        ((byte1 shl 24) + (byte2 shl 16) + (byte3 shl 8) + (byte4 shl 0)).toUInt()
    } catch (ignored: Exception) {
        0u
    }
}

/**
 * Reads a long value from the current position of the ByteArrayInputStream.
 *
 * @return the long value read from the input stream, or 0L if there is an exception while reading.
 */
fun ByteArrayInputStream.readLong(): Long {
    return try {
        val byte1 = this.read()
        val byte2 = this.read()
        val byte3 = this.read()
        val byte4 = this.read()
        val byte5 = this.read()
        val byte6 = this.read()
        val byte7 = this.read()
        val byte8 = this.read()
        (byte1.toLong() shl 56) + (byte2.toLong() shl 48) + (byte3.toLong() shl 40) + (byte4.toLong() shl 32) + (byte5.toLong() shl 24) + (byte6.toLong() shl 16) + (byte7.toLong() shl 8) + (byte8.toLong() shl 0)
    } catch (ignored: Exception) {
        0L
    }
}

/**
 * Reads an unsigned long (8-byte) value from the current position in this ByteArrayInputStream.
 *
 * @return the unsigned long value read from the stream, or 0 if an error occurs during reading.
 */
fun ByteArrayInputStream.readULong(): ULong {
    return try {
        val byte1 = this.read()
        val byte2 = this.read()
        val byte3 = this.read()
        val byte4 = this.read()
        val byte5 = this.read()
        val byte6 = this.read()
        val byte7 = this.read()
        val byte8 = this.read()
        ((byte1.toLong() shl 56) + (byte2.toLong() shl 48) + (byte3.toLong() shl 40) + (byte4.toLong() shl 32) + (byte5.toLong() shl 24) + (byte6.toLong() shl 16) + (byte7.toLong() shl 8) + (byte8.toLong() shl 0)).toULong()
    } catch (ignored: Exception) {
        0u
    }
}

/**
 * Reads a string from the ByteArrayInputStream.
 *
 * @return the string read from the ByteArrayInputStream, or an empty string
 *         if an exception occurs during reading.
 */
fun ByteArrayInputStream.readString(): String {
    return try {
        val length = this.readShort()
        val bytes = ByteArray(length.toInt())
        this.read(bytes, 0, length.toInt())
        String(bytes)
    } catch (ignored: Exception) {
        ""
    }
}
