package ru.bmstu.labs.graph.model

import java.util.*

class Graph<T> {

    var vertices: MutableList<Vertex<T>> = mutableListOf()

    fun size(): Int = vertices.size

    fun contains(value: T): Boolean = vertices.any { it.value == value }

    fun findVertex(value: T): Vertex<T>? = vertices.firstOrNull { it.value == value }

    fun add(value: T): Vertex<T> {
        return Optional
                .ofNullable(findVertex(value))
                .orElseGet {
                    val vertex = Vertex(value = value)
                    vertices.add(vertex)
                    vertex
                }
    }

    fun addEdge(from: Vertex<T>, to: Vertex<T>, weight: Int): Edge<T> {
        val edge = Edge(from, to, weight);
        from.addEdge(edge)
        to.addEdge(edge)
        return edge
    }

    fun getOppositeVertex(vertex: Vertex<T>, edge: Edge<T>): Vertex<T> {
        return if (edge.vertexFrom == vertex) edge.vertexTo else edge.vertexFrom
    }
}