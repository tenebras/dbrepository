package com.github.tenebras.dbrepository

import kotlin.reflect.KProperty1

class FindContions<T>() {

    class Condition<T>(val property: Any, val operator: Operator, val value: Any?) {
        infix fun and(condition: Condition<T>) = Condition<T>(this, Operator.AND, condition)
        infix fun or(condition: Condition<T>) = Condition<T>(this, Operator.OR, condition)
    }

    enum class Operator {
        EQUALS, GREETER_THEN, LESS_THEN, CONTAINS, ONE_OF,
        AND, OR
    }

    infix fun KProperty1<T, Any>.eq(subject: Any?) = Condition<T>(this, Operator.EQUALS, subject)

    infix fun KProperty1<T, Any>.gt(subject: Any?) = Condition<T>(this, Operator.GREETER_THEN, subject)

    infix fun KProperty1<T, Any>.lt(subject: Any?) = Condition<T>(this, Operator.LESS_THEN, subject)
    infix fun KProperty1<T, Any>.inside(subject: List<Any>) = Condition<T>(this, Operator.LESS_THEN, subject)
}