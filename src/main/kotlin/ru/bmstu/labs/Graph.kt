package ru.bmstu.labs

class Graph<T> {

    private var vertices: MutableList<Vertex<T>> = mutableListOf()

    fun size(): Int = vertices.size

    fun contains(value: T): Boolean = vertices.any { it.value == value }

    private data class Vertex<T>(
            val value: T,
            val adjacentEdges: List<Edge<T>> = mutableListOf()
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Vertex<*>) return false
            return value == other.value
        }

        override fun hashCode(): Int = value.hashCode()
    }

    private data class Edge<T>(
            val vertexFrom: Vertex<T>,
            val vertexTo: Vertex<T>,
            val weight: Int
    )
}