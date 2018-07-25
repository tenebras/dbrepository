package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.fixture.Comment
import com.github.tenebras.dbrepository.fixture.TestItem
import com.github.tenebras.dbrepository.fixture.connection
import org.junit.Test
import java.time.ZonedDateTime

class RepositoryTest {

    @Test
    fun `it should read array of objects`() {


        val repository = object : Repository<TestItem>(connection(), DefaultEntityReader(), TableInfo("test", TestItem::id)) {
            fun first() = query { "select * from $table limit 1 " }
            fun allByAuthorId(authrorId: String) = queryAll { "select * from $table where author_id=${bind(authrorId)}" }
        }

        val item = repository.first()
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

        val item = TestItem(0, "some statement", arrayOf(Comment("comment statement", "autor_id", ZonedDateTime.now())))

        repository.add(item)
    }
}
