package ru.bmstu.labs.anfis.utils

import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

object DatasetBuilder {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val writer = PrintWriter("dataset.txt", StandardCharsets.UTF_8)
            for (x in -4..4) {
                for (y in -4..4) {
                    writer.println(x.toString() + ", " + y + ", " + func(x.toDouble(), y.toDouble()))
                }
            }
            writer.close()
        } catch (e: IOException) {
            println(e.message)
        }
    }

    @JvmStatic
    fun func(x: Double, y: Double): Double {
        return (Math.pow(x - 1, 2.0) + Math.pow(y + 2, 2.0) - 5 * x * y + 3) * Math.pow(Math.cos(x / 5), 2.0)
    }
}