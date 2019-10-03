package ru.bmstu.labs.fuzzy_logic.model.sets

interface FuzzySet {
    fun getValue(x: Double): Double
}