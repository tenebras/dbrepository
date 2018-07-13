package com.github.tenebras.dbrepository

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class WrappedPreparedStatement(connection: Connection, val sql: String) {

    val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
    var nextIdx = 0
        get() = ++field

    fun bind(array: Array<*>, type: String = ""): WrappedPreparedStatement {
        var arrayType = type

        if (arrayType.isEmpty()) {
            if (array.isNotEmpty()) {
                arrayType = array.first()!!.javaClass.simpleName.toLowerCase()
            } else {
                throw IllegalArgumentException("Missing type for empty array")
            }
        }

        preparedStatement.setArray(nextIdx, preparedStatement.connection.createArrayOf(arrayType, array))

        return this
    }

    fun bind(enum: Enum<*>): WrappedPreparedStatement {
        preparedStatement.setObject(nextIdx, enum.name, java.sql.Types.OTHER)
        return this
    }

    fun bind(value: Any?, type: String = ""): WrappedPreparedStatement {

        when (value) {
            is SQLStatement.TypedBinding -> bind(value.value, value.type)
            is Enum<*> -> bind(value)
            is List<*> -> bind(value, type)
            is Array<*> -> bind(value, type)
            else -> preparedStatement.setObject(nextIdx, value)
        }

        return this
    }

    fun bindMultiple(params: Collection<Any?>) = bindMultiple(params.toList())

    fun bindMultiple(params: List<Any?>): WrappedPreparedStatement {
        params.forEach { bind(it) }

        return this
    }

    fun bind(list: List<*>, type: String = "")
        = bind(list.toTypedArray(), type)

    fun bind(value: SQLStatement.TypedBinding)
        = bind(value.value, value.type)

    fun execute() = preparedStatement.execute()
    fun executeQuery(): ResultSet = preparedStatement.executeQuery()
}