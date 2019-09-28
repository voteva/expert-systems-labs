package ru.bmstu.labs.graph

data class Edge<T>(
        val vertexFrom: Vertex<T>,
        val vertexTo: Vertex<T>,
        val weight: Int
)