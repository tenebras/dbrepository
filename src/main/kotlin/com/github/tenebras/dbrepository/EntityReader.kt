package com.github.tenebras.dbrepository

import java.sql.ResultSet
import kotlin.reflect.KClass

interface EntityReader {
    fun <T: Any> read(rs: ResultSet, clazz: KClass<T>): T
}