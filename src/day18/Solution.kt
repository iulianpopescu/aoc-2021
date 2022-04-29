package day18

import readInput
import java.util.*

private const val DAY = "day18"

fun main() {

    fun part1(input: List<String>): Int {
        return Day18(input).solvePart1()
    }

    fun part2(input: List<String>): Int {
        return Day18(input).solvePart2()
    }

    // test if implementation meets criteria from the description, like:
    readInput("$DAY/ExampleTest").let {
        check(part1(it) == 4140)
        check(part2(it) == 3993)
    }

    readInput("$DAY/Test").let {
        println(part1(it))
        println(part2(it))
    }
}

sealed class SnailfishNumber {
    class Regular(val n: Int): SnailfishNumber()
    class Pair(var left: SnailfishNumber, var right: SnailfishNumber): SnailfishNumber()

    operator fun plus(other: SnailfishNumber): SnailfishNumber {
        val result = Pair(this, other)
        return explode(result)
    }

    private fun split(number: SnailfishNumber): SnailfishNumber {
        val asString = number.toString()
        for (i in 0 until asString.lastIndex) {
            val possibleNumber = "${asString[i]}${asString[i+1]}".toIntOrNull()
            if (possibleNumber != null) {
                val left = possibleNumber / 2
                val right = (possibleNumber + 1)/2
                return parse(buildString {
                    append(asString.substring(0 until i))
                    append("[$left,$right]")
                    append(asString.substring(i+2))
                })
            }
        }
        return number
    }

    private fun explode(number: SnailfishNumber): SnailfishNumber {
        val asString = number.toString()
        var count = 0
        for (i in asString.indices) {
            if (asString[i] == '[') count++
            if (asString[i] == ']') count--

            if (count == 5) {
                // we are at [
                // search for comma
                val commaPosition = asString.substring(i).indexOf(',') + i
                val left = asString.substring(i+1 until commaPosition).toIntOrNull()
                val closingBracket = asString.substring(i).indexOf(']')
                val right = asString.substring(commaPosition+1 until closingBracket).toIntOrNull()
                if (left != null && right != null) {
                    buildString {
                        var leftIndexToChange = i
                        while (!asString[leftIndexToChange].isDigit() && leftIndexToChange >= 0) leftIndexToChange--
                        if (leftIndexToChange > 0) {
                            var doubleDigit = false
                            if (leftIndexToChange - 1 > 0 && asString[leftIndexToChange - 1].isDigit()) {
                                doubleDigit = true
                                leftIndexToChange--
                            }
                            append(asString.substring(0 until leftIndexToChange))
                            var toChange = if (doubleDigit) {
                                asString.substring(leftIndexToChange..leftIndexToChange+1).toInt()
                            } else {
                                asString[leftIndexToChange].digitToInt()
                            }
                            toChange += left
                            append(toChange)
                            val plusIndex = if (doubleDigit) 2 else 1
                            append(asString.substring(leftIndexToChange+plusIndex..i))
                        } else {
                            append(asString.substring(0 until i))
                        }

                        println(this)
                    }
                }
            }
        }

        return number
    }

    override fun toString(): String {
        if (this is Regular) {
            return n.toString()
        } else if (this is Pair){
            return "[$left,$right]"
        }
        return ""
    }

    companion object {
        fun parse(string: String): SnailfishNumber {
            if (string[1] == '[') {
                val commaPosition = findCommaPositionForNumber(string)
                return if (string[commaPosition+1] == '[') {
                    Pair(
                        parse(string.substring(1 until commaPosition)),
                        parse(string.substring(commaPosition+1 until string.lastIndex))
                    )
                } else {
                    val closingBracket = string.substring(commaPosition).indexOfFirst { it == ']' } + commaPosition
                    val right = string.substring(commaPosition+1 until closingBracket).toInt()
                    Pair(
                        parse(string.substring(1 until commaPosition)),
                        Regular(right)
                    )
                }
            } else {
                val commaPosition = string.indexOfFirst { it == ',' }
                val left = string.substring(1 until commaPosition).toInt()
                return if (string[commaPosition+1] == '[') {
                    Pair(
                        Regular(left),
                        parse(string.substring(commaPosition+1 until string.lastIndex))
                    )
                } else {
                    val closingBracket = string.substring(commaPosition).indexOfFirst { it == ']' } + commaPosition
                    val right = string.substring(commaPosition+1 until closingBracket).toInt()
                    Pair(
                        Regular(left),
                        Regular(right)
                    )
                }
            }
        }

        private fun findCommaPositionForNumber(string: String): Int {
            var counter = 0
            string.drop(1).forEachIndexed { index, it ->
                if (it == '[') counter++
                if (it == ']') counter--
                if (it == ',' && counter == 0) {
                    return index + 1
                }
            }
            throw Exception("Not possible")
        }
    }
}

