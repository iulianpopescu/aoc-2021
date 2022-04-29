package day22

import readInput

private const val DAY = "day22"

fun main() {

    fun part1(input: List<String>): Long {
//        val ons = mutableListOf<Operation>()
//        input.filter { it.inLowerRange() }
//            .sortedBy { it }
//            .forEach {
//                fun switch(state: Boolean) {
//                    for (i in it.x.first..it.x.last) {
//                        for (j in it.y.first..it.y.last) {
//                            for (k in it.z.first..it.z.last) {
//                                ons[i+50][j+50][k+50] = state
//                            }
//                        }
//                    }
//                }
//                switch(it is Operation.On)
//            }
//        return ons.fold(0) { total, interval -> total + interval.cubesOn() }
        return Day22(input).solvePart1()
    }

    fun part2(input: List<String>): Long {
        return Day22(input).solvePart2()

//        val ons = Array(100_001) {
//            Array(100_001) {
//                Array(100_001) { false }
//            }
//        }
//        println("dsad")
//
//        val minNeg = input.minOf { minOf(it.x.first, it.y.first, it.z.first) }
//        fun switch(it: Operation, state: Boolean) {
//            for (i in it.x.first..it.x.last) {
//                for (j in it.y.first..it.y.last) {
//                    for (k in it.z.first..it.z.last) {
//                        ons[i+minNeg][j+minNeg][k+minNeg] = state
//                    }
//                }
//            }
//        }
//        input.forEachIndexed { index, operation ->
//            println(index)
//            switch(operation, operation is Operation.On)
//        }
//        return ons.fold(0) { totalSlice, slice ->
//            totalSlice + slice.fold(0) { totalLine, line ->
//                totalLine + line.count { it }
//            }
//        }
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").let {
        check(part1(it) == 590784L)
//        check(part2(it) == 2758514936282235)
    }

    readInput("$DAY/Test").let {
        println(part1(it))
        println(part2(it))
    }
}

sealed class Operation(val x: IntRange, val y: IntRange, val z: IntRange): Comparable<Operation> {
    class On(x: IntRange, y: IntRange, z: IntRange): Operation(x, y, z)
    class Off(x: IntRange, y: IntRange, z: IntRange): Operation(x, y, z)

    fun inLowerRange(): Boolean {
        val lowerRange = -50..50
        return x.first in lowerRange && x.last in lowerRange
                && y.first in lowerRange && y.last in lowerRange
                && z.first in lowerRange && z.last in lowerRange
    }

    fun cubesOn(): Int {
        return (x.last - x.first) * (y.last - y.first) * (z.last - z.first)
    }

//    fun intersectsWith(other: Operation): Intersection {
//        if (x.last < other.x.first || y.last < other.y.first || z.last < other.z.first) {
//            return Intersection.DISJOINT
//        } else {
//            // cubes intersect or are adjacent
//            if (other.x.first in x && other.x.last in x
//                && other.y.first in y && other.y.last in y
//                && other.z.first in z && other.z.last in z
//            ) {
//                return Intersection.INSIDE
//            } else
//        }
//    }

    override fun toString(): String {
        return "x: $x -> y: $y -> z: $z\n"
    }

    override operator fun compareTo(other: Operation): Int {
        return if (this.x.first < other.x.first) {
            -1
        } else if (this.x.first > other.x.first) {
            +1
        } else {
            if (this.y.first < other.y.first) {
                -1
            } else if (this.y.first > other.y.first) {
                +1
            } else {
                this.z.first.compareTo(other.z.first)
            }
        }
    }

    enum class Intersection {
        INSIDE,
        ADJACENT,
        INTERSECTED,
        DISJOINT
    }
}


class Day22(input: List<String>) {

    private val cubes: List<Cuboid> = input.map { Cuboid.of(it) }
    private val part1Cube = Cuboid(true, -50..50, -50..50, -50..50)

    fun solvePart1(): Long =
        solve(cubes.filter { it.intersects(part1Cube) })

    fun solvePart2(): Long =
        solve()

    private fun solve(cubesToUse: List<Cuboid> = cubes): Long {
        val volumes = mutableListOf<Cuboid>()

        cubesToUse.forEach { cube ->
            volumes.addAll(volumes.mapNotNull { it.intersect(cube) })
            if (cube.on) volumes.add(cube)
        }
        return volumes.sumOf { it.volume() }
    }

    private class Cuboid(val on: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {
        fun volume(): Long =
            (x.size().toLong() * y.size().toLong() * z.size().toLong()) * if (on) 1 else -1

        fun intersect(other: Cuboid): Cuboid? =
            if (!intersects(other)) null
            else Cuboid(!on, x intersect other.x, y intersect other.y, z intersect other.z)

        fun intersects(other: Cuboid): Boolean =
            x.intersects(other.x) && y.intersects(other.y) && z.intersects(other.z)

        companion object {
            private val pattern =
                """^(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)$""".toRegex()

            fun of(input: String): Cuboid {
                val (s, x1, x2, y1, y2, z1, z2) = pattern.matchEntire(input)?.destructured
                    ?: error("Cannot parse input: $input")
                return Cuboid(
                    s == "on",
                    x1.toInt()..x2.toInt(),
                    y1.toInt()..y2.toInt(),
                    z1.toInt()..z2.toInt(),
                )
            }
        }
    }
}

infix fun IntRange.intersects(other: IntRange): Boolean =
    first <= other.last && last >= other.first

infix fun IntRange.intersect(other: IntRange): IntRange =
    maxOf(first, other.first)..minOf(last, other.last)

fun IntRange.size(): Int =
    last - first + 1

private fun List<String>.formatted() = this.map {
    val (x, y, z) = it.split(',')
    val onOp = it.contains("on")
    val (yStart, yEnd)= y.drop(2).split("..").map { it.toInt() }
    val (zStart, zEnd)= z.drop(2).split("..").map { it.toInt() }
    if (onOp) {
        val (xStart, xEnd)= x.drop(5).split("..").map { it.toInt() }
        Operation.On(xStart..xEnd, yStart..yEnd, zStart..zEnd)
    } else {
        val (xStart, xEnd)= x.drop(6).split("..").map { it.toInt() }
        Operation.Off(xStart..xEnd, yStart..yEnd, zStart..zEnd)
    }
}


fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
    return prerequisites.groupBy { it[1] }
        .toMutableMap()
        .apply {
            for (i in 0..numCourses) {
                if (!this.contains(i)) this[i] = listOf()
            }
        }
        .map { (id, list) ->
            Course(id, list.map { it.first() })
        }
        .sortedBy { it }
        .map { it.id }
        .toIntArray()
}

data class Course(
    val id: Int,
    val preCourses: List<Int>
): Comparable<Course> {


    override operator fun compareTo(other: Course): Int {
        if (other.preCourses.contains(id)) return -1
        if (preCourses.contains(other.id)) return 1

        return 0
    }
}