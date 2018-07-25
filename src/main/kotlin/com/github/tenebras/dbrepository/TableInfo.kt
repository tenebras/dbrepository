package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.extension.toSnakeCase
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

class TableInfo<T : Any>(
    val tableName: String,
    val entity: KClass<T>,
    val primaryKey: KProperty1<T, *>? = null
) {
    constructor(tableName: String, primaryKey: KProperty1<T, *>) : this(
        tableName,
        primaryKey.javaField!!.declaringClass.kotlin as KClass<T>,
        primaryKey
    )

    constructor(primaryKey: KProperty1<T, *>) : this(
        primaryKey.javaField!!.declaringClass.kotlin.simpleName!!.toSnakeCase(),
        primaryKey.javaField!!.declaringClass.kotlin as KClass<T>,
        primaryKey
    )
}