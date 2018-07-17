package com.github.tenebras.dbrepository

import org.junit.Test
import java.sql.DriverManager
import java.time.ZonedDateTime

class RepositoryTest {

    @Test
    fun `it should read array of objects`() {

//        val comments = "[{\"value\": \"test comment\", \"at\":\"2018-07-16T11:13:16Z\"}]".parseJson<Array<Comment>>()
//
//        println(comments)

        val repository = object : Repository("test", DriverManager.getConnection("jdbc:postgresql://localhost/dbrepository"), DbValueReader()) {
            fun first() = query<TestItem> { "select * from test" }
        }

        val item = repository.first()
        println(1)
    }


    data class TestItem(
            val id: Int,
            val value: String,
            val arrayOfJsonObjects: Array<Comment>
            //val intArray: Array<Int>,
            //val uuid: UUID,
            //val map: Map<String, String>
    )
    data class Comment(val value: String, val authorId: String, val at: ZonedDateTime)

}

