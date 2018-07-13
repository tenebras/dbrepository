package com.github.tenebras.dbrepository

class SQLStatement(init: SQLStatement.() -> String) {

    data class TypedBinding(val value: Any?, val type: String)

    val params = mutableListOf<Any?>()
    var sql: String = init()

    fun bind(param: Any?, type: String = ""): String {

        param.isComplexValue()

        if (type.isNotEmpty()) {
            params.add(TypedBinding(param, type))
        } else {
            params.add(param)
        }

        return "?"
    }

    inline fun Any?.isComplexValue() {

    }
}