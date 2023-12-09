package day09

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("9_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("9_1.txt").readLines())}")
}

fun parse(line: String) = Regex("""\s+""").split(line).map { it.toLong() }

fun oasis(n: List<Long>): Sequence<List<Long>> = generateSequence(n) { seq ->
    (1 until seq.size).map { seq[it] - seq[it - 1]}
}

fun nextNumber(n: List<Long>): Long {
    val oasis = oasis(n).takeWhile { l -> l.any { it != 0L } }
        .toList().reversed()
    val lastNumbers = oasis.map { it.last() }
    return lastNumbers.reduce { l, r -> l + r}
}

fun previousNumber(n: List<Long>): Long {
    val oasis = oasis(n).takeWhile { l -> l.any { it != 0L } }
        .toList()
    val firstNumbers = oasis.map { it.first() }
    return firstNumbers.foldRight(0L) { l, r -> l - r }
}

fun part1(input: List<String>): Long {
    val lines = input.map { parse(it) }
    val results = lines.map { nextNumber(it) }
    return results.sum()
}

fun part2(input: List<String>): Long {
    val lines = input.map { parse(it) }
    val results = lines.map { previousNumber(it) }
    return results.sum()
}