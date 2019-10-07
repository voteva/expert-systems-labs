package ru.bmstu.labs.fuzzy_logic.model.road

import ru.bmstu.labs.fuzzy_logic.model.sets.FuzzySet

data class RoadFuzzySet(
        val intervals: List<Int>
) : FuzzySet {

    override fun getValue(x: Double): Double {
        return if (x < intervals[0]) {
            0.0
        } else if (intervals[0] < x && intervals[1] > x) {
            (x - intervals[0]) / (intervals[1] - intervals[0])
        } else if (intervals[1] <= x && intervals[2] >= x) {
            1.0
        } else if (intervals[2] < x && intervals[3] > x) {
            (intervals[3] - x) / (intervals[3] - intervals[2])
        } else {
            0.0
        }
    }
}