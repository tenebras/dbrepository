package com.github.tenebras.dbrepository.alternative

import com.github.tenebras.dbrepository.ColumnType

class Sql(private val columnTypes: Map<String, ColumnType>, init: Sql.() -> String) {

    companion object {
        fun insert(table: String, params: Map<String, Any?>, columnTypes: Map<String, ColumnType> = emptyMap()): Sql {

            if (table.isBlank()) {
                throw IllegalArgumentException("Table name in ${Sql::class} could not be empty")
            }

            if (params.isEmpty()) {
                throw IllegalArgumentException("Missing data to add in ${Sql::class}")
            }

            return Sql(columnTypes) {
                "insert into $table (${params.keys.joinToString(", ")}) values (${bindMultiple(params)})"
            }
        }

//        fun update(table: String, params: Map<String, Any?>, columnTypes: Map<String, ColumnType> = emptyMap()): Sql {
//            return Sql {
//                "update $table set ??? where ??"
//            }
//        }
    }

    val values = mutableListOf<TypedBinding>()
    val statement: String = init()

    fun bind(value: Any?, type: ColumnType = ColumnType.AUTO): Char {
        values.add(TypedBinding(value, type))

        return '?'
    }

    fun bindMultiple(valuesMap: Map<String, Any?>): String {
        valuesMap.forEach {
            values.add(TypedBinding(it.value, columnTypes.getOrDefault(it.key, ColumnType.AUTO)))
        }

        return "?, ".repeat(valuesMap.keys.size).dropLast(2)
    }

    data class TypedBinding(val value: Any?, val type: ColumnType)
}