package ru.bmstu.labs.fuzzy_logic.model

data class Rule(
        val conditions: List<Condition>,
        val conclusions: List<Conclusion>
)