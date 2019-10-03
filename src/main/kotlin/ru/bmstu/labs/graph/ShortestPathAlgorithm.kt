package ru.bmstu.labs.graph

import ru.bmstu.labs.graph.model.Graph
import ru.bmstu.labs.graph.model.Vertex

class ShortestPathAlgorithm<T> {

    fun getShortestPath(graph: Graph<T>, vertexFrom: Vertex<T>, vertexTo: Vertex<T>): List<Vertex<T>> {
        val algo = BreadthFirstSearchAlgorithm<T>()
        algo.bfs(graph, vertexFrom)

        val path: MutableList<Vertex<T>> = mutableListOf()
        var currentVertex: Vertex<T>? = vertexTo

        while (currentVertex != null && currentVertex.distance > 0) {
            path.add(currentVertex)
            currentVertex = getPreviousVertex(graph, currentVertex)
        }

        path.add(vertexFrom)

        return path.reversed()
    }

    private fun getPreviousVertex(graph: Graph<T>, vertex: Vertex<T>): Vertex<T>? {
        for (edge in vertex.adjacentEdges) {
            val oppositeVertex = graph.getOppositeVertex(vertex, edge)
            if (oppositeVertex.distance + edge.weight == vertex.distance) {
                return oppositeVertex
            }
        }
        return null
    }
}