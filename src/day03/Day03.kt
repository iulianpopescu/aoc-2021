package day03

import readInput
import kotlin.math.pow

fun main() {

    fun countZeroes(input: List<String>, position: Int): Int {
        var zeroCount = 0
        input.forEach {
            if (it[position] == '0') {
                zeroCount++
            }
        }
        return zeroCount
    }

    fun part1(input: List<String>): Int {
        var gamma = 0
        var epsilon = 0
        val itemLength = input.first().length
        for (i in 0 until itemLength) {
            var zeroCount = 0
            input.forEach {
                if (it[i] == '0') {
                    zeroCount++
                }
            }
            val oneCount = input.size - zeroCount
            if (zeroCount >= oneCount) {
                // no need to update gamma
                epsilon += 2.0.pow(itemLength - i -1).toInt()
            } else {
                gamma += 2.0.pow(itemLength - i -1).toInt()
            }
        }

        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val oxygenRatingList = mutableListOf<String>().apply { addAll(input) }
        val coRatingList = mutableListOf<String>().apply { addAll(input) }
        var index = 0
        while (oxygenRatingList.size > 1) {
            val zeroCount = countZeroes(oxygenRatingList, index)
            val oneCount = oxygenRatingList.size - zeroCount
            if (zeroCount > oneCount) {
                oxygenRatingList.removeIf { it[index] == '1' }
            } else {
                oxygenRatingList.removeIf { it[index] == '0' }
            }
            index++
        }

        index = 0
        while (coRatingList.size > 1) {
            val zeroCount = countZeroes(coRatingList, index)
            val oneCount = coRatingList.size - zeroCount
            if (zeroCount <= oneCount) {
                coRatingList.removeIf { it[index] == '1' }
            } else {
                coRatingList.removeIf { it[index] == '0' }
            }
            index++
        }

        val itemLength = oxygenRatingList.first().length
        var oxygenRating = 0
        oxygenRatingList.first().forEachIndexed { index, it ->
            oxygenRating += it.digitToInt() * 2.0.pow(itemLength - index -1).toInt()
        }

        var coRating = 0
        coRatingList.first().forEachIndexed { index, it ->
            coRating += it.digitToInt() * 2.0.pow(itemLength - index -1).toInt()
        }

        return oxygenRating * coRating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val realInput = readInput("day03/Day03")
    println(part1(realInput))
    println(part2(realInput))
}
