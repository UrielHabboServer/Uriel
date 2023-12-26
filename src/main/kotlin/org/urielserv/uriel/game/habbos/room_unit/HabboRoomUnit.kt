package org.urielserv.uriel.game.habbos.room_unit

import kotlinx.coroutines.runBlocking
import org.urielserv.uriel.ChatBubblesManager
import org.urielserv.uriel.CommandManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.extensions.scheduleRepeating
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.game.rooms.chat.ChatBubble
import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.game.rooms.tiles.RoomTile
import org.urielserv.uriel.game.rooms.tiles.RoomTileDirection
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.RoomUnitStatusPacket
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat.RoomUnitChatPacket
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat.RoomUnitChatShoutPacket
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat.RoomUnitChatWhisperPacket
import org.urielserv.uriel.tick_loop.jobs.RepeatingJob
import kotlin.time.Duration.Companion.milliseconds

class HabboRoomUnit(
    val id: Int,

    val habbo: Habbo,
    val room: Room,

    currentTile: RoomTile,

    var bodyRotation: RoomTileDirection,
    var headRotation: RoomTileDirection = bodyRotation
) {

    var currentTile = currentTile
        private set(value) {
            previousTile = field
            field = value

            previousTile.roomUnitsOnTile.remove(this)
            currentTile.roomUnitsOnTile.add(this)

            if (currentTile == room.tileMap!!.doorTile) {
                runBlocking {
                    room.leave(habbo)
                }
            }
        }
    var previousTile = currentTile

    val statuses = mutableMapOf<HabboRoomUnitStatus, String>()

    private var goalTile: RoomTile? = null
    private var goalPath: List<RoomTile>? = null
    private var goalJob: RepeatingJob? = null
    private var isWalking = false

    var isStanding = true
        private set

    var isSittingOnChair = false
        private set
    var isSittingOnFloor = false
        private set

    suspend fun talk(message: RoomChatMessage) {
        if (message.message.startsWith(HotelSettings.commands.prefix)) {
            val split = message.message.substring(1).split(" ")
            val invoker = split[0]

            val command = CommandManager.getCommandByInvoker(invoker)

            if (command != null) {
                val permission = command.first.permission

                if (permission.isBlank() || habbo.hasPermission(permission)) {
                    command.second.execute(habbo, split.drop(1))
                    return
                }
            }
        }

        when (message.type) {
            RoomChatMessage.ChatType.TALK -> {
                RoomUnitChatPacket(message).broadcast(room)
            }

            RoomChatMessage.ChatType.SHOUT -> {
                RoomUnitChatShoutPacket(message).broadcast(room)
            }

            RoomChatMessage.ChatType.WHISPER -> {
                if (message.whisperTarget == null) return

                RoomUnitChatWhisperPacket(message).broadcast(habbo, message.whisperTarget!!)
            }
        }
    }

    suspend fun sendAlert(
        message: String,
        chatBubble: ChatBubble = ChatBubblesManager.getChatBubbleById(HotelSettings.habbos.alertChatBubbleId)!!
    ) {
        RoomUnitChatPacket(
            RoomChatMessage(
                habbo,
                message,
                chatBubble,
                RoomChatMessage.ChatType.TALK,
                ignoreBubbleChecks = true
            )
        ).send(habbo)
    }

    suspend fun stand() {
        if (isWalking) return

        isStanding = true
        isSittingOnChair = false
        isSittingOnFloor = false

        statuses.remove(HabboRoomUnitStatus.SIT)

        RoomUnitStatusPacket(habbo).broadcast(room)
    }

    suspend fun sitOnFloor() {
        if (isWalking) return

        isStanding = false
        isSittingOnChair = false
        isSittingOnFloor = true

        bodyRotation = bodyRotation.toNonDiagonal()
        headRotation = bodyRotation

        statuses[HabboRoomUnitStatus.SIT] = "0.5"

        RoomUnitStatusPacket(habbo).broadcast(room)
    }

    suspend fun lookAt(tile: RoomTile) {
        if (isWalking && !isSittingOnChair) return

        var direction = RoomTileDirection.getDirectionFromTileToTile(currentTile, tile)

        if (isSittingOnFloor) {
            direction = direction.toNonDiagonal()
        }

        bodyRotation = direction
        headRotation = direction

        RoomUnitStatusPacket(habbo).broadcast(room)
    }

    suspend fun walkTo(tile: RoomTile) {
        if (isSittingOnFloor || isSittingOnChair) {
            stand()
        }

        goalTile = tile
        isWalking = true

        if (goalJob == null) {
            val delay = if (HotelSettings.rooms.noDelayOnWalk) 0.milliseconds else 250.milliseconds

            goalJob = scheduleRepeating(room, interval = 500.milliseconds, delay) {
                goalPath = room.tileMap!!.getPathTo(currentTile, goalTile!!).drop(1)

                if (goalTile == null) {
                    stopWalking()
                    return@scheduleRepeating
                }

                if (goalPath!!.isEmpty()) {
                    stopWalking()
                    return@scheduleRepeating
                }

                val nextTile = goalPath!![0]

                if (nextTile == currentTile) {
                    goalPath = goalPath!!.drop(1)
                    return@scheduleRepeating
                }

                val direction = RoomTileDirection.getDirectionFromTileToTile(currentTile, nextTile)

                bodyRotation = direction
                headRotation = direction

                statuses[HabboRoomUnitStatus.MOVE] = "${nextTile.x},${nextTile.y},${nextTile.height}"

                currentTile = nextTile

                goalPath = goalPath!!.drop(1)

                RoomUnitStatusPacket(habbo).broadcast(room)
            }
        }
    }

    suspend fun stopWalking() {
        goalTile = null
        goalPath = null

        isWalking = false

        statuses.remove(HabboRoomUnitStatus.MOVE)

        goalJob?.isCancelled = true
        goalJob = null

        RoomUnitStatusPacket(habbo).broadcast(room)
    }

}