package ru.bmstu.labs.fuzzy_logic.model

import ru.bmstu.labs.fuzzy_logic.model.sets.FuzzySet

data class Condition(
        override val name: String,
        override val variable: Variable,
        override val term: FuzzySet
): Statement