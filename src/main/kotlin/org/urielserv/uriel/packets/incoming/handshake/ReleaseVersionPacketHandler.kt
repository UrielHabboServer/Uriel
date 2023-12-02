package org.urielserv.uriel.packets.incoming.handshake

import io.klogging.logger
import org.urielserv.uriel.Configuration
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class ReleaseVersionPacketHandler : PacketHandler {

    private val logger = logger(ReleaseVersionPacketHandler::class)

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        try {
            val releaseVersion = packet.readString()
            val type = packet.readString()
            val platformId = packet.readInt()
            val deviceCategoryId = packet.readInt()

            val platform = UrielServerClient.NitroInformation.Platform.getFromNumber(platformId)
            val deviceCategory = UrielServerClient.NitroInformation.DeviceCategory.getFromNumber(deviceCategoryId)

            if (platform != UrielServerClient.NitroInformation.Platform.HTML5
                && Configuration.connections.onlyAllowNitro) {
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