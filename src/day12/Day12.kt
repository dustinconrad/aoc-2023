package day12

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("12_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("12_1.txt").readLines())}")
}

val terminalChars = setOf('.','?')

data class SpringRow(val condition: String, val groupSizes: List<Int>) {

    fun placeIndexes(size: Int): List<Int> {
        if (size + 2 > condition.length) {
            return emptyList()
        }
        val result = mutableListOf<Int>()
        val window = (0 until size + 1).map { condition[it] }.groupBy { it }
            .mapValues { it.value.size }
            .toMutableMap()

        for (r in size + 1 until condition.length) {
            val left = r - size - 1
            val rChar = condition[r]
            val lChar = condition[left]
            var dotCount = 0
            if (lChar == '.') {
                dotCount++
            }
            if (terminalChars.contains(lChar) && terminalChars.contains(rChar) && window['.'] == dotCount
                && window.getOrDefault('g', 0) == 0) {
                result.add(left)
            }
            window.computeIfPresent(lChar) { _, v -> v - 1}
            window.compute(rChar) { k, v ->
                if (v != null) {
                    v + 1
                } else {
                    1
                }
            }
        }
        return result
    }

    fun places(size: Int) : List<String> {
        val positions = placeIndexes(size)
        val result = positions.map {
            val left = this.condition.substring(0, it)
            val middle = ".${"g".repeat(size)}."
            val right = this.condition.substring(it + size + 2, this.condition.length)
            "$left$middle$right"
        }
        return result
    }

    fun combos() : Set<String> =
        if (this.groupSizes.isEmpty()) {
           setOf(this.condition)
        } else {
            val curr = this.groupSizes[0]
            val rest = this.groupSizes.drop(1)
            val result = places(curr).flatMap {
                SpringRow(it, rest).combos()
            }.toSet()
            result
        }

    companion object {

        fun parse(line: String): SpringRow {
            val (c, l) = line.split(" ")
            return SpringRow(".$c.", l.split(",").map { it.toInt() }.sortedDescending())
        }

    }
}

fun part1(input: List<String>): Int {
    return input.map { SpringRow.parse(it) }
        .sumOf { it.combos().size }
}

fun part2(input: List<String>): Int {
    return 0
}