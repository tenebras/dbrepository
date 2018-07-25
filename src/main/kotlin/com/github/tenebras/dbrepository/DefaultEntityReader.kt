package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.extension.*
import org.postgresql.jdbc.PgArray
import org.postgresql.util.PGobject
import java.math.BigInteger
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.reflect.KClass

class DefaultEntityReader : EntityReader {

    val valueReader = mutableMapOf<KClass<*>, (ResultSet, String) -> Any?>()

    init {
        registerValueReader(Byte::class, ResultSet::getByte)
        registerValueReader(Short::class, ResultSet::getShort)
        registerValueReader(Int::class, ResultSet::getInt)
        registerValueReader(Long::class, ResultSet::getLong)
        registerValueReader(Float::class, ResultSet::getFloat)
        registerValueReader(Double::class, ResultSet::getDouble)
        registerValueReader(BigInteger::class) {rs, name -> BigInteger(rs.getString(name))}

        registerValueReader(LocalDate::class, ResultSet::getLocalDate)
        registerValueReader(ZonedDateTime::class, ResultSet::getZonedDateTime)
        registerValueReader(OffsetDateTime::class, ResultSet::getOffsetDateTime)

        registerValueReader(Char::class, ResultSet::getChar)

        registerValueReader(Array<OffsetDateTime>::class) { rs, name ->
            (rs.getArray(name).array as Array<Timestamp>).map{
                OffsetDateTime.ofInstant(it.toInstant(), ZoneId.of("UTC"))
            }.toTypedArray()
        }

        registerValueReader(Array<ZonedDateTime>::class) { rs, name ->
            (rs.getArray(name).array as Array<Timestamp>).map{
                ZonedDateTime.ofInstant(it.toInstant(), ZoneId.of("UTC"))
            }.toTypedArray()
        }

        registerValueReader(CharArray::class) {rs, name ->
            (rs.getArray(name).array as Array<String>).map { it[0] }.toCharArray()
        }
    }

    // class Reader<T: Any>(val clazz: KClass<T>, val mapper: (ResultSet, String) -> T?)

    fun <T : Any> registerValueReader(clazz: KClass<T>, mapper: (ResultSet, String) -> T?) {
        valueReader[clazz] = mapper
    }

    fun hasValueReaderFor(clazz: KClass<*>) = valueReader.contains(clazz)

    fun reader(clazz: KClass<*>) = valueReader[clazz]

    override fun <T : Any> read(rs: ResultSet, clazz: KClass<T>): T {
        val constructor = clazz.constructors.first()
        val params = constructor.parameters.map {
            // valueReader.read(this, it)
            val name = it.name!!.toSnakeCase()
            val type = it.type
            val paramClazz = (it.type.classifier as KClass<*>)

            val obj = if (hasValueReaderFor(paramClazz))
                reader(paramClazz)!!.invoke(rs, name)
            else
                rs.getObject(name)

            if (name == "hstore") {
                println(obj)
            }

            when (obj) {
                is PGobject -> when (obj.type) {
                    ColumnType.JSON.typeName, ColumnType.JSONB.typeName ->
                        obj.value.parseJson((type.classifier as KClass<*>).java)
                    else -> {
                        obj
                    }
                }
                is PgArray -> obj.array
                else -> obj
            }
        }

        return constructor.call(*params.toTypedArray())
    }
}