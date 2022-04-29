package day14

import readInput
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.math.absoluteValue


private const val DAY = "day14"

fun main() {

    tailrec fun computePolymer(
        polymer: String,
        input: Map<String, Char>,
        currentStep: Int = 0,
        steps: Int
    ): String {
        if (currentStep == steps) return polymer

        return computePolymer(buildString {
            polymer.windowed(2).forEach {
                append(it.first())
                if (input[it] != null) {
                    val middle = input[it]
                    append(middle)
                }
            }
            append(polymer.last())
        }, input, currentStep+1, steps)
    }

    fun part1(polymer: String, input: Map<String, Char>): Int {
        val finalPolymer = computePolymer(polymer, input, steps = 10)
        val counts = HashMap<Char, Int>()
        finalPolymer.forEach {
            val newCount = counts[it] ?: 0
            counts[it] = newCount + 1
        }
        val maxCount = counts.values.maxOf { it }
        val minCount = counts.values.minOf { it }

        ""[2].isLetterOrDigit()
        return maxCount - minCount
    }

    fun part2(polymer: String, input: Map<String, Char>): Long {
        var pairs = polymer.windowed(2)
            .groupBy { it }
            .mapValues { (_, list) -> list.size.toLong() } as HashMap<String, Long>
        val letters: HashMap<Char, Long> = polymer.groupBy { it }
            .toMap()
            .mapValues { (_, v) -> v.size.toLong() } as HashMap<Char, Long> /* = java.util.HashMap<kotlin.Char, kotlin.Long> */

        for (i in 0 until 40) {
            val np = HashMap<String, Long>()
            pairs.forEach { (key, value) ->
                val m = input[key]!!
//                println(np)
                np.merge("${key.first()}$m", value) { a, b -> a + b }
//                println(np)
                np.merge("$m${key.last()}", value) { a, b -> a + b }
                letters.merge(m, value) { a, b -> a + b }
            }
            pairs = np
        }
        val sortedCounts = letters.values.sortedDescending()
        return sortedCounts.first() - sortedCounts.last()
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it.first, it.second) == 1588)
        check(part2(it.first, it.second) == 2188189693529L)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it.first, it.second))
        println(part2(it.first, it.second))
    }
}

private fun List<String>.formatted(): Pair<String, Map<String, Char>> {
    val map = mutableMapOf<String, Char>()
    for (i in 2 until this.size) {
        val (key, value) = this[i].split(" -> ")
        map[key] = value.single()
    }
    return this.first() to map
}