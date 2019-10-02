package ru.bmstu.labs

import ru.bmstu.labs.logic.LogicalInference
import ru.bmstu.labs.logic.Predicate
import ru.bmstu.labs.logic.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Lab2 {

    @Test
    fun shortestPath() {
        val algorithm = LogicalInference<String, String>()

        val rules: List<Rule<String>> = mutableListOf()

        val facts: List<Predicate<String>> = mutableListOf(
                Predicate("perm", "ekaterinburg"),
                Predicate("perm", "kirov"),
                Predicate("perm", "kazan"),
                Predicate("perm", "izhevsk"),
                Predicate("izhevsk", "kazan"),
                Predicate("kirov", "yaroslavl"),
                Predicate("ekaterinburg", "chelyabinsk"),
                Predicate("ekaterinburg", "ufa"),
                Predicate("chelyabinsk", "ufa"),
                Predicate("ufa", "kazan"),
                Predicate("kazan", "cheboksary"),
                Predicate("kazan", "yoshkarOla"),
                Predicate("yoshkarOla", "nNovgorod"),
                Predicate("cheboksary", "nNovgorod"),
                Predicate("nNovgorod", "vladimir"),
                Predicate("vladimir", "moscow"),
                Predicate("ryazan", "moscow"),
                Predicate("yaroslavl", "moscow"))

        var result = algorithm.fol(rules, facts, Predicate("perm", "moscow"))
        assertTrue(result)

        result = algorithm.fol(rules, facts, Predicate("ekaterinburg", "kazan"))
        assertTrue(result)

        result = algorithm.fol(rules, facts, Predicate("kirov", "ryazan"))
        assertFalse(result)
    }
}
