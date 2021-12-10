package day06

import readInput

private const val DAY = "day06"

fun main() {
    fun getFishAfterDays(input: List<Int>, days: Int): String {
        val fish = Array(9) { ULong.MIN_VALUE }
        input.forEach { fish[it]++ }
        repeat(days) {
            val zeroFish = fish[0]
            for (i in 0 until 8) fish[i] = fish[i + 1]
            fish[6] += zeroFish
            fish[8] = zeroFish
        }
        return fish.sum().toString()
    }

    fun part1(input: List<Int>, days: Int) = getFishAfterDays(input, days)

    fun part2(input: List<Int>, days: Int) = getFishAfterDays(input, days)

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it, 80) == "5934")
        check(part2(it, 256) == "26984457539")
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it, 80))
        println(part2(it, 256))
    }
}

private fun List<String>.formatted() = this.single().split(',').map { it.toInt() }
