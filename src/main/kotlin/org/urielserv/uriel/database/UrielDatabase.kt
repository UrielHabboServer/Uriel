package org.urielserv.uriel.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.BaseTable
import org.ktorm.schema.ColumnDeclaring

/**
 * UrielDatabase represents a database connection and provides methods for performing
 * common database operations like querying, inserting, updating, and deleting.
 *
 * @param host The host of the database server.
 * @param port The port number of the database server.
 * @param username The username for authenticating with the database server.
 * @param password The password for authenticating with the database server.
 * @param databaseName The name of the database to connect to.
 */
class UrielDatabase(
    host: String,
    port: Int,
    username: String,
    password: String,
    databaseName: String
) {

    private val hikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://$host:$port/$databaseName"
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 10

        this.username = username
        this.password = password
    }

    private val dataSource = HikariDataSource(hikariConfig)
    private val database = Database.connect(dataSource)

    /**
     * Creates a QuerySource object from the given BaseTable object.
     *
     * @param table The BaseTable object to create the QuerySource from.
     * @return The created QuerySource object.
     */
    fun <T : Any> from(table: BaseTable<T>): QuerySource {
        return database.from(table)
    }

    /**
     * Inserts a row into the database table.
     *
     * @param table The table to insert the row into.
     * @param block The block of assignments to be applied to the table.
     * @return The number of rows inserted.
     */
    fun <T : BaseTable<*>> insert(table: T, block: AssignmentsBuilder.(T) -> Unit): Int {
        return database.insert(table, block)
    }

    /**
     * Updates the specified table using the given block of code.
     *
     * @param table the table to update
     * @param block the code block that defines the update statement
     * @return the number of rows affected by the update
     */
    fun <T : BaseTable<*>> update(table: T, block: UpdateStatementBuilder.(T) -> Unit): Int {
        return database.update(table, block)
    }

    /**
     * Deletes rows from the given table based on the provided predicate.
     *
     * @param table the table from which rows will be deleted
     * @param predicate the predicate used to filter rows for deletion
     * @return the number of rows deleted
     */
    fun <T : BaseTable<*>> delete(table: T, predicate: (T) -> ColumnDeclaring<Boolean>): Int {
        return database.delete(table, predicate)
    }

}