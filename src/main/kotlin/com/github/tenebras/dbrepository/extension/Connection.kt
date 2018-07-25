package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.Sql
import java.sql.Connection

fun Connection.prepare(sql: Sql) = this.prepareStatement(sql.statement).bindMultiple(sql.values)
fun Connection.query(sql: Sql) = prepare(sql).executeQuery()
fun Connection.execute(sql: Sql) = prepare(sql).execute()