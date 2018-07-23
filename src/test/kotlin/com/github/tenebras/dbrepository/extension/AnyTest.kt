package com.github.tenebras.dbrepository.extension

import org.junit.Test
import java.time.ZonedDateTime
import java.util.*

class AnyTest {
    @Test
    fun `it should extend object`() {
        val comment = Comment(UUID.randomUUID(), "statement", "authorId", ZonedDateTime.now())

        val serialized = comment.toJson(object {
            val link = "http://example.com/comments/${comment.id}"
        }, listOf("at"))

        val serializedFromMap = comment.toJson(mapOf(
                "link" to "http://example.com/comments/${comment.id}"
        ), listOf("at"))

        println(serialized)
    }

    @Test
    fun `it should extend array element`() {
        val comments = arrayOf(
                Comment(UUID.randomUUID(), "value1", "authorId1", ZonedDateTime.now()),
                Comment(UUID.randomUUID(), "value2", "authorId2", ZonedDateTime.now())
        )

        val json = comments.toJson(object {
            val link = "http://example.com"
        }, listOf("at", "authorId"))

        println(json)
    }

    data class Comment(val id: UUID, val value: String, val authorId: String, val at: ZonedDateTime)
}