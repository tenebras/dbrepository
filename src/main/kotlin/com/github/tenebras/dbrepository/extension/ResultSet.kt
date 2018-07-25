package com.github.tenebras.dbrepository.extension

import java.sql.ResultSet
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.*

fun ResultSet.getUUID(columnLabel: String): UUID? {
    val value = getString(columnLabel)
    return if (value !== null) UUID.fromString(value) else null
}

fun ResultSet.getLocalDate(columnLabel: String): LocalDate? = getDate(columnLabel)?.toLocalDate()
fun ResultSet.getZonedDateTime(columnLabel: String): ZonedDateTime? = getObject(columnLabel, OffsetDateTime::class.java)?.toZonedDateTime()
fun ResultSet.getOffsetDateTime(columnLabel: String): OffsetDateTime? = getObject(columnLabel, OffsetDateTime::class.java)
fun ResultSet.getChar(columnLabel: String) = getString(columnLabel)[0]