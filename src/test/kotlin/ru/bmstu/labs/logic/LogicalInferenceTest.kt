package ru.bmstu.labs.logic

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LogicalInferenceTest {

    @Test
    fun testFol1() {
        val rules: List<Rule<String>> = mutableListOf()

        val facts: List<Predicate<String>> = mutableListOf(
                Predicate("a", "b"),
                Predicate("b", "c"))

        val logicalInference = LogicalInference<String, String>()

        var result = logicalInference.fol(rules, facts, Predicate("a", "b"))
        assertTrue(result)

        result = logicalInference.fol(rules, facts, Predicate("a", "c"))
        assertTrue(result)

        result = logicalInference.fol(rules, facts, Predicate("b", "c"))
        assertTrue(result)

        result = logicalInference.fol(rules, facts, Predicate("b", "a"))
        assertFalse(result)

        result = logicalInference.fol(rules, facts, Predicate("a", "d"))
        assertFalse(result)
    }
}
