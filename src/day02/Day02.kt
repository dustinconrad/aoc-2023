package day02

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("2_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("2_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return validGamesSum(Round(12, 13, 14), input)
}

fun part2(input: List<String>): Long {
    return input.map { parseGame(it) }
        .sumOf { it.power }
}


fun validGamesSum(bag: Round, input: List<String>): Int {
    return input.map { parseGame(it) }
        .filter { it.validFor(bag) }
        .sumOf { it.id }
}


data class Round(
    val red: Int,
    val green: Int,
    val blue: Int
)

data class CubeGame(
    val id: Int,
    val rounds: List<Round>
) {
    val maxRed = rounds.maxBy { it.red }.red
    val maxGreen = rounds.maxBy { it.green }.green
    val maxBlue = rounds.maxBy { it.blue }.blue

    val power: Long = maxRed.toLong() * maxGreen.toLong() * maxBlue.toLong()

    fun validFor(bag: Round): Boolean =
        maxRed <= bag.red && maxGreen <= bag.green && maxBlue <= bag.blue
}

fun parseGame(line: String): CubeGame {
    val (gameId, rest) = line.split(":")
    val rounds = rest.split(";")
    return CubeGame(gameId.split(" ")[1].toInt(), rounds.map { parseRound(it) })
}

val red = Regex("""\d+ red""")
val green = Regex("""\d+ green""")
val blue = Regex("""\d+ blue""")

fun parseRound(round: String): Round {
    val cubes = round.split(",").map { it.trim() }
    val redCount = cubes.filter { red.matches(it) }
        .map { it.split(" ")[0].toInt() }
        .firstOrNull() ?: 0

    val greenCount = cubes.filter {green.matches(it) }
        .map { it.split(" ")[0].toInt() }
        .firstOrNull() ?: 0

    val blueCount = cubes.filter { blue.matches(it) }
        .map { it.split(" ")[0].toInt() }
        .firstOrNull() ?: 0

    return Round(redCount, greenCount, blueCount)
}