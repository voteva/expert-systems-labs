package ru.bmstu.labs.fuzzy_logic.model

import ru.bmstu.labs.fuzzy_logic.model.sets.FuzzySet

interface Statement {
    val name: String
    val variable: Variable
    val term: FuzzySet
}