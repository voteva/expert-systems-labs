package ru.bmstu.labs

import ru.bmstu.labs.logical_inference.LogicalInferenceAlgorithm
import ru.bmstu.labs.logical_inference.model.Predicate
import ru.bmstu.labs.logical_inference.model.Rule
import ru.bmstu.labs.logical_inference.model.RuleDeclaration.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Lab2 {

    private val algorithm = LogicalInferenceAlgorithm()

    private val facts: List<Predicate> = mutableListOf(
            Predicate("kirov", "kazan"),
            Predicate("perm", "kazan"),
            Predicate("kazan", "vladimir"),
            Predicate("vladimir", "moscow"),
            Predicate("moscow", "kiev"))

    @Test
    fun pathAvailableAllRules() {
        val rules: List<Rule> = mutableListOf(
                Rule(RULE_PARTITION),
                Rule(RULE_SWAP),
                Rule(RULE_NO_PATH, Predicate("vladimir", "moscow", true)))

        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "kazan")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kazan", "perm")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kirov", "perm")))
        assertTrue(algorithm.fol(rules, facts, Predicate("moscow", "kiev")))

        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "moscow")))
        assertFalse(algorithm.fol(rules, facts, Predicate("moscow", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("vladimir", "kiev")))
        assertFalse(algorithm.fol(rules, facts, Predicate("kazan", "kiev")))
        assertFalse(algorithm.fol(rules, facts, Predicate("kiev", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "vladivostok")))
    }

    @Test
    fun pathAvailablePartitionAndSwapRules() {
        val rules: List<Rule> = mutableListOf(Rule(RULE_PARTITION), Rule(RULE_SWAP))

        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "kazan")))
        assertTrue(algorithm.fol(rules, facts, Predicate("moscow", "kiev")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kazan", "perm")))
        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "moscow")))
        assertTrue(algorithm.fol(rules, facts, Predicate("moscow", "perm")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kirov", "perm")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kiev", "perm")))

        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "vladivostok")))
    }

    @Test
    fun pathAvailablePartitionRuleOnly() {
        val rules: List<Rule> = mutableListOf(Rule(RULE_PARTITION))

        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "kazan")))
        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "moscow")))

        assertFalse(algorithm.fol(rules, facts, Predicate("kazan", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("moscow", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("kirov", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "vladivostok")))
    }

    @Test
    fun pathAvailableSwapRuleOnly() {
        val rules: List<Rule> = mutableListOf(Rule(RULE_SWAP))

        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "kazan")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kazan", "perm")))
        assertTrue(algorithm.fol(rules, facts, Predicate("moscow", "vladimir")))

        assertFalse(algorithm.fol(rules, facts, Predicate("moscow", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("kirov", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "vladivostok")))
    }

    @Test
    fun pathAvailableNoPath() {
        val rules: List<Rule> = mutableListOf(Rule(RULE_NO_PATH, Predicate("vladimir", "moscow", true)))

        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "kazan")))
        assertTrue(algorithm.fol(rules, facts, Predicate("moscow", "kiev")))

        assertFalse(algorithm.fol(rules, facts, Predicate("vladimir", "moscow")))
        assertFalse(algorithm.fol(rules, facts, Predicate("moscow", "vladimir")))
        assertFalse(algorithm.fol(rules, facts, Predicate("moscow", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("vladimir", "kiev")))
    }

    @Test
    fun pathAvailableNoRules() {
        val rules: List<Rule> = mutableListOf()

        assertTrue(algorithm.fol(rules, facts, Predicate("perm", "kazan")))
        assertTrue(algorithm.fol(rules, facts, Predicate("kazan", "vladimir")))

        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "moscow")))
        assertFalse(algorithm.fol(rules, facts, Predicate("kazan", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("moscow", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("kirov", "perm")))
        assertFalse(algorithm.fol(rules, facts, Predicate("perm", "vladivostok")))
    }
}
