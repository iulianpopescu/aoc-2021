package day15

import readInput

private const val DAY = "day15"

fun main() {
    val directions = listOf(
        1 to 0,
        -1 to 0,
        0 to 1,
        0 to -1
    )

    fun lee(input: Array<Array<Int>>, scale: Int): Int {
        val risk = Array(input.size * scale) { Array(input.first().size * scale) { Int.MAX_VALUE } }
        val queue = ArrayDeque<Pair<Int, Int>>().apply { add(0 to 0) }
        risk[0][0] = 0
        while (queue.isNotEmpty()) {
            val top = queue.removeFirst()
            directions.forEach { (x, y) ->
                val newX = top.first + x
                val newY = top.second + y
                if (newX >= 0 && newY >= 0 && newX < input.size  * scale && newY < input.first().size * scale) {
                    if (risk[top.first][top.second] + input.scaled(newX, newY) < risk[newX][newY]) {
                        risk[newX][newY] = risk[top.first][top.second] + input.scaled(newX, newY)
                        queue.add(newX to newY)
                    }
                }
            }
        }
        return risk.last().last()
    }

    fun part1(input: Array<Array<Int>>): Int {
       return lee(input, 1)
    }

    fun part2(input: Array<Array<Int>>): Int {
        return lee(input, 5)
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it) == 40)
        check(part2(it) == 315)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it))
        println(part2(it))
    }
}

private fun List<String>.formatted() = Array(this.size) { i ->
    Array(this[i].length) { j ->
        this[i][j].digitToInt()
    }
}

private fun Array<Array<Int>>.scaled(i: Int, j: Int): Int {
    val n = this.size
    val m = this.first().size
    val item = this[i % n][j % m]
    val scaledItemDown = (item + i / n) % 9
    val scaledDone = if (scaledItemDown == 0) 9 else scaledItemDown
    val scaledRight = (scaledDone + j / m) % 9
    return if (scaledRight == 0) 9 else scaledRight
}