package day13

import byEmptyLines
import hamming
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("13_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("13_1.txt").readLines())}")
}

data class Mirrored(val horizontalLines: List<String>) {
    val verticalLines = (0..horizontalLines[0].lastIndex).map { col -> horizontalLines.map { it[col] }.joinToString("") }

    val verticalReflection = reflectionIndex(verticalLines)

    val horizontalReflection = reflectionIndex(horizontalLines)

    fun summarize() = if (verticalReflection != null) {
        verticalReflection.first + 1
    } else {
        (horizontalReflection!!.first + 1) * 100
    }

    val v2 = reflectionIndex(verticalLines, 1)

    val h2 = reflectionIndex(horizontalLines, 1)

    fun s2() = if (v2 != null) {
        v2.first + 1
    } else {
        (h2!!.first + 1) * 100
    }

    companion object {

        fun reflectionIndices(lines: List<String>, md: Int): List<Pair<Int, Int>> {
            return lines.indices.zipWithNext().mapNotNull { (l, r) ->
                if (lines[l].hamming(lines[r]) <= md) {
                    l to r
                } else {
                    null
                }
            }
        }

        fun isReflection(lines: List<String>, idx: Pair<Int, Int>, cd: Int): Boolean {
            var totalDistance = 0
            var (l, r) = idx
            while (l >= 0 && r < lines.size && totalDistance <= cd + 1) {
                val left = lines[l]
                val right = lines[r]
                totalDistance += left.hamming(right)
                l--
                r++
            }
            return cd == totalDistance
        }

        fun reflectionIndex(lines: List<String>, cd: Int = 0): Pair<Int, Int>? {
            return reflectionIndices(lines, cd).firstOrNull { isReflection(lines, it, cd) }
        }

    }
}

fun part1(input: List<String>): Int {
    return input.byEmptyLines().map { Mirrored(it.lines()) }
        .sumOf { it.summarize() }
}

fun part2(input: List<String>): Int {
    return input.byEmptyLines().map { Mirrored(it.lines()) }
        .sumOf { it.s2() }
}
