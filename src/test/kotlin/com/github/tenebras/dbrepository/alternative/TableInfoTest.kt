package com.github.tenebras.dbrepository.alternative

import junit.framework.Assert.assertEquals
import org.junit.Test

class TableInfoTest {

    @Test
    fun `it should store values`() {
        val properties = TableInfo("test", TestItem::class, TestItem::id)

        assertEquals("test", properties.tableName)
        assertEquals(TestItem::class, properties.entity)
        assertEquals(TestItem::id, properties.primaryKey)
    }

    @Test
    fun `it should detect tableName and entity`() {

        val properties = TableInfo(TestItem::id)

        assertEquals("test_item", properties.tableName)
        assertEquals(TestItem::class, properties.entity)
        assertEquals(TestItem::id, properties.primaryKey)
    }

    @Test
    fun `it should detect entity class`(){
        val properties = TableInfo("test", TestItem::id)

        assertEquals("test", properties.tableName)
        assertEquals(TestItem::class, properties.entity)
        assertEquals(TestItem::id, properties.primaryKey)
    }

    @Test
    fun `it should create without primary key`() {
        val properties = TableInfo("test", TestItem::class)

        assertEquals("test", properties.tableName)
        assertEquals(TestItem::class, properties.entity)
        assertEquals(null, properties.primaryKey)
    }
}