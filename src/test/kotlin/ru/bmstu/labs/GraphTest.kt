package ru.bmstu.labs

import kotlin.test.Test
import kotlin.test.assertNotNull

class GraphTest {

    @Test fun testGraph() {
        val graph = Graph<String>()

        val v1 = graph.add("Perm")
        val v2 = graph.add("Izhevsk")
        val v3 = graph.add("Kazan")
        val v4 = graph.add("Moscow")

        graph.addEdge(v1, v2, 1)
        graph.addEdge(v2, v3, 1)
        graph.addEdge(v3, v4, 3)
        graph.addEdge(v1, v4, 6)

        val shortestPath = graph.getShortestPath(v1, v4)
        shortestPath.forEach{
            println(it.value)
        }
    }
}
