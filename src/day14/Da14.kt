package day14

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("14_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("14_1.txt").readLines())}")
}

data class Dish(val grid: List<String>) {

    val columns = grid[0].indices.map { idx -> grid.map { it[idx] }.joinToString("") }

    fun tiltPart1(): Map<Pair<Int, Int>, List<Pair<Int, Int>>> {
        val result = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
        for (col in columns.withIndex()) {
            var lastRectangle = -1
            for (y in columns.indices) {
                when (col.value[y]) {
                    '#' -> lastRectangle = y
                    'O' -> {
                        result.compute(lastRectangle to col.index) { k, v ->
                            if (v == null) {
                                mutableListOf(lastRectangle + 1 to col.index)
                            } else {
                                v.add(lastRectangle + v.size to col.index)
                                v
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    fun part1(): Int {
        val part1Result = tiltPart1()
        return part1Result.map { (anchor, stones) ->
            val count = stones.count()
            val max = grid.size - (anchor.first + 1)
            (0 until count).sumOf { max - it }
        }.sum()
    }
}

fun part1(input: List<String>): Int {
    val dish = Dish(input)
    return dish.part1()
}

fun part2(input: List<String>): Int {
    return 0
}
