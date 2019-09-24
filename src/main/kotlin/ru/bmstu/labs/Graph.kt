package ru.bmstu.labs

import java.util.*

class Graph<T> {

    private var vertices: MutableList<Vertex<T>> = mutableListOf()

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

    fun getShortestPath(vertexFrom: Vertex<T>, vertexTo: Vertex<T>): List<Vertex<T>> {
        bfs(vertexFrom)

        val path: MutableList<Vertex<T>> = mutableListOf()
        var currentVertex: Vertex<T>? = vertexTo

        while (currentVertex != null && currentVertex.distance > 0) {
            path.add(currentVertex)
            currentVertex = getAdjacentVertexWithLessDistance(currentVertex)
        }

        path.add(vertexFrom)

        return path.reversed()
    }

    private fun getOppositeVertex(vertex: Vertex<T>, edge: Edge<T>): Vertex<T> {
        return if (edge.vertexFrom == vertex) edge.vertexTo else edge.vertexFrom
    }

    private fun getAdjacentVertexWithLessDistance(vertex: Vertex<T>): Vertex<T>? {
        for (edge in vertex.adjacentEdges) {
            val oppositeVertex = getOppositeVertex(vertex, edge)
            if (oppositeVertex.distance < vertex.distance) {
                return oppositeVertex
            }
        }
        return null
    }

    private fun bfs(vertexFrom: Vertex<T>) {
        for (vertex in vertices) {
            vertex.distance = Integer.MAX_VALUE
            vertex.visited = false
        }
        vertexFrom.distance = 0
        vertexFrom.visited = true

        val verticesQueue = LinkedList<Vertex<T>>()
        verticesQueue.add(vertexFrom)

        while (verticesQueue.isNotEmpty()) {
            val vertex = verticesQueue.remove()

            // try to improve state
            for (edge in vertex.adjacentEdges) {
                val oppositeVertex = getOppositeVertex(vertex, edge)
                if (!edge.repairWorks && vertex.distance + edge.weight < oppositeVertex.distance) {
                    oppositeVertex.distance = vertex.distance + edge.weight
                }
                oppositeVertex.visited = true
            }
        }
    }
}