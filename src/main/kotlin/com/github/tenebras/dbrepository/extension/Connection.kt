package com.github.tenebras.dbrepository.extension

import com.github.tenebras.dbrepository.SQLStatement
import com.github.tenebras.dbrepository.WrappedPreparedStatement
import java.sql.Connection

fun Connection.wrappedStatement(sql: String)
    = WrappedPreparedStatement(this, sql)

fun Connection.wrappedStatement(sql: String, params: Collection<Any?>)
    = WrappedPreparedStatement(this, sql).bindMultiple(params)

fun Connection.wrappedStatement(sqlStatement: SQLStatement)
    = WrappedPreparedStatement(this, sqlStatement.sql)
        .bindMultiple(sqlStatement.params)