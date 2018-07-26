package com.github.tenebras.dbrepository


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

        fun update(
            table: String,
            primaryKeyName: String,
            params: Map<String, Any?>,
            columnTypes: Map<String, ColumnType> = emptyMap()
        ): Sql {

            if (table.isBlank()) {
                throw IllegalArgumentException("Table name in ${Sql::class} could not be empty")
            }

            if (primaryKeyName.isBlank()) {
                throw IllegalArgumentException("PrimaryKey in ${Sql::class} could not be empty")
            }

            if (params.isEmpty()) {
                throw IllegalArgumentException("Missing data to update in ${Sql::class}")
            }

            return Sql(columnTypes) {
                "update $table set " + params.filterKeys { it != primaryKeyName }.map { bind(it, columnTypes) } .joinToString( ", ") + " where $primaryKeyName=${bind(params[primaryKeyName])}"
            }
        }

//        fun find(table: String, contions: FindContions.Condition<*>): Sql? {
//            println("select * from $table where ${unbox(contions)}")
//            return null
//        }


//        private fun unbox(condition: FindContions.Condition<*>): String {
//            return (if (condition.property is FindContions.Condition<*>) unbox(condition.property) else condition.property as String) + ' ' + condition.operator.presentation + ' ' + (if (condition.value is FindContions.Condition<*>) unbox(condition.value) else condition.value)
//        }
    }

    val values = mutableListOf<TypedBinding>()
    val statement: String = init()

    fun bind(entry: Map.Entry<String, Any?>, columnTypes: Map<String, ColumnType> = emptyMap()): String {
        return entry.key + '=' + bind(entry.value, columnTypes.getOrDefault(entry.key, ColumnType.AUTO))
    }

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