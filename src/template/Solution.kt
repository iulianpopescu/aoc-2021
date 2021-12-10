package template

import readInput

private const val DAY = ""

fun main() {

    fun part1(input: List<Int>): Int {
        return 0
    }

    fun part2(input: List<Int>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput.formatted()) == 7)
    check(part2(testInput.formatted()) == 5)

    val input = readInput("$DAY/Test")
    println(part1(input.formatted()))
    println(part2(input.formatted()))
}

private fun List<String>.formatted() = this.map { it.toInt() }