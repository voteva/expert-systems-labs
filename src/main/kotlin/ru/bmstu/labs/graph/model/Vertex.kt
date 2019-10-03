package ru.bmstu.labs.graph.model

data class Vertex<T>(
        val value: T,
        val adjacentEdges: MutableList<Edge<T>> = mutableListOf(),
        var visited: Boolean = false,
        var distance: Int = Int.MAX_VALUE
) {

    fun addEdge(edge: Edge<T>) = adjacentEdges.add(edge)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vertex<*>) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()
}