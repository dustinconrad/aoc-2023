package day10

import addVec
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("10_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("10_1.txt").readLines())}")
}

data class PipeMaze(val maze: List<String>) {

    val start: Pair<Int, Int> = maze.mapIndexed { idx, line -> idx to line.indexOf('S') }
        .first { it.second != -1 }


    fun loop(): Map<Pair<Int, Int>, Int> {
        val result = mutableMapOf<Pair<Int, Int>, Int>()
        var curr = start
        var distance = 0

        do {
            result[curr] = distance

            val next = moves(curr).subtract(result.keys)
            if (next.isEmpty()) {
                break
            }

            curr = next.first()
            distance++
        } while(curr != start)

        return result
    }

    fun exits(c: Pair<Int, Int>): Set<Pair<Int, Int>> {
        return when (maze[c.first][c.second]) {
            '|' -> setOf(-1 to 0, 1 to 0)
            '-' -> setOf(0 to -1, 0 to 1)
            'L' -> setOf(-1 to 0, 0 to 1)
            'J' -> setOf(-1 to 0, 0 to -1)
            '7' -> setOf(1 to 0, 0 to -1)
            'F' -> setOf(1 to 0, 0 to 1)
            'S' -> setOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            else -> emptySet()
        }
    }

    fun moves(start: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val exits = exits(start)
        val moves = exits.mapNotNull { exit ->
            val exitInverted = 0 - exit.first to 0 - exit.second
            val next = start.addVec(exit)
            if (!isInMaze(next)) {
                return@mapNotNull null
            }
            val nextExits = exits(next)
            if (nextExits.contains(exitInverted)) {
                next
            } else {
                null
            }
        }
        return moves.toSet()
    }

    fun isInMaze(c: Pair<Int, Int>): Boolean =
        (0 until maze.size).contains(c.first) && (0 until maze[0].length).contains(c.second)

}

fun part1(input: List<String>): Int {
    val maze = PipeMaze(input)
    val loop = maze.loop()
    return loop.size / 2
}

fun part2(input: List<String>): Int {
    return 0
}