private fun List<String>.formatted() = this.map { SnailfishNumber.parse(it) }

/**
 * Uses a linked list of Tokens as a representation of snailfish numbers. Operations such as
 * add, explode and split modifies the linked list given as (first) argument.
 */
class Day18(input: List<String>) {

    sealed interface Token {
        object Open : Token
        object Close : Token
        object Separator : Token // Separators are not really needed, but they make testing and debugging easier
        data class Value(val value: Int) : Token

        companion object {
            fun tokenFor(char: Char) = when (char) {
                '[' -> Open
                ']' -> Close
                ',' -> Separator
                else -> Value(char - '0') // Allows for chars with value higher than 10 (used in a few tests)
            }

            fun stringify(token: Token): String {
                return when (token) {
                    Close -> "]"
                    Open -> "["
                    Separator -> ","
                    is Value -> "${'0' + token.value}"
                }
            }
        }
    }

    private val homework = input.map { parse(it) }

    fun parse(str: String) = LinkedList(str.map { Token.tokenFor(it) })

    // Used by tests to compare results against what's given in the problem description
    fun stringify(input: LinkedList<Token>) = input.joinToString("") { Token.stringify(it) }

    operator fun LinkedList<Token>.plus(other: LinkedList<Token>) = apply {
        add(0, Token.Open)
        add(Token.Separator)
        addAll(other)
        add(Token.Close)

        // Adding two numbers always triggers a reduction
        while (explode(this) || split(this)) Unit // Keep exploding and splitting until fully reduced
    }

    // Adds 'toAdd' to the first regular number in the given range
    private fun addToFirstRegularNumber(number: LinkedList<Token>, range: IntProgression, toAdd: Int) {
        for (j in range) {
            val curr = number[j]
            if (curr is Token.Value) {
                number[j] = Token.Value(curr.value + toAdd)
                return
            }
        }
    }

    // returns true if an explosion was triggered
    fun explode(number: LinkedList<Token>): Boolean {
        var nesting = 0
        for (i in number.indices) {
            when (number[i]) {
                Token.Open -> nesting++
                Token.Close -> nesting--
                else -> Unit
            }
            if (nesting == 5) {
                // Need to explode. At this nesting level there are only two real numbers and no pairs
                // Left value is added to first regular number to the left
                val left = (number[i + 1] as Token.Value).value
                addToFirstRegularNumber(number, i - 1 downTo 0, left)

                // Right value is added to first regular number to the right
                val right = (number[i + 3] as Token.Value).value
                addToFirstRegularNumber(number, i + 5 until number.size, right)

                repeat(5) { number.removeAt(i) } // Remove the current pair
                number.add(i, Token.Value(0))    // and insert 0 in its place
                return true
            }
        }
        return false
    }

    // returns true if the number was split
    fun split(number: LinkedList<Token>): Boolean {
        for (i in number.indices) {
            val token = number[i]
            if (token is Token.Value && token.value >= 10) {
                val l = token.value / 2
                val r = token.value - l
                number.removeAt(i)
                number.addAll(i, listOf(Token.Open, Token.Value(l), Token.Separator, Token.Value(r), Token.Close))
                return true
            }
        }
        return false
    }

    private fun LinkedList<Token>.calculateMagnitude(): Int {
        val stack = mutableListOf<MutableList<Int>>()
        for (i in indices) {
            when (val ch = get(i)) {
                Token.Open -> stack.add(mutableListOf())
                Token.Close -> {
                    val (left, right) = stack.removeLast()
                    val magnitude = 3 * left + 2 * right
                    if (stack.isEmpty()) {
                        return magnitude
                    }
                    stack.last().add(magnitude)
                }
                is Token.Value -> stack.last().add(ch.value)
                Token.Separator -> Unit
            }
        }
        return -1
    }

    fun doHomework() = homework.reduce { acc, next -> acc + next }

    fun solvePart1(): Int {
        return doHomework().calculateMagnitude()
    }

    fun solvePart2(): Int {
        return sequence {
            for (i in homework.indices) {
                for (j in homework.indices) {
                    if (i != j) {
                        // create a copy to avoid modifying the homework
                        val copy = LinkedList(homework[i].toMutableList())
                        yield((copy + homework[j]).calculateMagnitude())
                    }
                }
            }
        }.maxOf { it }
    }
}