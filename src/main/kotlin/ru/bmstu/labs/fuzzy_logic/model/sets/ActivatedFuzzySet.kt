package ru.bmstu.labs.fuzzy_logic.model.sets

data class ActivatedFuzzySet(
        val fuzzySet: FuzzySet,
        val truthDegree: Double
) : FuzzySet {

    override fun getValue(x: Double): Double {
        return getActivatedValue(x)
    }

    private fun getActivatedValue(x: Double): Double {
        return Math.min(fuzzySet.getValue(x), truthDegree)
    }
}