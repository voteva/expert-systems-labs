package ru.bmstu.labs.kmeans

import java.util.*

open class AbstractKMeans<Centroid, Point>(
        val centroids: Array<Centroid>,
        val points: Array<Point>,
        val equal: Boolean,
        val distanceFunction: DistanceFunction<Centroid, Point>,
        val centerFunction: CenterFunction<Centroid, Point>,
        val listener: Listener?
) {

    protected val idealCount: Int
    protected val distances: Array<DoubleArray>
    open val assignments: IntArray
    protected val changed: BooleanArray
    open val counts: IntArray
    protected val done: BooleanArray

    interface Listener {
        fun iteration(iteration: Int, move: Int)
    }

    interface DistanceFunction<Centroid, Point> {
        fun distance(changed: BooleanArray, distances: Array<DoubleArray>, centroids: Array<Centroid>, points: Array<Point>)
    }

    interface CenterFunction<Centroid, Point> {
        fun center(changed: BooleanArray, assignments: IntArray, centroids: Array<Centroid>, points: Array<Point>)
    }

    init {
        if (centroids.size > 0) {
            idealCount = points.size / centroids.size
        } else {
            idealCount = 0
        }
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
            listener?.iteration(i, move)
        }
        return assignments
    }

    protected fun calculateDistances() {
        distanceFunction.distance(changed, distances, centroids, points)
        Arrays.fill(changed, false)
    }

    protected fun makeAssignments(): Int {
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

    protected fun remakeAssignments(cc: Int): Int {
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

    protected fun nearestCentroid(p: Int): Int {
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

    protected fun nearestPoint(inc: Int, fromc: Int): Int {
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

    protected fun largestCentroid(except: Int): Int {
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

    protected fun fillEmptyCentroids(): Int {
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

    protected fun moveCentroids() {
        centerFunction.center(changed, assignments, centroids, points)
    }
}