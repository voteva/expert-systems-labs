package ru.bmstu.labs.anfis

import ru.bmstu.labs.anfis.model.Conclusion
import ru.bmstu.labs.anfis.model.Rule
import ru.bmstu.labs.anfis.model.RuleUnit
import ru.bmstu.labs.anfis.model.Vector
import java.lang.Double.isNaN
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.exitProcess

class AnfisAlgorithm(rulesNum: Int) {
    var output: Double
        private set

    private var rules: Array<Rule?>
    private var error: Double
    private var eta: Double

    private val sumOfWeights: Double
        get() {
            var sumOfWeights = 0.0
            for (rule in rules) {
                sumOfWeights += rule!!.output
            }
            return sumOfWeights
        }

    fun train(vectors: List<Vector>, epochNum: Int = 10000) {
        println("Training starts..\n")

        for (i in 0..epochNum) {
            println("Epoch: ${i + 1}")

            for (vector in vectors) {
                predict(vector.x, vector.y)
                backpropagate(vector.x, vector.y, vector.output)
            }
            update()
            writeAndResetError(vectors.size)
        }

        println("Training completed\n")
    }

    fun predict(x: Double, y: Double) {
        var overall = 0.0

        for (rule in rules) {
            // вычисляется результат для функций принадлежности mA и mB
            val mA = rule!!.a
            mA.calculate(x)
            val mB = rule.b
            mB.calculate(y)
            // степень выполнения правила расчитывается как произведение входных сигналов
            rule.output = mA.output * mB.output

            if (isNaN(mA.output) || isNaN(mB.output)) {
                exitProcess(0)
            }

            rule.conclusion.conclude(x, y)
            overall += rule.output * rule.conclusion.conclusion
        }

        overall /= sumOfWeights
        output = overall
    }

    private fun backpropagate(x: Double, y: Double, output: Double) {
        error += (output - this.output).pow(2)
        val sum = sumOfWeights
        for (i in rules.indices) {
            val mA = rules[i]!!.a
            val mB = rules[i]!!.b
            val zDiff = getSumOfZ(i)
            // update ruleunit A
            var a = mA.a
            var b = mA.b
            var gradA = eta * (output - this.output) * mB.output * (zDiff / sum.pow(2)) * b * mA.output * (1 - mA.output)
            var gradB = eta * (output - this.output) * mB.output * (zDiff / sum.pow(2)) * (x - a) * mA.output * (1 - mA.output)
            mA.gradA = mA.gradA + gradA
            mA.gradB = mA.gradB + gradB
            // update ruleunit B
            a = mB.a
            b = mB.b
            gradA = eta * (output - this.output) * mA.output * (zDiff / sum.pow(2)) * b * mB.output * (1 - mB.output)
            gradB = eta * (output - this.output) * mA.output * (zDiff / sum.pow(2)) * (y - a) * mB.output * (1 - mB.output)
            mB.gradA = mB.gradA + gradA
            mB.gradB = mB.gradB + gradB
            val c = rules[i]!!.conclusion
            val gradP = eta * (output - this.output) * rules[i]!!.output / sumOfWeights * x
            val gradQ = eta * (output - this.output) * rules[i]!!.output / sumOfWeights * y
            val gradR = eta * (output - this.output) * rules[i]!!.output / sumOfWeights
            c.gradP = c.gradP + gradP
            c.gradQ = c.gradQ + gradQ
            c.gradR = c.gradR + gradR
        }
    }

    private fun update() {
        for (rule in rules) {
            // update rule A - a
            rule!!.a.a = rule.a.a + rule.a.gradA
            rule.a.gradA = 0.0
            // update rule A - b
            rule.a.b = rule.a.b - rule.a.gradB
            rule.a.gradB = 0.0
            // update rule B - a
            rule.b.a = rule.b.a + rule.b.gradA
            rule.b.gradA = 0.0
            // update rule B - b
            rule.b.b = rule.b.b - rule.b.gradB
            rule.b.gradB = 0.0
            // update conclusion
            rule.conclusion.p = rule.conclusion.p + rule.conclusion.gradP
            rule.conclusion.gradP = 0.0
            rule.conclusion.q = rule.conclusion.q + rule.conclusion.gradQ
            rule.conclusion.gradQ = 0.0
            rule.conclusion.r = rule.conclusion.r + rule.conclusion.gradR
            rule.conclusion.gradR = 0.0
        }
    }

    private fun getSumOfZ(i: Int): Double {
        var sum = 0.0
        for (rule in rules) {
            sum += rule!!.output * (rules[i]!!.conclusion.conclusion - rule.conclusion.conclusion)
        }
        return sum
    }

    private fun writeAndResetError(trainingSize: Int) {
        error /= trainingSize.toDouble()
        error = sqrt(error)
        println("Deviation $error\n")
        error = 0.0
    }

    init {
        val millis = System.currentTimeMillis()
        val random = Random(millis)
        rules = arrayOfNulls(rulesNum)
        for (i in 0 until rulesNum) {
            rules[i] = Rule(
                    RuleUnit(random.nextDouble(), random.nextDouble()),
                    RuleUnit(random.nextDouble(), random.nextDouble()),
                    Conclusion(random.nextDouble(), random.nextDouble(), random.nextDouble()))
        }
        output = 0.0
        error = 0.0
        eta = 0.001
    }
}