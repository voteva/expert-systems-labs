package ru.bmstu.labs.anfis

import ru.bmstu.labs.anfis.model.Conclusion
import ru.bmstu.labs.anfis.model.Rule
import ru.bmstu.labs.anfis.model.RuleUnit
import ru.bmstu.labs.anfis.model.Vector
import java.util.*
import kotlin.system.exitProcess

class AnfisAlgorithm(rulesNum: Int) {
    var rules: Array<Rule?>
        private set
    var output: Double
        private set
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

    fun predict(x: Double, y: Double) {
        // Здесь вычисляются функции принадлежности muA и muB и вычисляется их результат
        for (value in rules) {
            val A = value!!.a
            A.calculate(x)
            val B = value.b
            B.calculate(y)
            value.output = A.output * B.output

            if (java.lang.Double.isNaN(A.output) || java.lang.Double.isNaN(B.output)) {
                exitProcess(0)
            }
        }

        // tu se racuna wi * fi
        var overall = 0.0
        for (rule in rules) {
            rule!!.conclusion.conclude(x, y)
            overall = overall + rule.output * rule.conclusion.conclusion
        }
        // wi * fi se podijeli sa sum_i(wi)
        overall = overall / sumOfWeights
        // izlaz se spremi i ispise
        output = overall
    }

    fun backpropagate(x: Double, y: Double, output: Double) {
        error += Math.pow(output - this.output, 2.0)
        val sum = sumOfWeights
        for (i in rules.indices) {
            val A = rules[i]!!.a
            val B = rules[i]!!.b
            var zDiff = getSumOfZ(i)
            // update ruleunit A
            var a = A.a
            var b = A.b
            var gradA = eta * (output - this.output) * B.output * (zDiff / Math.pow(sum, 2.0)) * b * A.output * (1 - A.output)
            var gradB = eta * (output - this.output) * B.output * (zDiff / Math.pow(sum, 2.0)) * (x - a) * A.output * (1 - A.output)
            A.gradA = A.gradA + gradA
            A.gradB = A.gradB + gradB
            zDiff = getSumOfZ(i)
            // update ruleunit B
            a = B.a
            b = B.b
            gradA = eta * (output - this.output) * A.output * (zDiff / Math.pow(sum, 2.0)) * b * B.output * (1 - B.output)
            gradB = eta * (output - this.output) * A.output * (zDiff / Math.pow(sum, 2.0)) * (y - a) * B.output * (1 - B.output)
            B.gradA = B.gradA + gradA
            B.gradB = B.gradB + gradB
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

    // option = 0 --> batch; option = 1 --> stochastic;
    fun train(vectors: List<Vector>, epochNum: Int = 99999, option: Int = 0) {
        println("Training starts..\n")

        for (i in 0..epochNum) {
            println("Epoch: ${i + 1}")

            for (vector in vectors) {
                predict(vector.x, vector.y)
                backpropagate(vector.x, vector.y, vector.output)
                if (option == 1) update()
            }
            if (option == 0) update()
            writeAndResetError(vectors.size)
        }

        println("Training completed\n")
    }

    private fun writeAndResetError(trainingSize: Int): Double {
        error /= trainingSize.toDouble()
        println("Deviation ${error}\n")
        val err = error
        error = 0.0
        return err
    }

    init {
        val milis = System.currentTimeMillis()
        val random = Random(milis)
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