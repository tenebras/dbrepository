package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.gson.LocalDateTypeAdapter
import com.github.tenebras.dbrepository.gson.OffsetDateTimeTypeAdapter
import com.github.tenebras.dbrepository.gson.ZonedDateTimeTypeAdapter
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime

fun Any?.toJson(): String = GsonBuilder()
    .serializeNulls()
    .setPrettyPrinting()
    .registerTypeAdapter(ZonedDateTime::class, ZonedDateTimeTypeAdapter())
    .registerTypeAdapter(OffsetDateTime::class, OffsetDateTimeTypeAdapter())
    .registerTypeAdapter(LocalDate::class, LocalDateTypeAdapter())
    .create()
    .toJson(this)