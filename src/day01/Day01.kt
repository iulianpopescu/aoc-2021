package day01

import readInput

private const val DAY = "day01"

fun main() {

    fun part1(input: List<Int>) = input.windowed(2).count { (a, b) -> a < b }

    fun part2(input: List<Int>) = input.windowed(4).count { it[0] < it[3] }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput.map { it.toInt() }) == 7)
    check(part2(testInput.map { it.toInt() }) == 5)

    val input = readInput("$DAY/Test")
    println(part1(input.map { it.toInt() }))
    println(part2(input.map { it.toInt() }))
}
