package ru.bmstu.labs.logical_inference.model

data class Predicate(
        var lhs: String,
        var rhs: String,
        val negation: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Predicate) return false
        return lhs == other.lhs && rhs == other.rhs
    }

    override fun hashCode(): Int = 31 + lhs.hashCode() * rhs.hashCode()
}