package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.ColumnType
import com.github.tenebras.dbrepository.alternative.Sql
import java.sql.PreparedStatement

fun PreparedStatement.bindMultiple(values: List<Sql.TypedBinding>): PreparedStatement {

    values.forEachIndexed { idx, it ->
        when (it.type) {
            ColumnType.JSON, ColumnType.JSONB
                -> setObject(idx + 1, it.value.toJson(), java.sql.Types.OTHER)
            else
                -> when(it.value) {
                    is Enum<*> -> setObject(idx+1, it.value, java.sql.Types.OTHER)
                    is Array<*> ->
                        if (it.value.isNotEmpty()) {
                            setArray(idx+1, this.connection.createArrayOf(it.value.first()!!.javaClass.simpleName.toLowerCase(), it.value))
                        } else {
                            throw IllegalArgumentException("Missing type for empty array")
                        }
                else
                    -> setObject(idx + 1, it.value)
            }
        }
    }

    return this
}