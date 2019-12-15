package ru.bmstu.labs.anfis.model

data class RuleUnit(var a: Double,
                    var b: Double,
                    var output: Double = 0.0,
                    var gradA: Double = 0.0,
                    var gradB: Double = 0.0) {

    fun calculate(x: Double) {
        //System.out.println("b=" + this.b + " * (x=" + x + " -a=" + this.a + ")");
        val e = 1.0 + Math.exp(b * (x - a))
        //System.out.println("e je " + e);
        output = 1.0 / e
        //System.out.println("output je " + this.output);
    }
}