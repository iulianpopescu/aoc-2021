package day10

import readInput

private const val DAY = "day10"

fun main() {

    fun part1(input: List<String>) = input.fold(0) { total, parentheses ->
        total + parentheses.getCorruptedScore()
    }

    fun part2(input: List<String>) = input
        .flatMap { if (it.getIncompleteScore() != 0L) listOf(it.getIncompleteScore()) else listOf() }
        .sorted()
        .let { it[it.size / 2] }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("$DAY/Test")
    println(part1(input))
    println(part2(input))
}

private fun String.getCorruptedScore() = this.getSequenceType().let {
    val corruptedScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    if (it.first == Type.CORRUPTED) corruptedScores[it.second.single()]!!.toInt() else 0
}

private fun String.getIncompleteScore() = this.getSequenceType().let {
    val incompleteScores = mapOf(
        '(' to 1,
        '[' to 2,
        '{' to 3,
        '<' to 4
    )
    if (it.first == Type.INCOMPLETE) {
        it.second.reversed().fold(0L) { total, chr ->
            total * 5 + incompleteScores[chr]!!
        }
    } else 0
}

private fun String.getSequenceType(): Pair<Type, String> {
    val pairs = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )
    val queue = ArrayDeque<Char>()
    this.forEach {
        when (it) {
            '(', '[', '{', '<' -> queue.add(it)
            ')', ']', '}', '>' -> {
                if (pairs[it] == queue.last()) {
                    queue.removeLast()
                } else {
                    return Type.CORRUPTED to it.toString()
                }
            }
        }
    }
    return Type.INCOMPLETE to queue.joinToString("")
}

private enum class Type {
    INCOMPLETE,
    CORRUPTED
}