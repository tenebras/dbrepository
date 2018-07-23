//package com.github.tenebras.dbrepository
//
//import org.junit.Test
//import java.sql.DriverManager
//import java.time.ZonedDateTime
//
//class RepositoryTest {
//
//    @Test
//    fun `it should read array of objects`() {
//
////        val comments = "[{\"statement\": \"test comment\", \"at\":\"2018-07-16T11:13:16Z\"}]".parseJson<Array<Comment>>()
////
////        println(comments)
//
//        val repository = object : Repository("test", DriverManager.getConnection("jdbc:postgresql://localhost/dbrepository"), ValueReader()) {
//            fun first() = query<TestItem> { "select * from test" }
//        }
//
//        val item = repository.first()
//        println(1)
//    }
//
//    @Test
//    fun `it should store array`() {
//        val repository = object : Repository("test", DriverManager.getConnection("jdbc:postgresql://localhost/dbrepository"), ValueReader()) {
//
//            init {
//                overrideColumnType(
//                    TestItem::arrayOfJsonObjects to ColumnType.JSONB
//                )
//            }
//        }
//
//        val item = TestItem(0, "some statement", arrayOf(Comment("comment statement", "autor_id", ZonedDateTime.now())))
//
//        repository.insert(item, listOf("id"))
//    }
//
//    data class TestItem(
//            val id: Int,
//            val value: String,
//            @JsonColumn val arrayOfJsonObjects: Array<Comment>
//            //val intArray: Array<Int>,
//            //val uuid: UUID,
//            //val map: Map<String, String>
//    )
//    data class Comment(val value: String, val authorId: String, val at: ZonedDateTime)
//
//    annotation class JsonColumn()
//}
//
