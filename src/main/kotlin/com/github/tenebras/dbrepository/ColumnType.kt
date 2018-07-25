package com.github.tenebras.dbrepository

import kotlin.reflect.KClass

enum class ColumnType(val typeName: String) {
    AUTO(""),
    JSONB("jsonb"),
    JSON("json"),
    CUSTOM("custom"),
    UUID("uuid"),
    BIGINT("bigint"),
    INTEGER("integer"),
    CHAR("char"),
    TIMESTAMP("timestamp"),
    TIMESTAMPTZ("timestamptz");

    companion object {
        fun of(clazz: KClass<*>?, default: ColumnType = AUTO): ColumnType {

            if (default == AUTO && clazz !== null) {

                // Oid.toString(Oid.TIMESTAMPTZ)

                return when (clazz.java.componentType.typeName) {
                    "java.lang.Character" -> ColumnType.CHAR
                    "java.lang.Long" -> ColumnType.BIGINT
                    "java.lang.Integer" -> ColumnType.INTEGER
                    "java.time.OffsetDateTime", "java.time.ZonedDateTime" -> ColumnType.TIMESTAMPTZ
                    else -> default
                }
            }

            return default
        }
    }
}