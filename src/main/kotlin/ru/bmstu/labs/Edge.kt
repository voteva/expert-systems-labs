package ru.bmstu.labs

data class Edge<T>(
        val vertexFrom: Vertex<T>,
        val vertexTo: Vertex<T>,
        val weight: Int,
        val trafficJam: Boolean = false
)