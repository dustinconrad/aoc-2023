package day16

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("16_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("16_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return 0
}

fun part2(input: List<String>): Int {
    return 0
}

data class LightPath(val grid: List<String>) {

    fun path(): List<Pair<Int,Int>> {
        var curr = 0 to 0
        var currVec = 0 to 1

        val result = mutableListOf<Pair<Int,Int>>()

        while (true) {
            result.add(curr)

        }

    }

    fun nextVec(curr: Pair<Int, Int>, currVec: Pair<Int,Int>): List<Pair<Int,Int>> {
        val tile = grid[curr.first][curr.second]
        val (cy, cx) = currVec
        return when {
            tile == '/' -> listOf(-cx to -cy)
            tile == '\\' -> listOf(cy to cx)
            cy != 0 && tile == '-' ->
        }
    }

}

fun path(grid: List<String>): List<Pair<Int,Int>> {

}

