package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.fixture.*
import org.junit.Test
import java.sql.Connection
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

    @Test
    fun `it should read complex type`() {
        val types = TypesRepository(connection())

        types.update(types.first())

        types.queryMap( { it.cVarchar to it} ) {""}
        types.queryMap(Types::cVarchar) {""}
    }
}


class TypesRepository(conn: Connection) : Repository<Types>(conn, DefaultEntityReader(), Types::bBoolean) {

    init {
        columnType(
            Types::json to ColumnType.JSON,
            Types::jsonb to ColumnType.JSONB
        )
    }

    fun first() = query { "select * from $table limit 1" }
}