package ru.bmstu.labs

import ru.bmstu.labs.logic.LogicalInference
import ru.bmstu.labs.logic.Predicate
import ru.bmstu.labs.logic.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Lab2 {

    @Test
    fun pathAvailable() {
        val algorithm = LogicalInference()

        val rules: List<Rule> = mutableListOf(
                Rule.RULE1,
                Rule.RULE2
        )

        val facts: List<Predicate> = mutableListOf(
                Predicate("kirov", "kazan"),
                Predicate("perm", "kazan"),
                Predicate("kazan", "vladimir"),
                Predicate("vladimir", "moscow"))

//        var result = algorithm.fol(rules, facts, Predicate("perm", "kazan"))
//        assertTrue(result)
//
//        result = algorithm.fol(rules, facts, Predicate("kazan", "perm"))
//        assertTrue(result)

        var result = algorithm.fol(rules, facts, Predicate("perm", "moscow"))
        assertTrue(result)

        result = algorithm.fol(rules, facts, Predicate("moscow", "perm"))
        assertTrue(result)

        result = algorithm.fol(rules, facts, Predicate("kirov", "perm"))
        assertFalse(result)
    }
}
