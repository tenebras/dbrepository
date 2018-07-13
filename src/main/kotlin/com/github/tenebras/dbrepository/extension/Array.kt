package com.github.tenebras.dbrepository.extension

import java.util.concurrent.ThreadLocalRandom

fun <T> Array<T>.random(): T = this[ThreadLocalRandom.current().nextInt(0, size)]