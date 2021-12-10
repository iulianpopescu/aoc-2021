package day09

import readInput
import java.util.*

private const val DAY = "day09"

private val dx = listOf(0, 0, 1, -1)
private val dy = listOf(1, -1, 0, 0)

fun main() {
    fun part1(board: Board) = board.table.foldIndexed(0) { i, total, line ->
        total + line.foldIndexed(0) { j, subtotal, item ->
            val toSum = if (board.isLowPoint(i, j)) item + 1 else 0
            subtotal + toSum
        }
    }

    fun part2(board: Board): Int {
        for (i in board.table.indices) {
            for (j in board.table[i].indices) {
                if (board.isLowPoint(i, j)) {
                    board.fillBasin(i, j)
                }
            }
        }
        return board.basins.sortedDescending().take(3).fold(1) { total, it -> total * it }
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").let {
        check(part1(it.formatted()) == 15)
        check(part2(it.formatted()) == 1134)
    }

    readInput("$DAY/Test").let {
        println(part1(it.formatted()))
        println(part2(it.formatted()))
    }
}

private class Board(val table: Array<Array<Int>>) {

    fun isLowPoint(i: Int, j: Int): Boolean {
        var higherPoints = 0
        var neighbours = 0
        for (k in dx.indices) {
            val newX = i + dx[k]
            val newY = j + dy[k]

            if (this.containsPosition(newX, newY)) {
                neighbours++
                if (table[newX][newY] > table[i][j]) {
                    higherPoints++
                }
            }
        }
        return neighbours == higherPoints
    }

    fun containsPosition(i: Int, j: Int) = i >= 0 && i < table.size && j >= 0 && j < table.first().size

    private val visited = Array(table.size) { Array(table.first().size) { false } }
    val basins = mutableListOf<Int>()

    fun fillBasin(i: Int, j: Int) {
        var basinSize = 0
        visited[i][j] = true
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(i to j)
        while (queue.isNotEmpty()) {
            val top = queue.pop()
            basinSize++
            for (k in dx.indices) {
                val newX = top.first + dx[k]
                val newY = top.second + dy[k]
                if (containsPosition(newX, newY)
                    && table[newX][newY] != 9
                    && table[top.first][top.second] < table[newX][newY]
                    && !visited[newX][newY]
                ) {
                    queue.add(newX to newY)
                    visited[newX][newY] = true
                }
            }
        }
        basins.add(basinSize)
    }
}

private fun List<String>.formatted() = Board(Array(this.size) { i ->
    Array(this.first().length) { j ->
        this[i][j].digitToInt()
    }
})