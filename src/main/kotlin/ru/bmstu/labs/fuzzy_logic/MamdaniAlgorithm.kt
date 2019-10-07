package ru.bmstu.labs.fuzzy_logic

import ru.bmstu.labs.fuzzy_logic.model.Rule
import ru.bmstu.labs.fuzzy_logic.model.sets.ActivatedFuzzySet
import ru.bmstu.labs.fuzzy_logic.model.sets.FuzzySet
import ru.bmstu.labs.fuzzy_logic.model.sets.UnionFuzzySet
import kotlin.math.min

class MamdaniAlgorithm(
        val rules: List<Rule>,
        val inputData: Array<Double>
) {

    fun run(): List<Double> {
        val fuzzificated: List<Double> = fuzzification(inputData)
        val aggregated: List<Double> = aggregation(fuzzificated)
        val activated: List<ActivatedFuzzySet> = activation(aggregated)
        val accumulated: List<UnionFuzzySet> = accumulation(activated)
        return defuzzification(accumulated)
    }

    private fun fuzzification(inputData: Array<Double>): List<Double> {
        val fuzzificatedList: MutableList<Double> = mutableListOf()

        for (rule in rules) {
            for (condition in rule.conditions) {
                val conditionIndex = condition.variable.index
                val term = condition.term
                fuzzificatedList.add(term.getValue(inputData[conditionIndex]))
            }
        }
        return fuzzificatedList
    }

    private fun aggregation(fuzzificatedList: List<Double>): List<Double> {
        val aggregatedList: MutableList<Double> = mutableListOf()
        var index = 0

        for (rule in rules) {
            var truthOfConditions = 1.0
            for (condition in rule.conditions) {
                truthOfConditions = min(truthOfConditions, fuzzificatedList[index++])
            }
            aggregatedList.add(truthOfConditions)
        }
        return aggregatedList
    }

    private fun activation(aggregatedList: List<Double>): List<ActivatedFuzzySet> {
        val activatedList: MutableList<ActivatedFuzzySet> = mutableListOf()
        var index = 0

        for (rule in rules) {
            for (conclusion in rule.conclusions) {
                activatedList.add(ActivatedFuzzySet(conclusion.term, aggregatedList[index++] * conclusion.weight))
            }
        }
        return activatedList
    }

    private fun accumulation(activatedList: List<ActivatedFuzzySet>): List<UnionFuzzySet> {
        val accumulatedMap: MutableMap<Int, UnionFuzzySet> = mutableMapOf()
        var index = 0

        for (rule in rules) {
            for (conclusion in rule.conclusions) {
                val conclusionIndex = conclusion.variable.index
                if (!accumulatedMap.containsKey(conclusionIndex)) {
                    accumulatedMap[conclusionIndex] = UnionFuzzySet()
                }
                accumulatedMap[conclusionIndex]?.addFuzzySet(activatedList[index++])
            }
        }
        return accumulatedMap.values.toList()
    }

    private fun defuzzification(accumulatedList: List<UnionFuzzySet>): List<Double> {
        val defuzzificatedList: MutableList<Double> = mutableListOf()

        for (accumulated in accumulatedList) {
            val i1 = integral(accumulated, true)
            val i2 = integral(accumulated, false)
            defuzzificatedList.add(i1 / i2)
        }
        return defuzzificatedList
    }

    private fun integral(fuzzySet: FuzzySet, useX: Boolean): Double {
        val op = { x: Double -> if (useX) x * fuzzySet.getValue(x) else fuzzySet.getValue(x) }
        return integrate(0.0, 100.0, op)
    }

    private fun integrate(a: Double, b: Double, f: (Double) -> Double): Double {
        val n = 10000                 // precision parameter
        val h = (b - a) / (n - 1)     // step size

        // 1/3 terms
        var sum: Double = 1.0 / 3.0 * (f.invoke(a) + f.invoke(b))

        // 4/3 terms
        for (i in 1 until n step 2) {
            val x = a + h * i
            sum += 4.0 / 3.0 * f.invoke(x)
        }

        // 2/3 terms
        for (i in 2 until n step 2) {
            val x = a + h * i
            sum += 2.0 / 3.0 * f.invoke(x)
        }

        return sum * h
    }
}
