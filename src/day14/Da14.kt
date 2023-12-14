package day14

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("14_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("14_1.txt").readLines())}")
}

data class Dish(val grid: List<String>) {

    val columns = grid[0].indices.map { idx -> grid.map { it[idx] }.joinToString("") }

    fun tiltNorth(): Int {
        val result = Array<MutableMap<Int, Int>>(columns.size) { mutableMapOf() }
        for (col in columns.withIndex()) {
            var lastRectangle = -1
            var roundCount = 0
            for (y in columns.indices) {
                when (col.value[y]) {
                    '#' -> {
                        result[col.index][lastRectangle] = roundCount
                        lastRectangle = y
                        roundCount = 0
                    }
                    'O' -> roundCount++
                }
            }
            result[col.index][lastRectangle] = roundCount
        }
        return result.map {
            it.entries.map { (anchor, count) ->
                val max = columns[0].length - (anchor + 1)
                (0 until count).sumOf { max - it }
            }.sum()
        }.sum()
    }

}

fun part1(input: List<String>): Int {
    val dish = Dish(input)
    return dish.tiltNorth()
}

fun part2(input: List<String>): Int {
    return 0
}
