package org.urielserv.uriel.packets

/**
 * A utility class for building byte arrays.
 *
 * This class provides methods for appending various data types to a byte array.
 * The appended data can be retrieved as a byte array using the `toByteArray` method.
 */
@Suppress("unused")
class ByteArrayBuilder {

    private val bytes = mutableListOf<Byte>()

    /**
     * Appends a byte to the internal byte list.
     *
     * @param byte The byte to append.
     */
    fun appendByte(byte: Byte) {
        bytes.add(byte)
    }

    /**
     * Appends a short value to the internal byte list.
     *
     * @param short the short value to append
     */
    fun appendShort(short: Short) {
        bytes.add((short.toInt() shr 8).toByte())
        bytes.add((short.toInt() and 0xFF).toByte())
    }

    /**
     * Appends an integer value to the internal byte list.
     *
     * @param int The integer value to append.
     */
    fun appendInt(int: Int) {
        bytes.add((int shr 24).toByte())
        bytes.add((int shr 16).toByte())
        bytes.add((int shr 8).toByte())
        bytes.add((int and 0xFF).toByte())
    }

    /**
     * Appends a long value to the internal byte list.
     *
     * @param long the long value to append
     */
    fun appendLong(long: Long) {
        bytes.add((long shr 56).toByte())
        bytes.add((long shr 48).toByte())
        bytes.add((long shr 40).toByte())
        bytes.add((long shr 32).toByte())
        bytes.add((long shr 24).toByte())
        bytes.add((long shr 16).toByte())
        bytes.add((long shr 8).toByte())
        bytes.add((long and 0xFF).toByte())
    }

    /**
     * Appends a string to the internal byte list.
     *
     * @param string the string to be appended
     */
    fun appendString(string: String) {
        val size = string.length

        appendShort(size.toShort())
        appendByteArray(string.toByteArray())
    }

    /**
     * Appends a boolean value to the internal byte list.
     *
     * @param boolean The boolean value to append.
     */
    fun appendBoolean(boolean: Boolean) {
        bytes.add(if (boolean) 0x01 else 0x00)
    }

    /**
     * Appends a byte array to the internal byte list.
     *
     * @param byteArray The byte array to be appended.
     */
    fun appendByteArray(byteArray: ByteArray) {
        bytes.addAll(byteArray.toList())
    }

    /**
     * Appends a short value at the beginning of the internal byte list.
     *
     * @param short the short value to append
     */
    fun appendShortAtBeginning(short: Short) {
        bytes.add(0, (short.toInt() shr 8).toByte())
        bytes.add(1, (short.toInt() and 0xFF).toByte())
    }

    /**
     * Builds the ByteArray.
     *
     * @return the converted ByteArray.
     */
    fun toByteArray(): ByteArray {
        return bytes.toByteArray()
    }

}