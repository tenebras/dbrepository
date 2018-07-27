package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.ColumnType
import com.github.tenebras.dbrepository.Sql
import java.sql.PreparedStatement
import java.time.OffsetDateTime
import java.time.ZonedDateTime

// todo: rewrite this with rely on parameter type
fun PreparedStatement.bindMultiple(values: List<Sql.TypedBinding>): PreparedStatement {

    values.forEachIndexed { idx, it ->
        when (it.type) {
            ColumnType.JSON, ColumnType.JSONB
            -> setObject(idx + 1, it.value.toJson(), java.sql.Types.OTHER)
            else
            -> {
                val value = normalizeValue(it.value)

                when (value) {
                    is Enum<*> -> setObject(idx + 1, value, java.sql.Types.OTHER)
                    is Array<*> -> {
                        val type = ColumnType.of(value::class, it.type)

                        when {
                            type != ColumnType.AUTO -> setArray(idx + 1, this.connection.createArrayOf(type.typeName, value))
                            value.isNotEmpty() -> setArray(idx + 1, this.connection.createArrayOf(ColumnType.of(value.first()!!.javaClass.kotlin).typeName, value))
                            else -> // todo try to set empty array string
                                throw IllegalArgumentException("Missing type for empty array")
                        }
                    }
                    else
                    ->
                        if (columnTypeName(idx + 1).isStringType()) {
                            setObject(idx + 1, value.toString())
                        } else {
                            setObject(idx + 1, value)
                        }
                }
            }
        }
    }

    return this
}

fun PreparedStatement.columnTypeName(idx: Int): String {
    return parameterMetaData.getParameterTypeName(idx)
}

fun String.isStringType(): Boolean {
    return arrayOf("char", "varchar", "text").contains(this)
}

private fun normalizeValue(value: Any?): Any? =
    when (value) {
        is CharArray -> value.toTypedArray()
        is ZonedDateTime -> value.toOffsetDateTime()
//        is List<*> -> value.map { normalizeValue(it) }
        is Array<*> ->
            if (value.isArrayOf<ZonedDateTime>())
                value.map { normalizeValue(it) as OffsetDateTime }.toTypedArray()
            else value
        else -> value
    }
