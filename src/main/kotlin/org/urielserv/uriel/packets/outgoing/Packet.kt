package org.urielserv.uriel.packets.outgoing

import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import kotlinx.coroutines.runBlocking
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.networking.UrielServerClient

/**
 * Interface representing a packet.
 */
@Suppress("unused")
abstract class Packet {

    abstract val packetId: Int

    private val channelByteBuffer = Unpooled.buffer()
    private val byteStream = ByteBufOutputStream(channelByteBuffer)

    /**
     * Construct method.
     *
     * This method is a suspend function that constructs and initializes a packet.
     * It is designed to be called from a coroutine context.
     */
    abstract suspend fun construct()

    suspend fun broadcast() {
        for (habbo in HabboManager.getHabbos()) {
            send(habbo)
        }
    }

    suspend fun broadcast(habbos: List<Habbo>) {
        for (habbo in habbos) {
            send(habbo)
        }
    }

    suspend fun broadcast(vararg habbos: Habbo) {
        for (habbo in habbos) {
            send(habbo)
        }
    }

    suspend fun broadcast(room: Room) {
        for (habbo in room.getHabbos()) {
            send(habbo)
        }
    }

    /**
     * Sends a message to the specified Habbo.
     * Acts as a wrapper for the send method that accepts an Uriel server client.
     *
     * @param habbo The Habbo to send the message to.
     */
    suspend fun send(habbo: Habbo) {
        send(habbo.client)
    }

    /**
     * Sends a message to the specified Habbo.
     * Acts as a wrapper for the send method that accepts an Uriel server client.
     *
     * @param habbo The Habbo to send the message to.
     */
    fun sendSync(habbo: Habbo) {
        runBlocking {
            send(habbo)
        }
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
     * Sends a packet to the given Uriel server client.
     *
     * @param client The Uriel server client to send the packet to.
     */
    fun sendSync(client: UrielServerClient) {
        runBlocking {
            send(client)
        }
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