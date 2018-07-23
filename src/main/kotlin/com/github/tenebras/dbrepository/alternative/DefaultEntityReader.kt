package com.github.tenebras.dbrepository.alternative

import java.sql.ResultSet
import kotlin.reflect.KClass

class DefaultEntityReader: EntityReader {

    override fun <T : Any> read(rs: ResultSet, clazz: KClass<T>): T {
        val constructor = clazz.constructors.first()
        val params = constructor.parameters.map {
            // valueReader.read(this, it)
        }

        return constructor.call(*params.toTypedArray())
    }
}