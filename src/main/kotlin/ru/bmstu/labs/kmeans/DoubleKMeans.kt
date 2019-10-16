package ru.bmstu.labs.kmeans

import java.util.*

open class DoubleKMeans(
        centroids: Array<DoubleArray>,
        points: Array<DoubleArray>,
        equal: Boolean,
        doubleDistanceFunction: DoubleDistanceFunction
) : AbstractKMeans<DoubleArray, DoubleArray>(
        centroids,
        points,
        equal,
        DistanceFunction(doubleDistanceFunction),
        CenterFunction()
) {

    interface DoubleDistanceFunction {
        fun distance(p1: DoubleArray, p2: DoubleArray): Double
    }

    protected class DistanceFunction(private val doubleDistanceFunction: DoubleDistanceFunction)
        : AbstractKMeans.DistanceFunction<DoubleArray, DoubleArray> {

        override fun distance(changed: BooleanArray, distances: Array<DoubleArray>, centroids: Array<DoubleArray>, points: Array<DoubleArray>) {
            for (c in centroids.indices) {
                if (!changed[c]) continue
                val centroid = centroids[c]
                for (p in points.indices) {
                    val point = points[p]
                    distances[c][p] = doubleDistanceFunction.distance(centroid, point)
                }
            }
        }
    }

    protected class CenterFunction : AbstractKMeans.CenterFunction<DoubleArray, DoubleArray> {

        override fun center(changed: BooleanArray, assignments: IntArray, centroids: Array<DoubleArray>, points: Array<DoubleArray>) {
            for (c in centroids.indices) {
                if (!changed[c]) continue
                val centroid = centroids[c]
                var n = 0
                for (p in points.indices) {
                    if (assignments[p] != c) continue
                    val point = points[p]
                    if (n++ == 0) Arrays.fill(centroid, 0.0)
                    var d = 0
                    while (d < centroid.size && d < point.size) {
                        centroid[d] += point[d]
                        d++
                    }
                }
                if (n > 0) {
                    for (d in centroid.indices) {
                        centroid[d] /= n.toDouble()
                    }
                }
            }
        }
    }

    companion object {

        val EUCLIDEAN_DISTANCE_FUNCTION: DoubleDistanceFunction = object : DoubleDistanceFunction {

            override fun distance(p1: DoubleArray, p2: DoubleArray): Double {
                var s = 0.0
                var d = 0
                while (d < p1.size && d < p2.size) {
                    s += Math.pow(Math.abs(p1[d] - p2[d]), 2.0)
                    d++
                }
                return Math.sqrt(s)
            }
        }

        val MANHATTAN_DISTANCE_FUNCTION: DoubleDistanceFunction = object : DoubleDistanceFunction {

            override fun distance(p1: DoubleArray, p2: DoubleArray): Double {
                var s = 0.0
                var d = 0
                while (d < p1.size && d < p2.size) {
                    s += Math.abs(p1[d] - p2[d])
                    d++
                }
                return s
            }
        }
    }
}