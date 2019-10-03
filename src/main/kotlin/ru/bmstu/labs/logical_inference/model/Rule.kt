package ru.bmstu.labs.logical_inference.model

data class Rule(
        val declaration: RuleDeclaration,
        val argument: Predicate? = null
)