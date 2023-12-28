package day21

import addVec
import day03.Coord2
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("21_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("21_1.txt").readLines())}")
}

fun part1(input: List<String>, maxSteps: Int = 64): Int {
    val minSteps = mutableMapOf<Coord2, Int>()

    val start = input.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if ('S'.equals(c, true)) {
                y to x
            } else {
                null
            }
        }
    }.first()

    val q = ArrayDeque<Coord2>()
    q.add(start)
    minSteps[start] = 0

    while(!q.isEmpty()) {
        val currCoord = q.removeFirst()
        val currSteps = minSteps[currCoord]!!
        if (currSteps >= maxSteps) {
            break
        }

        val next = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
                .map { currCoord.addVec(it) }
                .filter { (0 .. input.lastIndex).contains(it.first) && (0 .. input[0].lastIndex).contains(it.second) }
                .filter { !minSteps.contains(it) }
                .filter { input[it.first][it.second] != '#' }

        next.forEach {
            minSteps[it] = currSteps + 1
            q.add(it)
        }
    }

    return minSteps.values.count { it % 2 == 0 }
}

fun part2(input: List<String>): Int {
    return 0
}
