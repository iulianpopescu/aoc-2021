package day07

import readInput
import kotlin.math.abs

private const val DAY = "day07"

fun main() {
    fun computeMinWithOperation(
        list: List<Int>,
        min: Int,
        max: Int,
        op: (item: Int, position: Int) -> Int
    ): Int {
        val fuelCount = Array(max - min + 1) { 0 }
        var minFuel = Int.MAX_VALUE
        for (i in list.indices) {
            fuelCount[i] = list.fold(0) { total, it ->
                total + op(it, i)
            }
            minFuel = minOf(minFuel, fuelCount[i])
        }
        return minFuel
    }

    fun part1(input: List<Int>, min: Int, max: Int) = computeMinWithOperation(input, min, max) { item, position ->
        abs(item - position)
    }

    fun part2(input: List<Int>, min: Int, max: Int) = computeMinWithOperation(input, min, max) { item, position ->
        val diffIndex = abs(item - position)
        diffIndex * (diffIndex + 1) / 2
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").let {
        val (input, min, max) = it.formatted()
        check(part1(input, min, max) == 37)
        check(part2(input, min, max) == 168)
    }

    readInput("$DAY/Test").let {
        val (input, min, max) = it.formatted()
        println(part1(input, min, max))
        println(part2(input, min, max))
    }
}

private fun List<String>.formatted() = this.first().split(',').let { entry ->
    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    val items = entry.map {
        val number = it.toInt()
        min = minOf(min, number)
        max = maxOf(max, number)
        number
    }
    Triple(items, min, max)
}