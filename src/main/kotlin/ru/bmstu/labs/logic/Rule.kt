package ru.bmstu.labs.logic

enum class Rule(value: String) {
    RULE1("f(x,y)>f(x,z)&f(z,y)"),
    RULE2("f(x,y)>f(y,x)")
}