package com.github.tenebras.dbrepository.extension

import java.util.concurrent.ThreadLocalRandom

inline fun <reified T: Enum<*>> random(): T {
    val constants = T::class.javaObjectType.enumConstants
    return constants[ThreadLocalRandom.current().nextInt(0, constants.size)]
}