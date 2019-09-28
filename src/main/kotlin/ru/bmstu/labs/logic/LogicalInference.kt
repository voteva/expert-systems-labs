package ru.bmstu.labs.logic

class LogicalInference<T, R> {

    fun fol(rules: List<Predicate<R>>, facts: List<Predicate<T>>, goal: Predicate<T>): Boolean {
        if (isGoalInFactsList(facts, goal)) {
            return true
        }

        val currentFact = choosePredicateForGoal(facts, goal)
        if (currentFact == null) {
            return false // TODO
        } else {
            if (currentFact.rhs == goal.rhs) {
                return true
            }
            val newGoal = Predicate(currentFact.rhs, goal.rhs)
            return fol(rules, facts, newGoal)
        }
    }

    private fun isGoalInFactsList(facts: List<Predicate<T>>, goal: Predicate<T>): Boolean {
        return facts.any { it == goal }
    }

    private fun choosePredicateForGoal(facts: List<Predicate<T>>, goal: Predicate<T>): Predicate<T>? {
        for (fact in facts) {
            if (fact.lhs == goal.lhs) {
                return fact;
            }
        }
        return null
    }
}