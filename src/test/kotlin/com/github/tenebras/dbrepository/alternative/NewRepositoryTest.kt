//package com.github.tenebras.dbrepository.alternative
//
//import com.github.tenebras.dbrepository.ColumnType
//import com.github.tenebras.dbrepository.ColumnType.JSON
//import com.github.tenebras.dbrepository.DefaultEntityReader
//import com.github.tenebras.dbrepository.TableInfo

//
//import java.time.ZonedDateTime
//import java.util.*
//
//class NewRepositoryTest {
//
//    @Test
//    fun `it should insert`() {
//        val tests = Tests(connection())
//        val item = TestItem(
//            value = "statement",
//            uuid = UUID.randomUUID(),
//            arrayOfJsonObjects = arrayOf(
//                Comment(
//                    value = "some useful comment",
//                    authorId = "author_id",
//                    at = ZonedDateTime.now()
//                )
//            )
//        )
//
//        //tests.add(item)
//        //val entry = tests.find(3)
//
//        val types = TypesRepository(connection())
//        val entry = types.first()
//
//
//
//        types.add(entry.toMap(extension = mapOf(
//            "hstore" to mapOf("b" to 2, "c" to 3)
//        )))
//
//        println(entry)
//
////    tests.where {
////        TestItem::arrayOfJsonObjects eq "" or (TestItem::statement inside listOf(""))
////    }
//    }
//}
//
//
//class Tests(conn: Connection) : NewRepository<TestItem>(conn, DefaultEntityReader(), TableInfo("test", TestItem::id)) {
//
//    init {
//        columnType(
//            TestItem::arrayOfJsonObjects to JSON
//        )
//    }
//
//    fun update(item: TestItem) = query {
//        "update $table set array_of_json_objects=${bind(item.arrayOfJsonObjects)} where id=${bind(item.id)}"
//    }
//
//    fun find(id: Int) = query { "select * from $table where id=${bind(id)} limit 1" }
//}
//
//data class TestItem(
//    val value: String,
//    val arrayOfJsonObjects: Array<Comment>,
//    //val intArray: Array<Int>,
//    val uuid: UUID?,
//    //val map: Map<String, String>
//    val id: Int? = null
//)
//
//data class Comment(val value: String, val authorId: String, val at: ZonedDateTime)
//
//class TypesRepository(conn: Connection) : NewRepository<Types>(conn, DefaultEntityReader(), TableInfo("types", Types::bBoolean)) {
//
//    init {
//        columnType(
//            Types::json to JSON,
//            Types::jsonb to ColumnType.JSONB
//        )
//    }
//
//    fun first() = query { "select * from $table limit 1" }
//}
