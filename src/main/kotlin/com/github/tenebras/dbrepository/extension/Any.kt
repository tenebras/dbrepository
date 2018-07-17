package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.gson.gson
import kotlin.reflect.full.declaredMemberProperties

fun Any?.toJson(exclude: List<String> = emptyList()): String =
        if (exclude.isEmpty()) {
            gson().toJson(this)
        } else {
            gson().toJson(this.toMap(exclude))
        }

fun Any?.toJson(extension: Map<String, Any>, exclude: List<String> = emptyList()): String = gson().toJson(this.toMap(exclude) + extension)

fun Any?.toJson(extension: Any, exclude: List<String> = emptyList()): String = gson().toJson(this.toMap(exclude) + extension.toMap())

fun Any?.toMap(exclude: List<String> = emptyList()): Map<String, Any?> {
    if (this == null) {
        return emptyMap()
    }

    val properties = mutableMapOf<String, Any?>()

    this::class.declaredMemberProperties.forEach {
        if (!exclude.contains(it.name)) {
            properties[it.name] = it.getter.call(this)
        }
    }

    return properties
}