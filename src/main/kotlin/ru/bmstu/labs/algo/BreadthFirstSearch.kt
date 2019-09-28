package ru.bmstu.labs.algo

import ru.bmstu.labs.graph.Graph
import ru.bmstu.labs.graph.Vertex
import java.util.*

class BreadthFirstSearch<T> {

    fun bfs(graph: Graph<T>, vertexFrom: Vertex<T>) {
        for (vertex in graph.vertices) {
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
                val oppositeVertex = graph.getOppositeVertex(vertex, edge)
                if (vertex.distance + edge.weight < oppositeVertex.distance) {
                    oppositeVertex.distance = vertex.distance + edge.weight
                }
                if (!oppositeVertex.visited) {
                    verticesQueue.add(oppositeVertex)
                    oppositeVertex.visited = true
                }
            }
        }
    }
}