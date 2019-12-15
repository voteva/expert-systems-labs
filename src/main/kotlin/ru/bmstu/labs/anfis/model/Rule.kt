package ru.bmstu.labs.anfis.model

data class Rule(val a: RuleUnit,
                val b: RuleUnit,
                val conclusion: Conclusion,
                var output: Double = 0.0)