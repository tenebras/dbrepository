package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.extension.execute
import com.github.tenebras.dbrepository.extension.query
import com.github.tenebras.dbrepository.extension.toMap
import com.github.tenebras.dbrepository.extension.toSnakeCase
import java.sql.Connection
import java.sql.ResultSet
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

open class Repository<T : Any>(val connection: Connection, val entityReader: EntityReader, val tableInfo: TableInfo<T>) {
    val columnTypes = mutableMapOf<String, ColumnType>()
    protected val table: String = tableInfo.tableName // todo: escape
    protected val primaryKey: String = tableInfo.primaryKey?.name?.toSnakeCase() ?: ""

    protected fun columnType(types: Map<String, ColumnType>) = columnTypes.putAll(types)
    protected fun columnType(vararg arrayOfPairs: Pair<KProperty1<*, *>, ColumnType>) = columnTypes.putAll(arrayOfPairs.map { it.first.name.toSnakeCase() to it.second })

    fun isPrimaryKeySpecified(): Boolean = tableInfo.primaryKey !== null

    fun query(statement: Sql.() -> String): T = connection.query(Sql(columnTypes, statement)).entity(tableInfo.entity)!!

    fun queryList(statement: Sql.() -> String): List<T> = connection.query(Sql(columnTypes, statement)).entities(tableInfo.entity)

    inline fun <reified U : Any> queryAs(noinline statement: Sql.() -> String): U? = connection.query(Sql(columnTypes, statement)).entity()

    inline fun <reified U : Any> queryListOf(noinline statement: Sql.() -> String): List<U?> = connection.query(Sql(columnTypes, statement)).entities()

    fun exec(statement: Sql.() -> String) = connection.execute(Sql(columnTypes, statement))

    fun add(params: Map<String, Any?>): Boolean {

        val mutableParams = params.mapKeys { it.key.toSnakeCase() }.toMutableMap()

        if (isPrimaryKeySpecified() && params.containsKey(primaryKey) && params[primaryKey] == null) {
            mutableParams.remove(primaryKey)
        }

        return connection.execute(Sql.insert(table, mutableParams, columnTypes))
    }

    fun add(item: T, skipped: List<KProperty1<T, *>> = emptyList()): Boolean = add(item.toMap(skipped.map { it.name }))

    fun update(item: T, skipped: List<KProperty1<T, *>> = emptyList()): Boolean = update(item.toMap(skipped.map { it.name }))

    fun update(params: Map<String, Any?>): Boolean {
        return connection.execute(Sql.update(
            table,
            primaryKey,
            params.mapKeys { it.key.toSnakeCase() },
            columnTypes
        ))
    }

//    fun where(x: FindContions<T>.() -> FindContions.Condition<T>) {
//        Sql.find(table, x.invoke(FindContions()))
//    }

    fun <U : Any> ResultSet.entity(clazz: KClass<U>): U {
        if (isBeforeFirst) {
            next()
        }

        return entityReader.read(this, clazz)!!
    }

    fun <U : Any> ResultSet.entities(clazz: KClass<U>): List<U> {
        val items = mutableListOf<U>()

        while (next()) {
            items.add(entity(clazz))
        }

        return items
    }

    inline fun <reified U : Any> ResultSet.entities(): List<U> = entities(U::class)
    inline fun <reified U : Any> ResultSet.entity(): U = entity(U::class)
}

