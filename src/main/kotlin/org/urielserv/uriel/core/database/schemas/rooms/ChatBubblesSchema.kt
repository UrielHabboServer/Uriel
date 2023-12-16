package org.urielserv.uriel.core.database.schemas.rooms

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.rooms.chat.ChatBubble

object ChatBubblesSchema : Table<ChatBubble>("chat_bubbles") {

    val id = int("id").primaryKey().bindTo { it.id }
    val nitroStyleId = int("nitro_style_id").bindTo { it.nitroStyleId }

    val name = varchar("name").bindTo { it.name }

    val isSystemBubble = boolean("is_system_bubble").bindTo { it.isSystemBubble }
    val isClubOnly = boolean("is_club_only").bindTo { it.isClubOnly }
    val isAmbassadorOnly = boolean("is_ambassador_only").bindTo { it.isAmbassadorOnly }

    val canBeOverridden = boolean("can_be_overridden").bindTo { it.canBeOverridden }
    val canTriggerTalkingFurniture = boolean("can_trigger_talking_furniture").bindTo { it.canTriggerTalkingFurniture }

}