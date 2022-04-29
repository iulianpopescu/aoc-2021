package day21

import readInput
import kotlin.math.min

private const val DAY = "day21"
private const val LIMIT = 1_000
private const val LOWER_LIMIT = 21

fun main() {

    fun getScoreForPosition(start: Int, rolls: Int): Int {
        return (start - 1 + rolls) % 10 + 1
    }

    fun numberRolled(rolls: Int): Int {
        var count = rolls % 100
        var sum = 0
        repeat(3) {
            count++
            if (count > 100) {
                count = 1
            }
            sum += count
        }
        return sum
    }

    fun part1(first: Int, second: Int): Int {
        var firstPosition = first
        var firstScore = 0
        var secondPosition = second
        var secondScore = 0
        var rolls = 0
        while (firstScore < LIMIT && secondScore < LIMIT) {
            firstPosition = getScoreForPosition(firstPosition, numberRolled(rolls))
            rolls += 3
            firstScore += firstPosition
            if (firstScore >= LIMIT) break
            secondPosition = getScoreForPosition(secondPosition, numberRolled(rolls))
            rolls += 3
            secondScore += secondPosition
        }
        return min(firstScore, secondScore) * rolls
    }

    fun part2(firstPosition: Int, secondPosition: Int, firstScore: Long = 0L, secondScore: Long = 0L, turn: Boolean = true): Pair<Long, Long> {
        if (firstScore >= LOWER_LIMIT) return 1L to 0L
        if (secondScore >= LOWER_LIMIT) return 0L to 1L

        var result = Pair(0L, 0L)
        for (i in 1..3) {
            result += if (turn) {
                val jump = getScoreForPosition(firstPosition, i)
                part2(jump, secondPosition, firstScore + jump, secondScore, !turn)
            } else {
                val jump = getScoreForPosition(secondPosition, i)
                part2(firstPosition, jump, firstScore, secondScore + jump, !turn)
            }
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it.first, it.second) == 739785)
//        check(part2(it.first, it.second).max() == 444356092776315)
    }
    readInput("$DAY/Test").formatted().let {
        println(part1(it.first, it.second))
        println(Day21(readInput("$DAY/Test")).solvePart2())
    }
}

private fun List<String>.formatted(): Pair<Int, Int> {
    val first = this.first().split(": ").last().toInt()
    val second = this.last().split(": ").last().toInt()
    return first to second
}

private operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> {
    return Pair(
        this.first + other.first,
        this.second + other.second
    )
}

private fun Pair<Long, Long>.max(): Long {
    println(this)
    return maxOf(this.first, this.second)
}

class Day21(input: List<String>) {

    private val player1Start: Int = input.first().substringAfterLast(" ").toInt()
    private val player2Start: Int = input.last().substringAfterLast(" ").toInt()
    private val initialGameState = GameState(PlayerState(player1Start), PlayerState(player2Start))

    private val quantumDieFrequency: Map<Int, Long> = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
    private val stateMemory: MutableMap<GameState, WinCounts> = mutableMapOf()

    fun solvePart1(): Int {
        var game = initialGameState
        val die = DeterministicDie()
        while (!game.isWinner()) {
            game = game.next(die.roll())
        }
        return game.minScore() * die.rolls
    }

    fun solvePart2(): Long =
        playQuantum().max()

    private fun playQuantum(state: GameState = initialGameState): WinCounts =
        when {
            state.isWinner(21) ->
                if (state.player1.score > state.player2.score) WinCounts(1, 0) else WinCounts(0, 1)
            state in stateMemory ->
                stateMemory.getValue(state)
            else ->
                quantumDieFrequency.map { (die, frequency) ->
                    playQuantum(state.next(die)) * frequency
                }.reduce { a, b -> a + b }.also { stateMemory[state] = it }
        }

    private class DeterministicDie(var value: Int = 0, var rolls: Int = 0) {
        fun roll(): Int {
            rolls += 3
            value += 3
            return 3 * value - 3
        }
    }

    private class WinCounts(val player1: Long, val player2: Long) {
        operator fun plus(other: WinCounts): WinCounts =
            WinCounts(player1 + other.player1, player2 + other.player2)

        operator fun times(other: Long): WinCounts =
            WinCounts(player1 * other, player2 * other)

        fun max(): Long =
            maxOf(player1, player2)
    }

    private data class GameState(val player1: PlayerState, val player2: PlayerState, val player1Turn: Boolean = true) {
        fun next(die: Int): GameState =
            GameState(
                if (player1Turn) player1.next(die) else player1,
                if (!player1Turn) player2.next(die) else player2,
                player1Turn = !player1Turn
            )

        fun isWinner(scoreNeeded: Int = 1000): Boolean =
            player1.score >= scoreNeeded || player2.score >= scoreNeeded

        fun minScore(): Int =
            minOf(player1.score, player2.score)
    }

    private data class PlayerState(val place: Int, val score: Int = 0) {
        fun next(die: Int): PlayerState {
            val nextPlace = (place + die - 1) % 10 + 1

            return PlayerState(
                place = nextPlace,
                score = score + nextPlace
            )
        }
    }

}