package org.urielserv.uriel.packets

import io.ktor.websocket.*
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.networking.UrielServerClient

/**
 * Interface representing a packet.
 */
@Suppress("unused")
open class Packet {

    open val packetId = 0

    private val channelByteBuffer = Unpooled.buffer()
    private val byteStream = ByteBufOutputStream(channelByteBuffer)

    /**
     * Construct method.
     *
     * This method is a suspend function that constructs and initializes a packet.
     * It is designed to be called from a coroutine context.
     */
    open suspend fun construct() = Unit

    /**
     * Sends a message to the specified Habbo.
     * Acts as a wrapper for the send method that accepts a Uriel server client.
     *
     * @param habbo The Habbo to send the message to.
     */
    suspend fun send(habbo: Habbo) {
        send(habbo.client!!)
    }

    /**
     * Sends a packet to the given Uriel server client.
     *
     * @param client The Uriel server client to send the packet to.
     */
    suspend fun send(client: UrielServerClient) {
        appendInt(0)
        appendShort(packetId.toShort())
        construct()

        channelByteBuffer.setInt(0, channelByteBuffer.writerIndex() - 4)

        client.send(channelByteBuffer.copy().array())
    }

    /**
     * Appends a boolean value to the byte array.
     *
     * @param boolean the boolean value to append
     */
    fun appendBoolean(boolean: Boolean) {
        byteStream.writeBoolean(boolean)
    }

    /**
     * Appends a byte to the byte array.
     *
     * @param byte the byte to append
     */
    fun appendByte(byte: Byte) {
        byteStream.writeByte(byte.toInt())
    }

    /**
     * Appends a short value to the byte array.
     *
     * @param short the short value to append
     */
    fun appendShort(short: Short) {
        byteStream.writeShort(short.toInt())
    }

    /**
     * Appends an integer value to the byte array.
     *
     * @param int The integer value to append.
     */
    fun appendInt(int: Int) {
        byteStream.writeInt(int)
    }

    /**
     * Appends a long value to the byte array.
     *
     * @param long the long value to append
     */
    fun appendLong(long: Long) {
        byteStream.writeLong(long)
    }

    /**
     * Appends a string to the byte array.
     *
     * @param string the string to be appended
     */
    fun appendString(string: String) {
        byteStream.writeShort(string.length)
        byteStream.write(string.toByteArray())
    }

    /**
     * Appends a byte array to the byte array.
     *
     * @param array the byte array to be appended
     */
    fun appendByteArray(array: ByteArray) {
        byteStream.write(array)
    }

}