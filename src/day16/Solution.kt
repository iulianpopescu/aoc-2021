package day16

import readInput

private const val DAY = "day16"

fun main() {

    fun part1(input: Packet): Int {
        return input.versionSum()
    }

    fun part2(input: Packet): Long {
        return input.calculate()
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").formatted().let {
        check(part1(it) == 20)
        check(part2(it) == 1L)
    }

    readInput("$DAY/Test").formatted().let {
        println(part1(it))
        println(part2(it))
    }
}

private fun List<String>.formatted()  = buildString {
    val hexMapping = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'F' to "1111",
        'E' to "1110"
    )
    this@formatted.first().forEach {
        append(hexMapping[it])
    }
}.let { Packet(it) }

class Packet(binary: String) {
    private val version = binary.substring(0..2).toInt(2)
    private val type = binary.substring(3..5).toInt(2)
    private var subPackets = emptyList<Packet>()
    private var value = 0L
    private val length: Int

    init {
        length = when {
            type == 4 -> {
                var pointer = 1
                var binaryValue = ""
                do {
                    pointer += 5
                    binaryValue += binary.substring(pointer + 1..pointer + 4)
                } while (binary[pointer] == '1')
                value = binaryValue.toLong(2)
                pointer + 5
            }
            binary[6] == '1' -> {
                var pointer = 18
                val numberOfPackages = binary.substring(7 until pointer).toInt(2)
                repeat(numberOfPackages) { pointer += extractSubPacket(binary.substring(pointer)) }
                pointer
            }
            else -> {
                var pointer = 22
                val lengthOfPackges = binary.substring(7 until pointer).toInt(2)
                while (pointer - 21 < lengthOfPackges) {
                    pointer += extractSubPacket(binary.substring(pointer))
                }
                pointer
            }
        }
    }

    private fun extractSubPacket(binary: String): Int {
        val subPacket = Packet(binary)
        subPackets = subPackets + subPacket
        return subPacket.length
    }

    fun versionSum(): Int = version + subPackets.sumOf { it.versionSum() }

    fun calculate(): Long {
        val values = subPackets.map { it.calculate() }
        return when (type) {
            0 -> values.sum()
            1 -> values.fold(1L) { product, it -> product * it }
            2 -> values.minOfOrNull { it } ?: 0L
            3 -> values.maxOfOrNull { it } ?: 0L
            4 -> value
            5 -> if (values[0] > values[1]) 1L else 0L
            6 -> if (values[0] < values[1]) 1L else 0L
            7 -> if (values[0] == values[1]) 1L else 0L
            else -> 0L
        }
    }
}