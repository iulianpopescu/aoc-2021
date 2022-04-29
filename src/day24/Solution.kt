@file:Suppress("CovariantEquals")

package day24

import readInput
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty0

private const val DAY = "day24"

var w = Operand.Variable(0)
var x = Operand.Variable(0)
var y = Operand.Variable(0)
var z = Operand.Variable(0)

fun main() {
    fun part1(input: List<Instructions>): Int {
        fun executeOperation(op: Instructions.Operation) {
            val (left, operation, right) = op
            if (right is Operand.Constant) {
                left.set(operation(left.get(), right))
            } else {
                left.set(operation(left.get(), (right as KMutableProperty0<Operand.Variable>).get()))
            }
        }

        fun evaluate(modelNumber: String) {
            w = Operand.Variable(0)
            x = Operand.Variable(0)
            y = Operand.Variable(0)
            z = Operand.Variable(0)
            var index = 0

            input.forEach {
                if (it is Instructions.Input) {
                    it.operand.set(Operand.Variable(modelNumber[index].digitToInt()))
                    index++
                } else if (it is Instructions.Operation){
                    executeOperation(it)
                }
            }

            if (z.value == 0) println(modelNumber)
        }

        fun generatePermutations(perm: String) {
            if (perm.length == 14) {
                evaluate(perm)
            } else {
                for (i in 9 downTo 1) {
                    generatePermutations(perm + i)
                }
            }
        }

        generatePermutations("")
        return 0
    }

    fun part2(input: List<Int>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("$DAY/ExampleTest")
//    check(part1(testInput.formatted()) == 7)
//    check(part2(testInput.formatted()) == 5)

    readInput("$DAY/Test").formatted().let {
        println(part1(it))
//        println(part2(it))
    }
}

sealed class Instructions {
    data class Input(val operand: KMutableProperty0<Operand.Variable>): Instructions()
    data class Operation(
        val leftOperand: KMutableProperty0<Operand.Variable>,
        val operation: KFunction2<Operand, Operand, Operand.Variable>,
        val rightOperand: Any
    ): Instructions()

    companion object {
        fun parse(line: String) = if (line.contains("inp")) {
            val (_, right) = line.split(' ')
            Input(Operand.parseVariable(right))
        } else {
            val (operation, left, right) = line.split(' ')
            val instruction = when(operation) {
                "mul" -> Operand::times
                "add" -> Operand::plus
                "div" -> Operand::div
                "mod" -> Operand::rem
                "eql" -> Operand::equals
                else -> throw Exception("Illegal operation")
            }

            val rightOp = if (Operand.isVariable(right)) {
                Operand.parseVariable(right)
            } else {
                Operand.parseConstant(right)
            }
            Operation(Operand.parseVariable(left), instruction, rightOp)
        }
    }
}

sealed class Operand(val value: Int) {
    class Variable(value: Int): Operand(value)
    class Constant(value: Int): Operand(value)

    operator fun plus(other: Operand) = Variable(this.value + other.value)

    operator fun times(other: Operand) = Variable(this.value * other.value)

    operator fun div(other: Operand) = Variable(this.value / other.value)

    operator fun rem(other: Operand) = Variable(this.value % other.value)

    fun equals(other: Operand) = Variable( if (this.value == other.value) 1 else 0 )

    companion object {
        fun isVariable(string: String) = listOf("w", "x", "y", "z").any { it == string }

        fun parseVariable(string: String) = when(string) {
            "w" -> ::w
            "x" -> ::x
            "y" -> ::y
            "z" -> ::z
            else -> throw Exception("No valid variable")
        }

        fun parseConstant(string: String) = Constant(string.toInt())
    }
}

private fun List<String>.formatted() = this.map { Instructions.parse(it) }