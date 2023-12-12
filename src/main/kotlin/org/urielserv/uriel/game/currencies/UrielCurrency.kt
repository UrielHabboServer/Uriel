package org.urielserv.uriel.game.currencies

import org.ktorm.entity.Entity
import kotlin.time.Duration

interface UrielCurrency : Entity<UrielCurrency> {

    val id: Int
    var nitroId: Int

    var name: String
    var isSeasonal: Boolean
    var default: Int
    
    var autoTimerTime: String
    var toGive: Int

}