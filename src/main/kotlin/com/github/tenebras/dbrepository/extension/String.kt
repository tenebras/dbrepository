package com.github.tenebras.dbrepository.extension

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