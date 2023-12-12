package org.urielserv.uriel.game.currencies

import org.ktorm.entity.Entity

interface UrielCurrency : Entity<UrielCurrency> {

    val id: Int
    var nitroId: Int

    var name: String
    var isSeasonal: Boolean
    var default: Int
    
    var autoTimer: Int
    var toGive: Int

}