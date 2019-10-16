package ru.bmstu.labs.kmeans

import java.util.*

open class AbstractKMeans<Centroid, Point>(
        val centroids: Array<Centroid>,
        val points: Array<Point>,
        val equal: Boolean,
        val distanceFunction: DistanceFunction<Centroid, Point>,
        val centerFunction: CenterFunction<Centroid, Point>
) {

    open val assignments: IntArray
    open val counts: IntArray
    private val idealCount: Int
    private val distances: Array<DoubleArray>
    private val changed: BooleanArray
    private val done: BooleanArray

    interface DistanceFunction<Centroid, Point> {
        fun distance(changed: BooleanArray, distances: Array<DoubleArray>, centroids: Array<Centroid>, points: Array<Point>)
    }

    interface CenterFunction<Centroid, Point> {
        fun center(changed: BooleanArray, assignments: IntArray, centroids: Array<Centroid>, points: Array<Point>)
    }

    init {
        idealCount = if (centroids.isNotEmpty()) points.size / centroids.size else 0

        distances = Array(centroids.size) { DoubleArray(points.size) }
        assignments = IntArray(points.size)
        Arrays.fill(assignments, -1)
        changed = BooleanArray(centroids.size)
        Arrays.fill(changed, true)
        counts = IntArray(centroids.size)
        done = BooleanArray(centroids.size)
    }

    @JvmOverloads
    fun run(iteration: Int = 128): IntArray {
        calculateDistances()
        var move = makeAssignments()
        var i = 0
        while (move > 0 && i++ < iteration) {
            if (points.size >= centroids.size) {
                move = fillEmptyCentroids()
            }
            moveCentroids()
            calculateDistances()
            move += makeAssignments()
        }
        return assignments
    }

    private fun calculateDistances() {
        distanceFunction.distance(changed, distances, centroids, points)
        Arrays.fill(changed, false)
    }

    private fun makeAssignments(): Int {
        var move = 0
        Arrays.fill(counts, 0)
        for (p in points.indices) {
            val nc = nearestCentroid(p)
            if (nc == -1) {
                continue
            }
            if (assignments[p] != nc) {
                if (assignments[p] != -1) {
                    changed[assignments[p]] = true
                }
                changed[nc] = true
                assignments[p] = nc
                move++
            }
            counts[nc]++
            if (equal && counts[nc] > idealCount) {
                move += remakeAssignments(nc)
            }
        }
        return move
    }

    private fun remakeAssignments(cc: Int): Int {
        var move = 0
        var md = Double.POSITIVE_INFINITY
        var nc = -1
        var np = -1
        for (p in points.indices) {
            if (assignments[p] != cc) {
                continue
            }
            for (c in centroids.indices) {
                if (c == cc || done[c]) {
                    continue
                }
                val d = distances[c][p]
                if (d < md) {
                    md = d
                    nc = c
                    np = p
                }
            }
        }
        if (nc != -1 && np != -1) {
            if (assignments[np] != nc) {
                if (assignments[np] != -1) {
                    changed[assignments[np]] = true
                }
                changed[nc] = true
                assignments[np] = nc
                move++
            }
            counts[cc]--
            counts[nc]++
            if (counts[nc] > idealCount) {
                done[cc] = true
                move += remakeAssignments(nc)
                done[cc] = false
            }
        }
        return move
    }

    private fun nearestCentroid(p: Int): Int {
        var md = Double.POSITIVE_INFINITY
        var nc = -1
        for (c in centroids.indices) {
            val d = distances[c][p]
            if (d < md) {
                md = d
                nc = c
            }
        }
        return nc
    }

    private fun nearestPoint(inc: Int, fromc: Int): Int {
        var md = Double.POSITIVE_INFINITY
        var np = -1
        for (p in points.indices) {
            if (assignments[p] != inc) {
                continue
            }
            val d = distances[fromc][p]
            if (d < md) {
                md = d
                np = p
            }
        }
        return np
    }

    private fun largestCentroid(except: Int): Int {
        var lc = -1
        val mc = 0
        for (c in centroids.indices) {
            if (c == except) {
                continue
            }
            if (counts[c] > mc) {
                lc = c
            }
        }
        return lc
    }

    private fun fillEmptyCentroids(): Int {
        var move = 0
        for (c in centroids.indices) {
            if (counts[c] == 0) {
                val lc = largestCentroid(c)
                val np = nearestPoint(lc, c)
                assignments[np] = c
                counts[c]++
                counts[lc]--
                changed[c] = true
                changed[lc] = true
                move++
            }
        }
        return move
    }

    private fun moveCentroids() {
        centerFunction.center(changed, assignments, centroids, points)
    }
}