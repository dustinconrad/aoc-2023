package day16

import Pos
import Vec
import addVec
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("16_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("16_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val lp = LightPath(input)
    val result = lp.part1()
    return result.size
}

fun part2(input: List<String>): Int {
    val lp = LightPath(input)
    return lp.part2()
}

data class LightPath(val grid: List<String>) {

    private val height = grid.size
    private val width = grid[0].length

    fun path(start: Pair<Pos,Vec> = (0 to 0) to (0 to 1) ): Set<Pair<Pos,Vec>> {
        val result = mutableSetOf<Pair<Pos,Vec>>()

        val q = ArrayDeque<Pair<Pos,Vec>>()
        q.add(start)

        while (q.isNotEmpty()) {
            val curr = q.removeFirst()
            result.add(curr)

            val next = next(curr)

            q.addAll(0, next.filter { !result.contains(it) })
        }

        return result
    }

    fun part1(): Set<Pos> {
        val paths = path()
        val tiles = paths.map { it.first }.toSet()

        return tiles
    }

    fun part2(): Int {
        val top = (0 until width).map { (0 to it) to (1 to 0) }
        val bottom = (0 until width).map { (height - 1 to it) to (-1 to 0) }
        val left = (0 until height).map { (it to 0) to (0 to 1) }
        val right = (0 until height).map { (it to width - 1) to (0 to -1) }

        val all = top + bottom + left + right

        return all.maxOf { path(it).map { it.first }.toSet().size }
    }

    fun next(curr: Pair<Pos,Vec>): List<Pair<Pos,Vec>> {
        val (pos, vec) = curr
        val tile = grid[pos.first][pos.second]
        val (cy, cx) = vec
        val nextVecs = when {
            tile == '/' -> listOf(-cx to -cy)
            tile == '\\' -> listOf(cx to cy)
            cy != 0 && tile == '-' -> listOf(0 to 1, 0 to -1)
            cx != 0 && tile == '|' -> listOf(1 to 0, -1 to 0)
            else -> listOf(vec)
        }

        val result = nextVecs.map { pos.addVec(it) to it }
            .filter { (pos, _) -> pos.first in 0 until height && pos.second in 0 until width }

        return result
    }

    fun debugPart1(): String {
        val tiles = part1()
        return (0 until height).map { y ->
            (0 until width).map { x ->
                if (tiles.contains(y to x)) {
                    "#"
                } else {
                    "."
                }
            }.joinToString("")
        }.joinToString("\n")
    }

}

