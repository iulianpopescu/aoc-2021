package day08

import readInput

private const val DAY = "day08"

private val mapping = mapOf(
    "abcefg" to 0,
    "cf" to 1,
    "acdeg" to 2,
    "acdfg" to 3,
    "bcdf" to 4,
    "abdfg" to 5,
    "abdefg" to 6,
    "acf" to 7,
    "abcdefg" to 8,
    "abcdfg" to 9
)

private val charMapping = mutableMapOf(
    'a' to ' ',
    'b' to ' ',
    'c' to ' ',
    'd' to ' ',
    'e' to ' ',
    'f' to ' ',
    'g' to ' '
)

fun main() {
    val segmentsToDigit = mapOf(
        1 to listOf(),
        2 to listOf(1),
        3 to listOf(7),
        4 to listOf(4),
        7 to listOf(8),
        6 to listOf(0, 6, 9),
        5 to listOf(2, 3, 5)
    )

    fun part1(inputFile: List<String>): Int {
        var totalCount = 0
        inputFile.forEach {
            val (_, output) = it.split(" | ")
            totalCount += output.split(' ').count { display ->
                segmentsToDigit[display.length]?.size == 1
            }
        }
        return totalCount
    }

    fun part2(inputFile: List<String>): Int {
        var total = 0
        inputFile.forEach { entry ->
            val (input, output) = entry.split(" | ")
            val items = input.split(" ").sortedBy { it.length }.map { it.toList().sorted().joinToString("") }

            // parsing rules
            // first two item are 1 and 7, which differ by the top line -> 'a' is the difference between them
            charMapping['a'] = items[1].find { !items[0].contains(it) }!!

            // get c and f by checking common chars between 1 and 0,6,9 -> if they all contain an item, then it is f
            val cf = "${items.first()[0]}${items.first()[1]}"
            val item069 = items.filter { it.length == 6 }
            if (item069.count { it.contains(cf[0]) } == 2) {
                charMapping['c'] = cf[0]
                charMapping['f'] = cf[1]
            } else {
                charMapping['c'] = cf[1]
                charMapping['f'] = cf[0]
            }

            // g is the common char between 0, 2, 3, 5, 6, 8, 9 0 -> any number with length >= 5
            charMapping['g'] = items.filter { it.length >= 5 }.commonCharFrom(charMapping.keys.filter {
                it != charMapping['a'] && it != charMapping['c'] && it != charMapping['f']
            })

            val bd = items[2].filter { it != charMapping['c'] && it != charMapping['f'] }
            // this is 4 and contains the same chars as one, plus b and d
            // since we know the big picture of each char we can deduct that e will be remaining char from 8 after we
            // remove the already found chars and the one that have to be set in 'b' and 'd'
            charMapping['e'] = items.last().find {
                it != charMapping['a']
                        && it != charMapping['c']
                        && it != charMapping['f']
                        && it != charMapping['g']
                        && !bd.contains(it)
            }!!

            charMapping['b'] = items.filter { it.length == 6 }.commonCharFrom(
                bd.toList()
            )
            if (charMapping['b'] == bd.first()) {
                charMapping['d'] = bd[1]
            } else {
                charMapping['d'] = bd.first()
            }

            buildString {
                output.split(' ').forEach { currentEntry ->
                    mapping.forEach findMap@{ (k, v) ->
                        if (k == currentEntry.mapped()) {
                            this.append(v)
                            return@findMap
                        }
                    }
                }
                total += this.toString().toInt()
            }
        }
        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$DAY/ExampleTest")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("$DAY/Test")
    println(part1(input))
    println(part2(input))
}

private fun String.mapped() = buildString {
    val inverseMap = mutableMapOf<Char, Char>()
    charMapping.forEach { (key, value) ->
        inverseMap[value] = key
    }
    this@mapped.forEach {
        append(inverseMap[it])
    }
}.sorted()

private fun List<String>.commonCharFrom(list: Iterable<Char>): Char {
    list.forEach { chr ->
        val count = this.count { it.contains(chr) }
        if (count == this.size) {
            return chr
        }
    }
    throw Exception("Impossible")
}

private fun String.sorted() = this.toList().sorted().joinToString("")