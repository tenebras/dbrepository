package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.extension.toSnakeCase
import com.github.tenebras.dbrepository.extension.wrappedStatement
import java.sql.Connection
import java.sql.ResultSet
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

open class Repository(val table: String, val connection: Connection, val valueReader: DbValueReader) {

    @Throws(EntityNotFound::class)
    inline fun <reified T> query(noinline stmtInit: SQLStatement.() -> String): T {

        val rs = connection.wrappedStatement(SQLStatement(stmtInit)).executeQuery()

        if (!rs.isBeforeFirst) {
            throw EntityNotFound()
        }

        return rs.entity()
    }

    inline fun <reified T> queryAll(noinline stmtInit: SQLStatement.() -> String): Array<T> {
        return connection.wrappedStatement(SQLStatement(stmtInit)).executeQuery().entities()
    }

    fun exec(stmtInit: SQLStatement.() -> String) = connection.wrappedStatement(SQLStatement(stmtInit)).execute()

    fun insert(model: Any, ignored: List<String> = emptyList()) {

        val clazz = model::class
        val params = mutableMapOf<String, Any?>()

        clazz.memberProperties.forEach {
            if (!ignored.contains(it.name)) {

                val returnType = it.returnType.javaType as Class<*>
                val value = it.call(model)

                if (returnType.isArray) {
                    val arrayType = returnType.simpleName.dropLast(2).toLowerCase()

                    params.put(it.name.toSnakeCase(), SQLStatement.TypedBinding(value, arrayType))
                } else {
                    params.put(it.name.toSnakeCase(), value)
                }

            }
        }

        insert(params)
    }

    fun insert(params: Map<String, Any?>) {

        val sql = "insert into $table " +
            "(${params.keys.joinToString(", ")}) " +
            "VALUES(${"?, ".repeat(params.keys.size).dropLast(2)})"

        connection.wrappedStatement(sql, params.values).execute()
    }

    inline fun <reified T> ResultSet.entity(): T {

        if (isBeforeFirst) {
            next()
        }

        val constructor = T::class.constructors.first()
        val params = constructor.parameters.map {
            valueReader.read(this, it)
        }

        return constructor.call(*params.toTypedArray())
    }

    inline fun <reified T> ResultSet.entities(): Array<T> {
        val items = mutableListOf<T>()
        val constructor = T::class.constructors.first()

        while (next()) {

            if (valueReader.couldBeRead(T::class)) {
                items.add(valueReader.readFirst(this, T::class) as T)
            } else {
                val params = constructor.parameters.map {
                    valueReader.read(this, it)
                }

                items.add(constructor.call(*params.toTypedArray()))
            }
        }

        return items.toTypedArray()
    }
}