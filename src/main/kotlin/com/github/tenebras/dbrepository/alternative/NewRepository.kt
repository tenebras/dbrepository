package com.github.tenebras.dbrepository.alternative

import com.github.tenebras.dbrepository.ColumnType
import com.github.tenebras.dbrepository.extension.execute
import com.github.tenebras.dbrepository.extension.query
import com.github.tenebras.dbrepository.extension.toMap
import com.github.tenebras.dbrepository.extension.toSnakeCase
import java.sql.Connection
import java.sql.ResultSet
import kotlin.reflect.KProperty1

open class NewRepository<T : Any>(val connection: Connection, val entityReader: EntityReader, val properties: TableInfo<T>) {


    private val columnTypes = mutableMapOf<String, ColumnType>()
    protected val table: String = properties.tableName // todo: escape
    protected val primaryKey: String = properties.primaryKey?.name ?: ""

    protected fun columnType(types: Map<String, ColumnType>) = columnTypes.putAll(types)
    protected fun columnType(vararg arrayOfPairs: Pair<KProperty1<*, *>, ColumnType>)
        = columnTypes.putAll(arrayOfPairs.map { it.first.name.toSnakeCase() to it.second })

    fun query(statement: Sql.() -> String): T = connection.query(Sql(columnTypes, statement)).entity()
    fun queryAll(statement: Sql.() -> String): List<T> = connection.query(Sql(columnTypes, statement)).entities()

    fun exec(statement: Sql.() -> String) = connection.execute(Sql(columnTypes, statement)) // column types!!!


    fun add(params: Map<String, Any?>): Boolean {

        val mutableParams = params.toMutableMap()

        if (isPrimaryKeySpecified() && params.containsKey(primaryKey) && params[primaryKey] == null) {
            mutableParams.remove(primaryKey)
        }

        return connection.execute(Sql.insert(table, mutableParams, columnTypes))
    }

    fun add(item: T, skipped: List<KProperty1<T, *>> = emptyList()): Boolean = add(
        item.toMap(skipped.map { it.name }).mapKeys { it.key.toSnakeCase() }
    )


    private fun isPrimaryKeySpecified(): Boolean = properties.primaryKey !== null

//    fun where(x: FindContions<T>.() -> FindContions.Condition<T>) {
//        x.invoke(FindContions())
//    }

    fun ResultSet.entity(): T {

        if (isBeforeFirst) {
            next()
        }

        return entityReader.read(this, properties.entity)
    }

    fun ResultSet.entities(): List<T> {
        val items = mutableListOf<T>()

        while (next()) {
            items.add(entity())
        }

        return items
    }
}

