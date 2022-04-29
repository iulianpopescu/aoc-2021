package day17

import readInput

private const val DAY = "day17"

fun main() {

    fun part1(zone: TargetZone): Int {
        var maxHeight = 0
        for (i in 0..100) {
            val xDistance = i * (i+1) / 2
            if (xDistance in zone.minX..zone.maxX) {
                for (j in 0..100) {
                    var fallStep = j+1
                    val yDistance = (j* (j+1)) / 2
                    var fallDistance = yDistance
                    val targetDistanceMin = yDistance - zone.minY
                    val targetDistanceMax = yDistance - zone.maxY
                    while (fallDistance <= targetDistanceMin && fallDistance <= targetDistanceMax) {
                        fallDistance += fallStep
                        fallStep++
                        if (fallDistance in targetDistanceMin..targetDistanceMax) {
                            maxHeight = maxOf(maxHeight, yDistance)
                        }
                    }
                }
            }
        }
        return maxHeight
    }

    fun part2(zone: TargetZone): Int {
        var count = 0
        for (i in 0..314) {
            for (j in -314..314) {
                var xVelocity = i
                var xPos = 0
                var yVelocity = j
                var yPos = 0
                while (true) {
                    xPos += xVelocity
                    xVelocity = maxOf(xVelocity - 1, 0)
                    yPos += yVelocity
                    yVelocity--

                    if (xPos in zone.minX..zone.maxX && yPos in zone.maxY..zone.minY) {
                        count++
                        break
                    }
                    if (xPos > zone.maxX || yPos < zone.maxY) break
                }
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it) == 45)
        check(part2(it) == 112)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it))
        println(part2(it))
    }
}

data class TargetZone(
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int
)

private fun List<String>.formatted() = this.first().substring(13).split(", ").let {
    val xArray = it[0].drop(2)
    val yArray = it[1].drop(2)
    val xInts = xArray.split("..").map { it.toInt() }
    val yInts = yArray.split("..").map { it.toInt() }
    TargetZone(xInts[0], xInts[1], yInts[1], yInts[0]) }