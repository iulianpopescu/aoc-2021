package day11

import readInput

private const val DAY = "day11"

fun main() {

    val directions = listOf(
        1 to 0,
        -1 to 0,
        0 to 1,
        0 to -1,
        1 to 1,
        1 to -1,
        -1 to 1,
        -1 to -1
    )

    fun part1(input: Array<Array<Int>>, steps: Int): Int {
        var flashes = 0
        val queue = ArrayDeque<Pair<Int, Int>>()
        val flashed = Array(input.size) { Array(input.first().size) { false } }
        repeat(steps) {
            for (i in input.indices) {
                for (j in input[i].indices) {
                    input[i][j]++
                    if (input[i][j] > 9) {
                        queue.add(i to j)
                    }
                }
            }
            while (queue.isNotEmpty()) {
                val (x, y) = queue.removeFirst()
                if (!flashed[x][y]) {
                    if (input[x][y] > 9) {
                        flashes++
                        flashed[x][y] = true
                        input[x][y] = 0
                        directions.forEach { (dx, dy) ->
                            val newX = x + dx
                            val newY = y + dy
                            if (newX >= 0 && newY >= 0 && newX < input.size && newY < input.first().size) {
                                queue.add(newX to newY)
                            }
                        }
                    } else input[x][y]++

                    if (input[x][y] > 9) {
                        queue.add(x to y)
                    }
                }
            }

            for (i in flashed.indices) {
                for (j in flashed[i].indices) {
                    flashed[i][j] = false
                }
            }
        }
        return flashes
    }

    fun part2(input: Array<Array<Int>>): Int {
        var step = 1
        while (true) {
            var flashes = 0
            val queue = ArrayDeque<Pair<Int, Int>>()
            val flashed = Array(input.size) { Array(input.first().size) { false } }
            for (i in input.indices) {
                for (j in input[i].indices) {
                    input[i][j]++
                    if (input[i][j] > 9) {
                        queue.add(i to j)
                    }
                }
            }
            while (queue.isNotEmpty()) {
                val (x, y) = queue.removeFirst()
                if (!flashed[x][y]) {
                    if (input[x][y] > 9) {
                        flashes++
                        if (flashes == input.size * input.first().size) return step

                        flashed[x][y] = true
                        input[x][y] = 0
                        directions.forEach { (dx, dy) ->
                            val newX = x + dx
                            val newY = y + dy
                            if (newX >= 0 && newY >= 0 && newX < input.size && newY < input.first().size) {
                                queue.add(newX to newY)
                            }
                        }
                    } else input[x][y]++

                    if (input[x][y] > 9) {
                        queue.add(x to y)
                    }
                }
            }

            for (i in flashed.indices) {
                for (j in flashed[i].indices) {
                    flashed[i][j] = false
                }
            }
            step++
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput.formatted(), 100) == 1656)
    check(part2(testInput.formatted()) == 195)

    val input = readInput("$DAY/Test")
    println(part1(input.formatted(), 100))
    println(part2(input.formatted()))
}

private fun List<String>.formatted() = Array(this.size) { x -> Array(this[x].length) { y -> this[x][y].digitToInt() } }
