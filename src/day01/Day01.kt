package day01

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("1_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("1_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val p = Regex("""\d""")
    return calibrationValues(p, input)
}

fun part2(input: List<String>): Int {
    val p = Regex("""(?=(\d|one|two|three|four|five|six|seven|eight|nine)).""")
    return calibrationValues(p, input)
}

fun calibrationValues(p: Regex, input: List<String>): Int {
    return input.map { it.matchFirstLast(p) }
        .map { outer -> outer.toList().map { it.asDigit() } }
        .map { it.joinToString("") }
        .sumOf { it.toInt() }
}

fun String.matchFirstLast(p: Regex): Pair<String, String> {
    val matches = p.findAll(this).toList()
    val first = matches.first().groupValues.last()
    val last = matches.last().groupValues.last()
    return first to last
}

fun String.asDigit(): Int {
    return when {
        this.length == 1 -> this.toInt()
        this == "one" -> 1
        this == "two" -> 2
        this == "three" -> 3
        this == "four" -> 4
        this == "five" -> 5
        this == "six" -> 6
        this == "seven" -> 7
        this == "eight" -> 8
        this == "nine" -> 9
        else -> throw IllegalArgumentException("Can't parse $this as digit")
    }
}