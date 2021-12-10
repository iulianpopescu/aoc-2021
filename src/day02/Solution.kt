package day02

import readInput
import kotlin.math.max

private const val DAY = "day02"

fun main() {
    fun part1(input: List<Pair<String, Int>>): Int {
        var depth = 0
        var hPosition = 0

        input.forEach {
            when (it.first) {
                "up" -> depth = max(depth - it.second, 0)
                "down" -> depth += it.second
                "forward" -> hPosition += it.second
            }
        }
        return depth * hPosition
    }

    fun part2(input: List<Pair<String, Int>>): Int {
        var depth = 0
        var hPosition = 0
        var aim = 0

        input.forEach {
            when(it.first) {
                "up" -> aim = max(aim - it.second, 0)
                "down" -> aim += it.second
                "forward" -> {
                    hPosition += it.second
                    depth += (aim * it.second)
                }
            }
        }
        return depth * hPosition
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput.formatted()) == 150)
    check(part2(testInput.formatted()) == 900)

    val input = readInput("$DAY/Test")
    println(part1(input.formatted()))
    println(part2(input.formatted()))
}

private fun List<String>.formatted() = this.map {
    val (action, value) = it.split(" ")
    action to value.toInt()
}
