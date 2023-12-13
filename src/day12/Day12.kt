package day12

import readResourceAsBufferedReader
import kotlin.math.min


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("12_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("12_1.txt").readLines())}")
}

fun parse(line: String): Pair<String, List<Int>> {
    val (c, l) = line.split(" ")
    return Pair(c, l.split(",").map { it.toInt() })
}

fun parse2(line: String): Pair<String, List<Int>> {
    val (c, l) = line.split(" ")
    val condition = (0 until 5).joinToString("?") { c }
    val numbers = (0 until 5).joinToString(",") { l }.split(",").map { it.toInt() }
    return Pair(condition, numbers)
}

fun springCombinations(condition: String, groupSizes: List<Int>): Long {
    val cache = Array(condition.length) { LongArray(groupSizes.size) { -1L } }
    return springCombinations(cache, condition, groupSizes, 0, 0)
}

fun springCombinations(cache: Array<LongArray>, condition: String, groupSizes: List<Int>, conditionIdx: Int, groupIndex: Int): Long {
    // we have no where left to place springs
    if (conditionIdx > condition.lastIndex) {
        // if we have springs left, this placement is invalid
        return if (groupIndex <= groupSizes.lastIndex) {
            0
        } else {
            1
        }
    }
    // placing MUST cover the first broken spring
    val firstBrokenSpring = condition.indexOf('#', conditionIdx)

    // we placed all groups
    if (groupIndex > groupSizes.lastIndex) {
        // make sure we have covered all springs
        return if (firstBrokenSpring == -1) {
            1
        } else {
            0
        }
    }

    if (cache[conditionIdx][groupIndex] != -1L) {
        return cache[conditionIdx][groupIndex]
    }

    val currGroup = groupSizes[groupIndex]

    // invariant - first index is always valid
    fun placeIndexes(): List<Int> {
        // if the group is larger than the remaining space we can't place it
        if (conditionIdx + currGroup > condition.length) {
            return emptyList()
        }

        val result = mutableListOf<Int>()
        val window = (conditionIdx until conditionIdx + currGroup).map { condition[it] }.groupBy { it }
            .mapValues { it.value.size }
            .toMutableMap()

        val end = if (firstBrokenSpring != -1) {
            min(firstBrokenSpring + currGroup, condition.length)
        } else {
            condition.length
        }

        for (r in conditionIdx + currGroup .. end) {
            val l = r - currGroup

            if(window.getOrDefault('.', 0) == 0) {
                // check left buffer
                val lbuffer = if (l > conditionIdx) {
                    condition[l - 1] == '.' || condition[l - 1] == '?'
                } else {
                    true
                }
                // check right buffer
                val rbuffer = if (r < condition.length) {
                    condition[r] == '.' || condition[r] == '?'
                } else {
                    true
                }
                if (lbuffer && rbuffer) {
                    result.add(r + 1)
                }
            }

            window.computeIfPresent(condition[l]) { _, v -> v - 1}
            if (r < condition.length) {
                window.compute(condition[r]) { k, v ->
                    if (v != null) {
                        v + 1
                    } else {
                        1
                    }
                }

            }
        }
        return result
    }

    val results = placeIndexes().map { rIndex ->
        springCombinations(cache, condition, groupSizes, rIndex, groupIndex + 1)
    }

    val sum = results.sum()
    cache[conditionIdx][groupIndex] = sum
    return sum
}

fun part1(input: List<String>): Long {
    val rows = input.map { parse(it) }
    val sums = rows.map { springCombinations(it.first, it.second) }
    return sums.sum()
}

fun part2(input: List<String>): Long {
    val rows = input.map { parse2(it) }
    val sums = rows.map { springCombinations(it.first, it.second) }
    return sums.sum()
}