package org.urielserv.uriel.core.database.schemas.messenger

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.urielserv.uriel.game.habbos.messenger.interfaces.Relationship

object MessengerRelationshipsSchema : Table<Relationship>("messenger_relationships") {

    val id = int("id").primaryKey().bindTo { it.id }
    val nitroId = int("nitro_id").bindTo { it.nitroId }

}