package ru.bmstu.labs.logical_inference.model

enum class RuleDeclaration(rule: String) {
    RULE_PARTITION("f(x,y)>f(x,z)&f(z,y)"),
    RULE_SWAP("f(x,y)>f(y,x)"),
    RULE_NO_PATH("f(x,y)>null")
}