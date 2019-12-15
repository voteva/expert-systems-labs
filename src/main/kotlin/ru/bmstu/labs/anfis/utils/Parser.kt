package ru.bmstu.labs.anfis.utils

import ru.bmstu.labs.anfis.model.Vector
import java.io.File

object Parser {
    @JvmStatic
    fun parseVectors(filename: String): List<Vector> {
        val vectors: MutableList<Vector> = mutableListOf()
        val file = File(filename)
        file.bufferedReader().forEachLine { line ->
            val parts = line.split(",").toTypedArray()
            vectors.add(Vector(
                    parts[0].trim { it <= ' ' }.toDouble(),
                    parts[1].trim { it <= ' ' }.toDouble(),
                    parts[2].trim { it <= ' ' }.toDouble()))
        }
        return vectors
    }
}