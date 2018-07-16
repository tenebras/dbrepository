package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.gson.gson

fun Any?.toJson(): String = gson().toJson(this)