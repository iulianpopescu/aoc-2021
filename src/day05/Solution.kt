package day05

import readInput

private const val DAY = "day05"

fun main() {

    fun part1(input: List<Line>): Int {
        val maxX = input.maxOf { maxOf(it.x1, it.x2) }
        val maxY = input.maxOf { maxOf(it.y1, it.y2) }
        return Grid(maxX, maxY, false).also {
            it.drawLines(input)
        }.countCrosses()
    }

    fun part2(input: List<Line>): Int {
        val maxX = input.maxOf { maxOf(it.x1, it.x2) }
        val maxY = input.maxOf { maxOf(it.y1, it.y2) }
        return Grid(maxX, maxY, true).also {
            it.drawLines(input)
        }.countCrosses()
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it) == 5)
        check(part2(it) == 12)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it))
        println(part2(it))
    }
}

private class Grid(
    n: Int,
    m: Int,
    private val drawsOblique: Boolean
) {
    val crosses = Array(n + 1) { Array(m + 1) { 0 } }

    fun countCrosses() = crosses.fold(0) { total, line ->
        total + line.fold(0) { subtotal, item ->
            val toSum = if (item > 1) 1 else 0
            subtotal + toSum
        }
    }

    fun drawLines(lines: List<Line>) {
        lines.forEach { line ->
            if (line.x1 == line.x2) {
                drawHorizontalLine(line)
            } else if (line.y1 == line.y2) {
                drawVerticalLine(line)
            } else if (drawsOblique) {
                drawObliqueLine(line)
            }
        }
    }

    private fun drawHorizontalLine(line: Line) {
        for (i in minOf(line.y1, line.y2)..maxOf(line.y1, line.y2)) {
            crosses[i][line.x1]++
        }
    }

    private fun drawVerticalLine(line: Line) {
        for (i in minOf(line.x1, line.x2)..maxOf(line.x1, line.x2)) {
            crosses[line.y1][i]++
        }
    }

    private fun drawObliqueLine(line: Line) {
        val stepX = if (line.x1 > line.x2) -1 else +1
        val stepY = if (line.y1 > line.y2) -1 else +1
        var indexX = line.x1
        var indexY = line.y1
        crosses[indexY][indexX]++
        do {
            indexX += stepX
            indexY += stepY
            crosses[indexY][indexX]++
        } while (indexX != line.x2 && indexY != line.y2)
    }
}

private data class Line(
    val x1: Int,
    val x2: Int,
    val y1: Int,
    val y2: Int
)

private fun List<String>.formatted() = this.map {
    val ends = it.split(" -> ")
    val (x1, y1) = ends.first().split(',').map { it.toInt() }
    val (x2, y2) = ends.last().split(',').map { it.toInt() }
    Line(x1, x2, y1, y2)
}