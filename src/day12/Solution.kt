package day12

import readInput

private const val DAY = "day12"

fun main() {

    fun countPaths(
        graph: Map<String, Set<String>>,
        currentPath: List<String>,
        conditions: List<(neighbour: String, path: List<String>) -> Boolean>
    ): Int {
        val lastItem = currentPath.last()
        return if (lastItem == "end") {
            1
        } else {
            var totalWaysCounts = 0
            graph[lastItem]!!.forEach { neighbour ->
                if (neighbour != "start" && conditions.any { it(neighbour, currentPath) }) {
                    totalWaysCounts += countPaths(graph, currentPath + neighbour, conditions)
                }
            }
            totalWaysCounts
        }
    }

    fun part1(graph: Map<String, Set<String>>, currentPath: List<String>): Int {
        return countPaths(graph, currentPath, listOf(
            { neighbour: String, path: List<String> -> !path.contains(neighbour) },
            { neighbour: String, _: List<String> -> neighbour.isUppercase() }
        ))
    }

    fun part2(graph: Map<String, Set<String>>, currentPath: List<String>): Int {
        return countPaths(graph, currentPath, listOf(
            { neighbour: String, path: List<String> -> !path.contains(neighbour) },
            { neighbour: String, _: List<String> -> neighbour.isUppercase() },
            { _: String, path: List<String> -> !path.hasLowercaseDoubles() }
        ))
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let { graph ->
        check(part1(graph, listOf("start")) == 10)
        check(part2(graph, listOf("start")) == 36)
    }

    readInput("$DAY/Test").formatted().let { graph ->
        println(part1(graph, listOf("start")))
        println(part2(graph, listOf("start")))
    }
}

private fun List<String>.formatted(): Map<String, Set<String>> {
    val map = HashMap<String, MutableSet<String>>()
    this.forEach {
        val (x, y) = it.split('-')
        if (map[x] == null) map[x] = mutableSetOf()
        if (map[y] == null) map[y] = mutableSetOf()
        map[x]!!.add(y)
        map[y]!!.add(x)
    }
    return map
}

private fun String.isUppercase() = this.matches(Regex("^[A-Z]+$"))

private fun List<String>.hasLowercaseDoubles(): Boolean {
    for (i in 0 until this.size - 1) {
        for (j in i + 1 until this.size) {
            if (!this[i].isUppercase() && this[i] == this[j]) return true
        }
    }
    return false
}