package ru.bmstu.labs.anfis.model

import kotlin.math.exp

data class RuleUnit(var a: Double,
                    var b: Double,
                    var gradA: Double = 0.0,
                    var gradB: Double = 0.0,
                    var output: Double = 0.0) {

    fun calculate(x: Double) {
        val e = 1.0 + exp(b * (x - a))
        output = 1.0 / e
    }
}