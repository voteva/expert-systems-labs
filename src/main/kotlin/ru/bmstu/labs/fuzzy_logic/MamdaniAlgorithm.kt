package ru.bmstu.labs.fuzzy_logic

import ru.bmstu.labs.fuzzy_logic.model.Rule
import ru.bmstu.labs.fuzzy_logic.model.sets.ActivatedFuzzySet
import ru.bmstu.labs.fuzzy_logic.model.sets.FuzzySet
import ru.bmstu.labs.fuzzy_logic.model.sets.UnionFuzzySet
import java.util.*
import java.util.ArrayList

class MamdaniAlgorithm(
        val rules: List<Rule>,
        val inputData: Array<Double>
) {

    private fun fuzzification(inputData: Array<Double>): List<Double> {
        val b: MutableList<Double> = mutableListOf()

        for (rule in rules) {
            for (condition in rule.conditions) {
                val j = condition.variable.index
                val term = condition.term
                b.add(term.getValue(inputData[j]))
            }
        }
        return b
    }

    private fun aggregation(b: List<Double>): List<Double> {
        val c: MutableList<Double> = mutableListOf()
        var i = 0

        for (rule in rules) {
            var truthOfConditions = 1.0
            for (condition in rule.conditions) {
                truthOfConditions = Math.min(truthOfConditions, b[i])
                i++
            }
            c.add(truthOfConditions)
        }
        return c
    }

    private fun activation(c: List<Double>): List<ActivatedFuzzySet> {
        val activatedFuzzySets = ArrayList<ActivatedFuzzySet>()
        var i = 0

        for (rule in rules) {
            for (conclusion in rule.conclusions) {
                activatedFuzzySets.add(ActivatedFuzzySet(
                        conclusion.term, c[i] * conclusion.weight
                ))
                i++
            }
        }
        return activatedFuzzySets
    }

    private fun accumulation(activatedFuzzySets: List<ActivatedFuzzySet>): List<UnionFuzzySet> {
        val unionsOfFuzzySets = HashMap<Int, UnionFuzzySet>()
        var i = 0

        for (rule in rules) {
            for (conclusion in rule.conclusions) {
                val index = conclusion.variable.index
                if (!unionsOfFuzzySets.containsKey(index)) {
                    unionsOfFuzzySets[index] = UnionFuzzySet()
                }
                unionsOfFuzzySets[index]?.addFuzzySet(activatedFuzzySets[i])
                i++
            }
        }
        return ArrayList(unionsOfFuzzySets.values)
    }

    private fun defuzzification(unionsOfFuzzySets: List<UnionFuzzySet>): List<Double> {
        val y = ArrayList<Double>()
        for (unionOfFuzzySets in unionsOfFuzzySets) {
            val i1 = integral(unionOfFuzzySets, true)
            val i2 = integral(unionOfFuzzySets, false)
            y.add(i1 / i2)
        }
        return y
    }

    private fun integral(fuzzySet: FuzzySet, useX: Boolean): Double {
        val op = { x: Double -> if (useX) x * fuzzySet.getValue(x) else fuzzySet.getValue(x) }
        return integrate(0.0, 100.0, op)
    }

    fun integrate(a: Double, b: Double, f: (Double) -> Double): Double {
        val n = 10000                 // precision parameter
        val h = (b - a) / (n - 1)     // step size

        // 1/3 terms
        var sum: Double = 1.0 / 3.0 * (f.invoke(a) + f.invoke(b))

        // 4/3 terms
        run {
            var i: Int = 1
            while (i < n - 1) {
                val x = a + h * i
                sum += 4.0 / 3.0 * f.invoke(x)
                i += 2
            }
        }

        // 2/3 terms
        var i: Int = 2
        while (i < n - 1) {
            val x = a + h * i
            sum += 2.0 / 3.0 * f.invoke(x)
            i += 2
        }

        return sum * h
    }
}
