package com.github.tenebras.dbrepository.extension

import com.google.gson.GsonBuilder
import kotlin.reflect.KClass

fun GsonBuilder.registerTypeAdapter(klass: KClass<*>, typeAdapter: Any) = registerTypeAdapter(klass.java, typeAdapter)