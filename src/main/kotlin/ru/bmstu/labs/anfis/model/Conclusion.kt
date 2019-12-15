package ru.bmstu.labs.anfis.model

data class Conclusion(var p: Double,
                      var q: Double,
                      var r: Double,
                      var gradP: Double = 0.0,
                      var gradQ: Double = 0.0,
                      var gradR: Double = 0.0,
                      var conclusion: Double = 0.0) {

    fun conclude(x: Double, y: Double) {
        conclusion = p * x + q * y + r
    }
}