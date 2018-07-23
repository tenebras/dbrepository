package com.github.tenebras.dbrepository.alternative

import com.github.tenebras.dbrepository.ColumnType.JSON
import org.junit.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.ZonedDateTime
import java.util.*

class NewRepositoryTest {

    @Test
    fun `it should insert`() {
        val tests = Tests(connection())
        val item = TestItem(
            value = "statement",
            uuid = UUID.randomUUID(),
            arrayOfJsonObjects = arrayOf(
                Comment(
                    value = "some useful comment",
                    authorId = "author_id",
                    at = ZonedDateTime.now()
                )
            )
        )

        //tests.add(item)
        val entry = tests.find(1)



//    tests.where {
//        TestItem::arrayOfJsonObjects eq "" or (TestItem::statement inside listOf(""))
//    }
    }
}


class Tests(conn: Connection) : NewRepository<TestItem>(conn, DefaultEntityReader(), TableInfo("test", TestItem::id)) {

    init {
        columnType(
            TestItem::arrayOfJsonObjects to JSON
        )
    }

    fun update(item: TestItem) = query {
        "update $table set array_of_json_objects=${bind(item.arrayOfJsonObjects)} where id=${bind(item.id)}"
    }

    fun find(id: Int) = query { "select * from $table where id=${bind(id)} limit 1" }
}

data class TestItem(
    val value: String,
    val arrayOfJsonObjects: Array<Comment>,
    //val intArray: Array<Int>,
    val uuid: UUID,
    //val map: Map<String, String>
    val id: Int? = null
)

data class Comment(val value: String, val authorId: String, val at: ZonedDateTime)

private fun connection() = DriverManager.getConnection("jdbc:postgresql://localhost/dbrepository")