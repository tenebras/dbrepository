package com.github.tenebras.dbrepository

import com.github.tenebras.dbrepository.extension.toSnakeCase
import kotlin.reflect.KProperty1

class FindContions<T>() {

    class Condition<T>(val property: Any, val operator: Operator, val value: Any?) {
        infix fun and(condition: Condition<T>) = Condition<T>(this, Operator.AND, condition)
        infix fun or(condition: Condition<T>) = Condition<T>(this, Operator.OR, condition)
    }

    enum class Operator(val presentation: String) {
        EQUALS("="),
        GREETER_THEN(">"),
        LESS_THEN("<"),
        LIKE("like"),
        ONE_OF("in"),
        AND("and"),
        OR("or")
    }

    infix fun KProperty1<T, Any>.eq(subject: Any?) = Condition<T>(this.name.toSnakeCase(), Operator.EQUALS, subject)
    infix fun KProperty1<T, Any>.gt(subject: Any?) = Condition<T>(this.name.toSnakeCase(), Operator.GREETER_THEN, subject)
    infix fun KProperty1<T, Any>.lt(subject: Any?) = Condition<T>(this.name.toSnakeCase(), Operator.LESS_THEN, subject)
    infix fun KProperty1<T, Any>.inside(subject: List<Any>) = Condition<T>(this.name.toSnakeCase(), Operator.ONE_OF, subject)
    infix fun KProperty1<T, Any>.between(subject: ClosedRange<*>) = Condition<T>(this.name.toSnakeCase(), Operator.ONE_OF, subject)
}