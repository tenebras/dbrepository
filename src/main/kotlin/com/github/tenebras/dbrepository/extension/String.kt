package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.gson.gson

fun String.toSnakeCase(): String {
    var text = ""

    forEachIndexed { idx, char ->
        text += if (char.isUpperCase()) {
            (if (idx != 0) "_" else "") + char.toLowerCase()
        } else {
            char
        }
    }

    return text
}

inline fun <reified T> String.parseJson() = gson().fromJson<T>(this, T::class.java)
fun <T> String.parseJson(clazz: Class<T>) = gson().fromJson<T>(this, clazz)