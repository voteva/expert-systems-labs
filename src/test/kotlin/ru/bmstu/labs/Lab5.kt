package ru.bmstu.labs

import org.junit.Test
import ru.bmstu.labs.anfis.AnfisAlgorithm
import ru.bmstu.labs.anfis.utils.DatasetBuilder
import ru.bmstu.labs.anfis.utils.DatasetParser

class Lab5 {

    private val filename = "src/test/resources/dataset.txt"
    private val rulesNum = 16
    private val epochNum = 10000

    @Test
    fun anfis() {
        val vectors = DatasetParser.parseVectors(filename)
        val anfis = AnfisAlgorithm(rulesNum)

        anfis.train(vectors, epochNum)

        val inputs: List<Pair<Double, Double>> = mutableListOf(
                Pair(0.0, 0.0),
                Pair(1.0, 0.4),
                Pair(0.0, 2.0),
                Pair(3.0, 2.5),
                Pair(-2.0, 0.5),
                Pair(3.0, -0.4),
                Pair(-3.0, -2.8),
                Pair(1.2, -3.7)
        )

        for (input in inputs) {
            anfis.predict(input.first, input.second)

            println("x=${input.first} y=${input.second}")
            println("Expected result: ${DatasetBuilder.func(input.first, input.second)}")
            println("Actual result: ${anfis.output}")
            println()
        }
    }
}