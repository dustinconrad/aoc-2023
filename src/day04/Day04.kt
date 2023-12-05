package day04

import readResourceAsBufferedReader
import kotlin.math.max
import kotlin.math.min


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("4_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("4_1.txt").readLines())}")
}

val whitespace = Regex("\\s+")

fun part1(input: List<String>): Int {
    return input.map { Scratchcard.parse(it) }
        .sumOf { it.points() }
}

fun part2(input: List<String>): Int {
    val cards = input.map { Scratchcard.parse(it) }
    val cardCounts = cards.map { it.id to 1 }.toMap().toMutableMap()
    for (card in cards) {
        val currentCount = cardCounts[card.id] ?: 0
        for (nc in card.newCards()) {
            cardCounts.computeIfPresent(nc) { _, ov -> ov + currentCount}
        }
    }
    return cardCounts.values.sum()
}

data class Scratchcard(
    val id: Int,
    val winners: Set<Int>,
    val numbers: Set<Int>
) {

    fun points(): Int {
        return when (matches) {
            0 -> 0
            1 -> 1
            else -> 2 shl (matches - 2)
        }
    }

    fun newCards(): Set<Int> {
        return if (matches > 0) {
            (1..matches).map { id + it }.toSet()
        } else {
            emptySet()
        }
    }

    val matches = winners.intersect(numbers).size

    companion object {
        fun parse(line: String): Scratchcard {
            val (card, n) = line.split(":").map { it.trim() }
            val (winners, numbers) = n.split("|").map { it.trim() }

            val id = whitespace.split(card).last().trim().toInt()
            return Scratchcard(
                id,
                whitespace.split(winners).map { it.toInt() }.toSet(),
                whitespace.split(numbers).map { it.toInt() }.toSet(),
            )
        }
    }
}
