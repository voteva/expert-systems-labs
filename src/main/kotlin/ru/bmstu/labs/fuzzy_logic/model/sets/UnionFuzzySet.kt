package ru.bmstu.labs.fuzzy_logic.model.sets

data class UnionFuzzySet(
        val fuzzySets: MutableList<FuzzySet> = mutableListOf()
) : FuzzySet {

    override fun getValue(x: Double): Double {
        return getMaxValue(x)
    }

    fun addFuzzySet(fuzzySet: FuzzySet) {
        fuzzySets.add(fuzzySet)
    }

    private fun getMaxValue(x: Double): Double {
        return fuzzySets.stream()
                .map { it.getValue(x) }
                .max(Double::compareTo)
                .orElse(0.0)
    }
}