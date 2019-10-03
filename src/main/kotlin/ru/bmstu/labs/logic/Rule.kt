package ru.bmstu.labs.logic

data class Rule(
        val declaration: RuleDeclaration,
        val argument: Predicate? = null
)