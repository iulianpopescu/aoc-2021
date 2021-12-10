package day03

import readInput
import kotlin.math.pow

private const val DAY = "day03"

fun main() {
    fun part1(input: List<String>): Int {
        val entryLength = input.first().length
        var gamma = 0
        var epsilon = 0
        for (i in 0 until entryLength) {
            val zeroCount = input.countZeroesAtPosition(i)
            val oneCount = input.size - zeroCount
            if (zeroCount >= oneCount) {
                epsilon += 2.0.pow(entryLength - i - 1).toInt()
            } else {
                gamma += 2.0.pow(entryLength - i - 1).toInt()
            }
        }
        return gamma * epsilon
    }

    fun getLastItem(
        list: List<String>,
        condition: (zeros: Int, ones: Int) -> Boolean
    ): String {
        val listCopy = mutableListOf<String>().apply { addAll(list) }
        var index = 0
        while (listCopy.size > 1) {
            val zeroCount = listCopy.countZeroesAtPosition(index)
            val oneCount = listCopy.size - zeroCount
            if (condition(zeroCount, oneCount)) {
                listCopy.removeIf { it[index] == '1' }
            } else {
                listCopy.removeIf { it[index] == '0' }
            }
            index++
        }
        return listCopy.single()
    }

    fun part2(input: List<String>): Int {
        val oxygenRating = getLastItem(input) { zeros, ones -> zeros > ones }
        val coRating = getLastItem(input) { zeros, ones -> zeros <= ones }
        return oxygenRating.toInt(2) * coRating.toInt(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("$DAY/Test")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.countZeroesAtPosition(position: Int) = this.count { it[position] == '0' }