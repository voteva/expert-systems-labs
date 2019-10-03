package ru.bmstu.labs.fuzzy_logic.model

import ru.bmstu.labs.fuzzy_logic.model.sets.FuzzySet

data class Conclusion(
        override val name: String,
        override val variable: Variable,
        override val term: FuzzySet,
        val weight: Double
) : Statement