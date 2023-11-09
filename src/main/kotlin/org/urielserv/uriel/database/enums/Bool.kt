package org.urielserv.uriel.database.enums

/**
 * Represents a boolean value.
 *
 * The Bool class is an enumeration that defines two possible values: TRUE and FALSE.
 * This class is used to represent boolean values in the database.
 */
enum class Bool {
    TRUE,
    FALSE;

    fun toBoolean(): Boolean = this == TRUE
}