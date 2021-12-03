package day01

import readInput

fun main() {
    fun part1(input: List<Int>): Int {
        return input.windowed(2).count { (a, b) -> a < b }
    }

    fun part2(input: List<Int>): Int {
        val summedInput = mutableListOf<Int>()
        for (i in 2 until input.size) {
            summedInput.add(
                input[i] + input[i - 1] + input[i - 2]
            )
        }
        return part1(summedInput)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput.map { it.toInt() }) == 7)
    check(part2(testInput.map { it.toInt() }) == 5)

    val input = readInput("day01/Day01")
    println(part1(input.map { it.toInt() }))
    println(part2(input.map { it.toInt() }))
}
