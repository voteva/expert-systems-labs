package ru.bmstu.labs.logic

data class Goal<T>(
        val lhs: T,
        val rhs: T
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Predicate<*>) return false
        return lhs == other.lhs && rhs == other.rhs
    }

    override fun hashCode(): Int = 31 + lhs.hashCode() * rhs.hashCode()
}