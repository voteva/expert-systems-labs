package ru.bmstu.labs.logical_inference

import ru.bmstu.labs.logical_inference.model.Predicate
import ru.bmstu.labs.logical_inference.model.Rule
import ru.bmstu.labs.logical_inference.model.RuleDeclaration.*

class LogicalInferenceAlgorithm {
    private val maxRecursionDepth: Int = 5

    fun fol(rules: List<Rule>, facts: List<Predicate>, goal: Predicate, depth: Int = 0): Boolean {
        if (isRulesContradictGoal(rules, goal)) {
            return false
        }
        if (isGoalInFactsList(facts, goal)) {
            return true
        }
        if (depth == maxRecursionDepth) {
            return false
        }
        if (rules.isEmpty()) {
            return false
        }

        for (rule in rules) {
            var atLeastOneFactSuccess = false
            val abstractGoals = applyRule(rule, goal)

            for (fact in facts) {
                var allGoalsSuccess = true

                val newGoals = applyFactToGoals(rule, abstractGoals, fact)
                if (newGoals.isEmpty()) {
                    allGoalsSuccess = false
                }

                for (newGoal in newGoals) {
                    if (!fol(rules, facts, newGoal, depth + 1)) {
                        allGoalsSuccess = false
                    }
                }

                if (allGoalsSuccess) {
                    atLeastOneFactSuccess = true
                }
            }

            if (atLeastOneFactSuccess) {
                return true
            }
        }

        return false
    }

    private fun isRulesContradictGoal(rules: List<Rule>, goal: Predicate): Boolean {
        return rules.any { it.argument == goal && it.argument.negation }
    }

    private fun isGoalInFactsList(facts: List<Predicate>, goal: Predicate): Boolean {
        return facts.any { it == goal && !it.negation }
    }

    private fun applyRule(rule: Rule, goal: Predicate): List<Predicate> {
        val abstractGoals: MutableList<Predicate> = mutableListOf()

        when (rule.declaration) {
            RULE_PARTITION -> {
                abstractGoals.add(Predicate(goal.lhs, "*"))
                abstractGoals.add(Predicate("*", goal.rhs))
            }
            RULE_SWAP -> abstractGoals.add(Predicate(goal.rhs, goal.lhs))
        }

        return abstractGoals
    }

    private fun applyFactToGoals(rule: Rule, abstractGoals: List<Predicate>, fact: Predicate): List<Predicate> {
        val newGoals: MutableList<Predicate> = mutableListOf()

        when (rule.declaration) {
            RULE_PARTITION -> {
                if (abstractGoals[0].lhs == fact.lhs) {
                    newGoals.add(fact)
                    newGoals.add(Predicate(fact.rhs, abstractGoals[1].rhs))
                } else if (abstractGoals[1].rhs == fact.rhs) {
                    newGoals.add(Predicate(abstractGoals[0].lhs, fact.rhs))
                    newGoals.add(fact)
                }
            }
            RULE_SWAP -> newGoals.add(abstractGoals[0])
        }

        return newGoals
    }
}