package com.github.tenebras.dbrepository.gson

import com.github.tenebras.dbrepository.extension.registerTypeAdapter
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime

fun gson() = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(ZonedDateTime::class, ZonedDateTimeTypeAdapter())
        .registerTypeAdapter(OffsetDateTime::class, OffsetDateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate::class, LocalDateTypeAdapter())
        .create()