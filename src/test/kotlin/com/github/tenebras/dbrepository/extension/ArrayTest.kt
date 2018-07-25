package com.github.tenebras.dbrepository.extension

import org.junit.Test
import java.time.ZonedDateTime
import java.util.*

class ArrayTest {
    @Test
    fun `it should extend array element`() {
        val comments = arrayOf(
            AnyTest.Comment(UUID.randomUUID(), "value1", "authorId1", ZonedDateTime.now()),
            AnyTest.Comment(UUID.randomUUID(), "value2", "authorId2", ZonedDateTime.now())
        )

        val json = comments.toJson( exclude = listOf("authorId")) {
            mapOf(
                "link" to "http://example.com/comments/${it.id}",
                "author" to it.authorId
            )
        }

        println(json)
    }
}