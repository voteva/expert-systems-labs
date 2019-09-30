package ru.bmstu.labs.logic

class LogicalInference<T, R> {

    fun fol(rules: List<Rule<R>>, facts: List<Predicate<T>>, goal: Predicate<T>): Boolean {
        if (isGoalInFactsList(facts, goal)) {
            return true
        }

        val currentFact = choosePredicateForGoal(facts, goal)
        return if (currentFact == null) {
            false // TODO
        } else {
            val newGoal = Predicate(currentFact.rhs, goal.rhs)
            fol(rules, facts, newGoal)
        }
    }

    private fun isGoalInFactsList(facts: List<Predicate<T>>, goal: Predicate<T>): Boolean {
        return facts.any { it == goal }
    }

    private fun choosePredicateForGoal(facts: List<Predicate<T>>, goal: Predicate<T>): Predicate<T>? {
        return facts.stream()
                .filter { it.lhs == goal.lhs }
                .findAny()
                .orElse(null)
    }
}