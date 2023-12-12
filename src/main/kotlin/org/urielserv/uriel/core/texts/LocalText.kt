package org.urielserv.uriel.core.texts

import org.ktorm.entity.Entity

interface LocalText : Entity<LocalText> {
    var key: String
    var value: String

}