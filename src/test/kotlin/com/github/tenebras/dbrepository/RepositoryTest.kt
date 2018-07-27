package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.fixture.Comment
import com.github.tenebras.dbrepository.fixture.Status
import com.github.tenebras.dbrepository.fixture.TestItem
import com.github.tenebras.dbrepository.fixture.connection
import org.junit.Test
import java.time.ZonedDateTime

class RepositoryTest {

    @Test
    fun `it should read array of objects`() {


        val items = object : Repository<TestItem>(connection(), DefaultEntityReader(), TableInfo("test", TestItem::id)) {
            init {
                columnType(
                    TestItem::arrayOfJsonObjects to ColumnType.JSONB
                )
            }

            fun first() = query { "select * from $table limit 1 " }
            fun allByAuthorId(authorId: String) = queryList { "select * from $table where author_id=${bind(authorId)}" }

            fun allValues() = queryListOf<String> { "select value from $table" }

        }

        items.update(TestItem(1, "updated value", arrayOf(), Status.NO))

        //println(items.first())

        // val item = repository.allValues()
        val item = items.first()
        println(item)
    }

    @Test
    fun `it should store array`() {
        val repository = object : Repository<TestItem>(connection(), DefaultEntityReader(), TableInfo("test", TestItem::id)) {

            init {
                columnType(
                    TestItem::arrayOfJsonObjects to ColumnType.JSONB
                )
            }
        }

        val item = TestItem(0, "some statement", arrayOf(Comment("comment statement", "autor_id", ZonedDateTime.now())), Status.NO)

        repository.add(item)
    }
}
