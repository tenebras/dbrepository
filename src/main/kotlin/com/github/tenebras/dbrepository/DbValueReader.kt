package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.extension.*
import java.lang.Exception
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaType

open class DbValueReader {
    val resolvers = mutableMapOf<String, (ResultSet, String) -> Any?>()
    val fallbackResolver = { rs: ResultSet, name: String, type: KType ->

        val typeName = type.javaType.typeName
        val columnTypeName = rs.metaData.getColumnTypeName(rs.findColumn(name))

        when {
            columnTypeName == "jsonb" || columnTypeName == "json" ->
                rs.getString(name).parseJson((type.classifier as KClass<*>).java)

            typeName.endsWith("[]") -> rs.getArray(name).array

            type.isSubtypeOf(Enum::class.starProjectedType) -> {

                // TODO: stop on null
                val value = rs.getString(name)
                val constants = (type.classifier as KClass<Enum<*>>).javaObjectType.enumConstants

                constants.firstOrNull { it.name == value }
                        ?: throw Exception("No matching enum constant for class $typeName, value is '$value'")
            }
            else -> {

                val value = rs.getObject(name)

                if ((type.classifier as KClass<*>).isSuperclassOf(rs.getObject(name).javaClass.kotlin)) {
                    value
                } else {
                    val clazz = Class.forName(typeName)
                    val constructor = clazz.kotlin.primaryConstructor

                    if (constructor == null || constructor.parameters.size != 1) {
                        throw Exception("No suitable function for $typeName. Constructor renderer one parameter required.")
                    }

                    constructor.call(read(rs, constructor.parameters.first().type, name))
                }
            }
        }
    }

    init {
        // todo: review is all this required. Maybe getObject will work just fine?
        register(String::class, ResultSet::getString)
        register(Byte::class, ResultSet::getByte)
        register(Short::class, ResultSet::getShort)
        register(Int::class, ResultSet::getInt)
        register(Long::class, ResultSet::getLong)
        register(Float::class, ResultSet::getFloat)
        register(Double::class, ResultSet::getDouble)
        register(Boolean::class, ResultSet::getBoolean)
        register(ZonedDateTime::class, ResultSet::getZonedDateTime)
        register(OffsetDateTime::class, ResultSet::getOffsetDateTime)
        register(Date::class, ResultSet::getDate)
        register(Time::class, ResultSet::getTime)
        register(Timestamp::class, ResultSet::getTimestamp)
        register(LocalDate::class, ResultSet::getLocalDate)
    }

    fun couldBeRead(type: KClass<*>) = resolvers.containsKey(type.qualifiedName!!)


    fun register(type: KClass<*>, resolver: (ResultSet, String) -> Any?): DbValueReader {
        resolvers.put(type.qualifiedName!!, resolver)
        return this
    }

    fun readFirst(rs: ResultSet, type: KClass<*>): Any? {
        return read(rs, type.starProjectedType, rs.metaData.getColumnName(1))
    }

    fun read(rs: ResultSet, param: KParameter): Any? {
        return read(rs, param.type, param.name!!)
    }

    fun read(rs: ResultSet, type: KType, name: String): Any? {

        val typeString = type.toString()
        val isNullable = typeString.endsWith('?')
        val normalizedType = if (isNullable) {
            typeString.substringBeforeLast('?')
        } else {
            typeString
        }

        try {
            return if (resolvers.contains(normalizedType)) {
                resolvers[normalizedType]!!.invoke(rs, name.toSnakeCase())
            } else {
                //try {
                fallbackResolver.invoke(rs, name.toSnakeCase(), type)
                //} catch (e: Exception) {}
            }

        } catch (e: Exception) {
            println("Can't read column $name of type $typeString. Is nullable = $isNullable")

            if (isNullable) {
                return null
            } else {
                throw e
            }
        }
    }
}