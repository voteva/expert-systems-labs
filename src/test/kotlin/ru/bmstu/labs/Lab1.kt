package ru.bmstu.labs

import ru.bmstu.labs.algo.ShortestPath
import ru.bmstu.labs.graph.Graph
import kotlin.test.Test

class Lab1 {

    @Test
    fun shortestPath() {
        val graph = Graph<String>()

        val perm = graph.add("Perm")
        val kirov = graph.add("Kirov")
        val ekaterinburg = graph.add("Ekaterinburg")
        val ufa = graph.add("Ufa")
        val chelyabinsk = graph.add("Chelyabinsk")
        val izhevsk = graph.add("Izhevsk")
        val kazan = graph.add("Kazan")
        val yoshkarOla = graph.add("Yoshkar-Ola")
        val cheboksary = graph.add("Cheboksary")
        val nNovgorod = graph.add("N.Novgorod")
        val vladimir = graph.add("Vladimir")
        val ryazan = graph.add("Ryazan")
        val yaroslavl = graph.add("Yaroslavl")
        val moscow = graph.add("Moscow")

        graph.addEdge(perm, ekaterinburg, 363)
        graph.addEdge(perm, kirov, 494)
        graph.addEdge(perm, kazan, 591)
        graph.addEdge(perm, izhevsk, 281)
        graph.addEdge(izhevsk, kazan, 389)
        graph.addEdge(kirov, yaroslavl, 696)
        graph.addEdge(ekaterinburg, chelyabinsk, 210)
        graph.addEdge(ekaterinburg, ufa, 492)
        graph.addEdge(chelyabinsk, ufa, 421)
        graph.addEdge(ufa, kazan, 529)
        graph.addEdge(kazan, cheboksary, 151)
        graph.addEdge(kazan, yoshkarOla, 142)
        graph.addEdge(yoshkarOla, nNovgorod, 333)
        graph.addEdge(cheboksary, nNovgorod, 243)
        graph.addEdge(nNovgorod, vladimir, 229)
        graph.addEdge(vladimir, moscow, 189)
        graph.addEdge(ryazan, moscow, 202)
        graph.addEdge(yaroslavl, moscow, 265)

        val algo = ShortestPath<String>()

        val shortestPath = algo.getShortestPath(graph, perm, moscow)
        shortestPath.forEach {
            println("-> " + it.value + " " + it.distance)
        }
    }
}
