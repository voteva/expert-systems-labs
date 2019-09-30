package ru.bmstu.labs.logic

data class Rule<T>(
        val head: Predicate<T>,
        val body: List<Predicate<T>>
)