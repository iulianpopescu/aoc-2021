package day13

import readInput

private const val DAY = "day13"

fun main() {

    fun part1(paper: Paper, folds: List<Fold>): Int {
        folds.first().let { fold ->
            if (fold.horizontal) {
                paper.foldHorizontally(fold.index)
            } else {
                paper.foldVertically(fold.index)
            }
        }
        return paper.countDots()
    }

    fun part2(paper: Paper, folds: List<Fold>): Int {
        folds.forEach { fold ->
            if (fold.horizontal) {
                paper.foldHorizontally(fold.index)
            } else {
                paper.foldVertically(fold.index)
            }
        }
        paper.print()
        return paper.countDots()
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it.first, it.second) == 17)
        check(part2(it.first, it.second) == 26)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it.first, it.second))
        println(part2(it.first, it.second))
    }
}

private fun List<String>.formatted(): Pair<Paper, List<Fold>> {
    val dots = this.takeWhile { it != "" }.map {
        val (x, y) = it.split(',')
        x.toInt() to y.toInt()
    }
    val folds = this.takeLastWhile { it != "" }.map {
        val (orientation, index) = it.split(' ').last().split('=')
        val isHorizontal = orientation == "y"
        Fold(isHorizontal, index.toInt())
    }

    return Pair(Paper(dots), folds)
}

private data class Fold(
    val horizontal: Boolean,
    val index: Int
)

private class Paper(dots: List<Pair<Int, Int>>) {
    private var table: Array<Array<Boolean>>

    init {
        val maxX = dots.maxOf { it.second }
        val maxY = dots.maxOf { it.first }
        table = Array(maxX + 1) { Array(maxY + 1) { false } }
        dots.forEach { table[it.second][it.first] = true }
    }

    fun countDots() = table.fold(0) { total, line ->
        total + line.fold(0) { subtotal, item ->
            val nr = if (item) 1 else 0
            subtotal + nr
        }
    }

    fun print() {
        table.forEach { line ->
            line.forEach {
                val char = if (it) "#" else "."
                print(char)
            }
            println()
        }
    }

    fun foldHorizontally(index: Int) {
        table = Array(index) { i ->
            Array(table.first().size) { j ->
                table[i][j] || table[table.size - i - 1][j]
            }
        }
    }

    fun foldVertically(index: Int) {
        table = Array(table.size) { i ->
            Array(index) { j ->
                table[i][j] || table[i][table.first().size - j - 1]
            }
        }
    }
}