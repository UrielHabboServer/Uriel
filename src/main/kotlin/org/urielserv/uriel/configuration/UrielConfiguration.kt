package org.urielserv.uriel.configuration

import kotlinx.serialization.Serializable

/**
 * UrielConfiguration is a data class that represents the configuration settings for the Uriel server.
 * It contains nested data classes for different types of configuration settings.
 *
 * @property server the server configuration settings
 * @property database the database configuration settings
 * @property connections the connections configuration settings
 * @property packets the packets configuration settings
 * @property security the security configuration settings
 */
@Serializable
data class UrielConfiguration(
    val server: Server,
    val database: Database,
    val connections: Connections,
    val packets: Packets,
    val security: Security,
    val tickLoops: TickLoops
) {

    @Serializable
    data class Server(
        val ip: String,
        val port: Int,
        val route: String
    )

    @Serializable
    data class Database(
        val host: String,
        val port: Int,
        val username: String,
        val password: String,
        val databaseName: String
    )

    @Serializable
    data class Connections(
        val maxConnectedClients: Int,
        val maxConnectionsPerIp: Int
    )

    @Serializable
    data class Packets(
        val packetRateLimitTime: Int,
        val packetRateLimitAmount: Int
    )

    @Serializable
    data class Security(
        val refreshSSOTicketOnLogin: Boolean
    )

    @Serializable
    data class TickLoops(
        val hotelTicksPerSecond: Int,
        val roomTicksPerSecond: Int
    )

}