package day04

import readInput

private const val DAY = "day04"

fun main() {
    fun part1(numbers: List<Int>, boards: List<Board>): Int {
        numbers.forEach { x ->
            boards.forEachIndexed { index, board ->
                val (i, j) = board.indexesOf(x)
                if (i != -1 && j != -1) {
                    board.markItem(i, j)
                    if (boards[index].isBoardCompleteForPoint(i, j)) {
                        return x * boards[index].sum()
                    }
                }
            }
        }
        return -1
    }

    fun part2(numbers: List<Int>, boards: List<Board>): Int {
        var boardsCompleted = 0
        numbers.forEach { x ->
            boards.filter { board -> !board.isBoardComplete }
                .forEach { board ->
                    val (i, j) = board.indexesOf(x)
                    if (i != -1 && j != -1) {
                        board.markItem(i, j)
                        if (board.isBoardCompleteForPoint(i, j)) {
                            board.isBoardComplete = true
                            boardsCompleted++
                        }
                        if (boardsCompleted == boards.size) {
                            return x * board.sum()
                        }
                    }
                }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it.first, it.second) == 4512)
        check(part2(it.first, it.second) == 1924)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it.first, it.second))
        println(part2(it.first, it.second))
    }
}

private fun List<String>.formatted(): Pair<List<Int>, List<Board>> {
    fun getInputBoards(input: List<String>): List<Board> {
        val boards = mutableListOf<Board>()
        var step = 2
        while (step < input.size) {
            val currentBoard = Board(Array(5) {
                val values = input[step].split(' ').map { it.toInt() }
                step++
                Array(5) {
                    Pair(values[it], false)
                }
            })
            boards.add(currentBoard)
            step++
        }
        return boards
    }

    return this.first().split(',').map { it.toInt() } to getInputBoards(this)
}

private class Board(private val table: Array<Array<Pair<Int, Boolean>>>) {

    var isBoardComplete = false

    fun isBoardCompleteForPoint(i: Int, j: Int): Boolean {
        val horizontalCompleted = table[i].fold(true) { complete, item -> complete && item.second }
        val verticalCompleted = table.fold(true) { complete, line -> complete && line[j].second }
        return verticalCompleted || horizontalCompleted
    }

    fun indexesOf(x: Int): Pair<Int, Int> {
        for (i in table.indices) {
            for (j in table[i].indices) {
                if (table[i][j].first == x) return i to j
            }
        }
        return -1 to -1
    }

    fun sum() = table.fold(0) { total, line ->
        total + line.fold(0) { subtotal, item ->
            val toSum = if (!item.second) item.first else 0
            subtotal + toSum
        }
    }

    fun markItem(i: Int, j: Int) {
        table[i][j] = table[i][j].first to true
    }
}