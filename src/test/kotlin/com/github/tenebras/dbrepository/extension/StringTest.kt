package com.github.tenebras.dbrepository.extension

import org.junit.Assert
import org.junit.Test

class StringTest {

    @Test
    fun `it should convert to snakeCase`() {
        val upperCaseString = "UpperCaseString"
        val snakeCaseString = "upper_case_string"

        Assert.assertEquals(snakeCaseString, upperCaseString.toSnakeCase())
    }
}