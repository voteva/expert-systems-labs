package ru.bmstu.labs.logic

class LogicalInference {
    private val MAX_DEPTH: Int = 10

    fun fol(rules: List<Rule>, facts: List<Predicate>, goal: Predicate, depth: Int = 0): Boolean {
        if (isGoalInFactsList(facts, goal)) {
            return true
        }

        if (depth == MAX_DEPTH) {
            return false
        }

        return rules.stream()
                .anyMatch { rule ->
                    for (fact in facts) {
                        val abstractGoals = chooseAbstractGoals(rule, goal)

                        val newGoals: MutableList<Predicate> = mutableListOf()

                        if (abstractGoals.size == 1) {
                            if (abstractGoals[0].lhs == fact.lhs && abstractGoals[0].rhs == fact.rhs) {
                                newGoals.add(fact)
                            }
                        } else if (abstractGoals.size == 2) {
                            if (abstractGoals[0].lhs == fact.lhs && abstractGoals[1].rhs == "*") {
                                newGoals.add(Predicate(fact.lhs, fact.rhs))
                                newGoals.add(Predicate(fact.rhs, abstractGoals[1].rhs))
                            } else if (abstractGoals[1].lhs == "*" && abstractGoals[1].rhs == fact.rhs) {
                                //newGoals.add(Predicate(fact.lhs, fact.rhs))
                                //newGoals.add(Predicate(fact.rhs, fact.rhs))
                            }
                        }

                        val allGoalsMatch = newGoals.stream()
                                .allMatch { newGoal ->
                                    fol(rules, facts, newGoal, depth + 1)
                                }

                        if (allGoalsMatch && newGoals.isNotEmpty()) {
                            return@anyMatch true
                        }

                    }
                    false
                }
    }

    private fun isGoalInFactsList(facts: List<Predicate>, goal: Predicate): Boolean {
        return facts.any { it == goal }
    }

    private fun chooseAbstractGoals(rule: Rule, goal: Predicate): List<Predicate> {
        val abstractGoals: MutableList<Predicate> = mutableListOf()

        if (rule == Rule.RULE1) {
            abstractGoals.add(Predicate(goal.lhs, "*"))
            abstractGoals.add(Predicate("*", goal.rhs))
        } else if (rule == Rule.RULE2) {
            abstractGoals.add(Predicate(goal.rhs, goal.lhs))
        }

        return abstractGoals
    }
}