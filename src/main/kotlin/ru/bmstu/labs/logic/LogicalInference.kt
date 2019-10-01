package ru.bmstu.labs.logic

import java.util.stream.Collectors

class LogicalInference<T, R> {
    private val MAX_DEPTH: Int = 10

    fun fol(rules: List<Rule<R>>, facts: List<Predicate<T>>, goal: Predicate<T>): Boolean {
        goal.depth = goal.depth + 1

        if (isGoalInFactsList(facts, goal)) {
            return true
        }

        if (goal.depth == MAX_DEPTH) {
            return false
        }

        val factsForGoal = chooseFactsForGoal(facts, goal)
        for (fact in factsForGoal) {
            val newGoal = Predicate(fact.rhs, goal.rhs, goal.depth)
            if (fol(rules, facts, newGoal)) {
                return true
            }
        }
        val newFacts = extractNewFactsFromRules(rules, facts, goal)
        return fol(rules, newFacts, goal)
    }

    private fun isGoalInFactsList(facts: List<Predicate<T>>, goal: Predicate<T>): Boolean {
        return facts.any { it == goal }
    }

    private fun chooseFactsForGoal(facts: List<Predicate<T>>, goal: Predicate<T>): List<Predicate<T>> {
        return facts.stream()
                .filter { it.lhs == goal.lhs }
                .collect(Collectors.toList())
    }

    private fun extractNewFactsFromRules(rules: List<Rule<R>>, facts: List<Predicate<T>>, goal: Predicate<T>): List<Predicate<T>> {
        val newFacts: MutableList<Predicate<T>> = mutableListOf()
        newFacts.addAll(facts)

        for (rule in rules) {
            rule.head
        }

        return newFacts
    }
}