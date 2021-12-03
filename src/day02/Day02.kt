package day02

import readInput
import kotlin.math.max

fun main() {
    fun part1(input: List<Pair<String, Int>>): Int {
        var depth = 0
        var hPosition = 0

        input.forEach {
            when(it.first) {
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
    val testInput = readInput("day02/Day02_test")
    val input = testInput.map {
        val (action, value) = it.split(" ")
        Pair(action, value.toInt())
    }
    check(part1(input) == 150)
    check(part2(input) == 900)

    val realInput = readInput("day02/Day02")
    val iinn = realInput.map {
        val (action, value) = it.split(" ")
        Pair(action, value.toInt())
    }
    println(part1(iinn))
    println(part2(iinn))
}
