package ru.bmstu.labs.graph.model

data class Edge<T>(
        val vertexFrom: Vertex<T>,
        val vertexTo: Vertex<T>,
        val weight: Int
)