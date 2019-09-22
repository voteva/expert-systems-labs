package ru.bmstu.labs

import kotlin.test.Test
import kotlin.test.assertNotNull

class GraphTest {

    @Test fun testGraph() {
        val graph = Graph<Int>()

        val v1 = graph.add(1)
        val v2 = graph.add(2)
        val v3 = graph.add(3)

        graph.addEdge(v1, v2, 1)
        graph.addEdge(v2, v3, 3)
        graph.addEdge(v1, v3, 1)

        val shortestPath = graph.getShortestPath(v1, v3)
        shortestPath.forEach{
            println(it.value)
        }
    }
}
