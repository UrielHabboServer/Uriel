package org.urielserv.uriel.packets.incoming.handshake

import io.klogging.logger
import org.urielserv.uriel.Configuration
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class ReleaseVersionPacketHandler : PacketHandler {

    private val logger = logger(ReleaseVersionPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        try {
            val releaseVersion = packet.getString()
            val type = packet.getString()
            val platformId = packet.getInt()
            val deviceCategoryId = packet.getInt()

            val platform = UrielServerClient.NitroInformation.Platform.getFromNumber(platformId)
            val deviceCategory = UrielServerClient.NitroInformation.DeviceCategory.getFromNumber(deviceCategoryId)

            if (platform != UrielServerClient.NitroInformation.Platform.HTML5
                && Configuration.connections.onlyAllowNitro
            ) {
                logger.warn("Client ${client.ip}:${client.port} joined from a non-Nitro client, disconnecting")
                client.dispose()
            }

            client.nitroInformation = UrielServerClient.NitroInformation(releaseVersion, type, platform, deviceCategory)
        } catch (ignored: Exception) {
            logger.warn("Client ${client.ip}:${client.port} sent an invalid ReleaseVersion packet and was disconnected")
            client.dispose()
        }
    }

}