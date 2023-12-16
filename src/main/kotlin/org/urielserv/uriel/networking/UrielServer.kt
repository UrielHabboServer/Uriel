package org.urielserv.uriel.networking

import io.klogging.logger
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.urielserv.uriel.Configuration
import org.urielserv.uriel.PacketHandlerManager
import org.urielserv.uriel.extensions.readShort
import java.time.Duration

/**
 * Represents the Uriel server that listens for incoming Nitro WebSocket connections and handles them.
 *
 * @property host The host IP address to bind the server to. Default is "127.0.0.1".
 * @property port The port number to bind the server to. Default is 2096.
 * @property routePath The path to the WebSocket endpoint. Default is "/".
 */
class UrielServer(
    val host: String = "127.0.0.1",
    val port: Int = 2096,
    val routePath: String = "/"
) {

    private val logger = logger(UrielServer::class)

    private val server = embeddedServer(Netty, host = host, port = port) {
        module()
    }
    private val connectedClients = mutableListOf<UrielServerClient>()

    init {
        server
    }

    /**
     * Starts the WebSocket server.
     *
     * This method starts the WebSocket server on the specified host and port.
     * It waits for incoming connections and handles them accordingly.
     */
    suspend fun start() {
        logger.info("Starting the WebSocket Server on $host:$port")
        server.start(wait = true)
    }

    /**
     * Shuts down the WebSocket Server.
     *
     * This method stops the WebSocket Server and logs a shutdown message.
     * The server will be stopped with a graceful shutdown timeout of 1000 milliseconds
     * for both the endpoint handlers and the HTTP server.
     */
    fun shutdown() {
        server.stop(1000, 1000)
    }

    private fun addClient(client: UrielServerClient) {
        connectedClients.add(client)
    }

    fun disposeClient(client: UrielServerClient) {
        connectedClients.remove(client)
    }

    private fun getTotalConnectionsFromIp(ip: String): Int {
        return connectedClients.count { it.ip == ip }
    }

    private fun Application.module() {
        webSocket()
        routes()
    }

    private fun Application.webSocket() {
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = 500000L
            masking = false
        }
    }

    private fun Application.routes() {
        routing {
            webSocket(routePath) {
                val ip = call.request.local.remoteAddress
                val port = call.request.local.remotePort

                if (connectedClients.size >= Configuration.connections.maxConnectedClients) {
                    logger.warn("Client attempted to connect from $ip:$port, but was denied due to exceeding the maximum amount of connected clients")
                    this.close()
                    return@webSocket
                }

                if (getTotalConnectionsFromIp(ip) >= Configuration.connections.maxConnectionsPerIp) {
                    logger.warn("Client attempted to connect from $ip:$port, but was denied due to exceeding the maximum amount of connections per IP")
                    this.close()
                    return@webSocket
                }

                val client = UrielServerClient(
                    ip,
                    port,
                    socketServerSession = this
                )
                addClient(client)

                logger.debug("Client connected from ${client.ip}:${client.port}")

                for (frame in incoming) {
                    frame as? Frame.Binary ?: continue

                    val packet = frame.readBytes()

                    // create byte stream
                    val byteStream = packet.inputStream()

                    // ignore first 4 bytes
                    byteStream.skip(4)

                    // read packet id (short)
                    val packetId = byteStream.readShort()

                    logIncomingPacket(packetId, client, packet)

                    PacketHandlerManager.handlePacket(packetId, client, byteStream)
                }

                if (client.habbo != null) {
                    client.habbo!!.disconnect()
                } else {
                    client.dispose()
                }

                logger.debug("Client disconnected from ${client.ip}:${client.port}")

                disposeClient(client)
            }
        }
    }

    private suspend fun logIncomingPacket(packetId: Short, client: UrielServerClient, packet: ByteArray) {
        logger.debug("Packet: $packetId | Client: ${client.ip}:${client.port} | Packet: ${packet.contentToString()}")
    }

}