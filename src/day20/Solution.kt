package day20

import readInput
import kotlin.math.absoluteValue
import kotlin.math.sign

private const val DAY = "day20"

fun main() {

    fun part1(input: List<String>): Int {
        return Day20(input).solvePart1()
    }

    fun part2(input: List<String>): Int {
        return Day20(input).solvePart2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)


    val input = readInput("$DAY/Test")
    println(part1(input))
    println(part2(input))
}

class Day20(input: List<String>) {

    private val algorithmString: String = input.first().map { if (it == '#') '1' else '0' }.joinToString("")
    private val startingImage: List<String> = parseInput(input)

    fun solvePart1(): Int =
        solve(2)

    fun solvePart2(): Int =
        solve(50)

    private fun solve(rounds: Int): Int =
        (1 .. rounds).fold(startingImage to '0') { (image, defaultValue), _ ->
            image.nextRound(defaultValue) to if (defaultValue == '0') algorithmString.first() else algorithmString.last()
        }.first.sumOf { it.count { char -> char == '1' } }

    private fun List<String>.nextRound(defaultValue: Char): List<String> =
        (-1..this.size).map { y ->
            (-1..this.first().length).map { x ->
                algorithmString[
                        Point2d(x, y)
                            .surrounding()
                            .map { this.valueAt(it, defaultValue) }
                            .joinToString("")
                            .toInt(2)
                ]
            }.joinToString("")
        }

    private fun List<String>.valueAt(pixel: Point2d, defaultValue: Char): Char =
        if (pixel in this) this[pixel.y][pixel.x] else defaultValue

    private operator fun List<String>.contains(pixel: Point2d): Boolean =
        pixel.y in this.indices && pixel.x in this.first().indices

    private fun Point2d.surrounding(): List<Point2d> =
        listOf(
            Point2d(x - 1, y - 1), Point2d(x, y - 1), Point2d(x + 1, y - 1),
            Point2d(x - 1, y), this, Point2d(x + 1, y),
            Point2d(x - 1, y + 1), Point2d(x, y + 1), Point2d(x + 1, y + 1),
        )

    private fun parseInput(input: List<String>): List<String> =
        input.drop(2).map { row ->
            row.map { char ->
                if (char == '#') 1 else 0
            }.joinToString("")
        }
}

data class Point2d(val x: Int, val y: Int) {

    infix fun sharesAxisWith(that: Point2d): Boolean =
        x == that.x || y == that.y

    infix fun lineTo(that: Point2d): List<Point2d> {
        val xDelta = (that.x - x).sign
        val yDelta = (that.y - y).sign
        val steps = maxOf((x - that.x).absoluteValue, (y - that.y).absoluteValue)
        return (1..steps).scan(this) { last, _ -> Point2d(last.x + xDelta, last.y + yDelta) }
    }

    fun neighbors(): List<Point2d> =
        listOf(
            Point2d(x, y + 1),
            Point2d(x, y - 1),
            Point2d(x + 1, y),
            Point2d(x - 1, y)
        )

    fun allNeighbors(): List<Point2d> =
        neighbors() + listOf(
            Point2d(x - 1, y - 1),
            Point2d(x - 1, y + 1),
            Point2d(x + 1, y - 1),
            Point2d(x + 1, y + 1)
        )
}

data class Point3d(val x: Int, val y: Int, val z: Int) {

    infix fun distanceTo(other: Point3d): Int =
        (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue + (this.z - other.z).absoluteValue

    operator fun plus(other: Point3d): Point3d =
        Point3d(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point3d): Point3d =
        Point3d(x - other.x, y - other.y, z - other.z)

    fun face(facing: Int): Point3d =
        when (facing) {
            0 -> this
            1 -> Point3d(x, -y, -z)
            2 -> Point3d(x, -z, y)
            3 -> Point3d(-y, -z, x)
            4 -> Point3d(y, -z, -x)
            5 -> Point3d(-x, -z, -y)
            else -> error("Invalid facing")
        }

    fun rotate(rotating: Int): Point3d =
        when (rotating) {
            0 -> this
            1 -> Point3d(-y, x, z)
            2 -> Point3d(-x, -y, z)
            3 -> Point3d(y, -x, z)
            else -> error("Invalid rotation")
        }

    companion object {
        fun of(rawInput: String): Point3d =
            rawInput.split(",").let { part ->
                Point3d(part[0].toInt(), part[1].toInt(), part[2].toInt())
            }
    }

}


private fun List<String>.formatted() = this.map { it.toInt() }