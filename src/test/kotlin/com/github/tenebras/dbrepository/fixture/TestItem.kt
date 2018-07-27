package com.github.tenebras.dbrepository.fixture

import org.postgresql.util.PGInterval
import java.math.BigDecimal
import java.sql.DriverManager
import java.sql.Time
import java.time.ZonedDateTime
import java.util.*

enum class Status {
    YES, NO;
}

data class TestItem(
    val id: Int,
    val value: String,
    val arrayOfJsonObjects: Array<Comment>,
    val enum: Status
    //val intArray: Array<Int>,
    //val uuid: UUID,
    //val map: Map<String, String>
)
data class Comment(val value: String, val authorId: String, val at: ZonedDateTime, val id: Int? = null)

data class Types(
    val bBoolean: Boolean,
    val cChar: Char,
    val cChar_fixed: String,
    val cVarchar_limited: String,
    val cVarchar: String,
    val cText: String,
    val nSmallint: Short,
    val nInt: Int,
    val nBigint: Long,
    val nFloat: Float,
    val nReal: Double,
    val nNumeric: BigDecimal,
    val tDate: Date,
    val tTime: Time,
    val tTimestamp: ZonedDateTime,
    val tTimestampz: ZonedDateTime,
    val tInterval: PGInterval,
    val aChar: CharArray,
    val aInt: Array<Int>,
    val aBigint: Array<Long>,
    val aTimestamp: Array<ZonedDateTime>,
    val json: Array<Comment>,
    val jsonb: Array<Comment>,
    val uuid: UUID,
    val hstore: Map<String, String>,
    val sBox: String? = null,
    val sLine: String? = null,
    val sPoint: String? = null,
    val sLseg: String? = null,
    val sInet: String? = null,
    val sMacaddr: String? = null
)

fun connection() = DriverManager.getConnection("jdbc:postgresql://localhost/dbrepository")