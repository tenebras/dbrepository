package com.github.tenebras.dbrepository

enum class ColumnType(val typeName: String) {
    AUTO(""),
    JSONB("jsonb"),
    JSON("json"),
    CUSTOM("custom"),
    UUID("uuid");
}