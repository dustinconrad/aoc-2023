package day11

import readResourceAsBufferedReader
import kotlin.math.absoluteValue


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("11_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("11_1.txt").readLines())}")
}

fun expand(lines: List<String>): List<String> {
    val expandVertical = lines.flatMap { line ->
        if(line.all { it == '.' })  {
            listOf(line, line)
        } else {
            listOf(line)
        }
    }

    val expandHorizontal = (0 .. expandVertical[0].lastIndex).mapNotNull { x ->
        val vertLine = expandVertical.map { it[x] }
        if (vertLine.all { it == '.' }) {
            x
        } else {
            null
        }
    }.toSet()

    val expanded = expandVertical.map { line ->
        val newLine = line.flatMapIndexed { idx, c ->
            if (expandHorizontal.contains(idx)) {
                listOf(c, c)
            } else {
                listOf(c)
            }
        }

        newLine.joinToString("")
    }

    return expanded
}

fun galaxies(lines: List<String>): List<Pair<Int,Int>> {
    return lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c == '#') {
                y to x
            } else {
                null
            }
        }
    }
}

fun distances(pts: List<Pair<Int,Int>>): List<Int> {
    val result = mutableListOf<Int>()
    for (i in 0..pts.lastIndex) {
        for (j in i..pts.lastIndex) {
            val l = pts[i]
            val r = pts[j]
            result.add((l.first - r.first).absoluteValue + (l.second - r.second).absoluteValue)
        }
    }
    return result
}

fun part1(input: List<String>): Int {
    val expanded = expand(input)
    val galaxies = galaxies(expanded)
    val distances = distances(galaxies)
    return distances.sum()
}

fun part2(input: List<String>): Int {
    return 0
}
