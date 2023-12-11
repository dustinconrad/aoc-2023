package day11

import readResourceAsBufferedReader
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("11_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("11_1.txt").readLines())}")
}

fun emptyHorizontals(lines: List<String>): Set<Int> {
    return lines.mapIndexedNotNull { y, line ->
        if (line.all { it == '.' }) {
            y
        } else {
            null
        }
    }.toSet()
}

fun emptyVerticals(lines: List<String>): Set<Int> {
    return (0 .. lines[0].lastIndex).mapNotNull { x ->
        val vertLine = lines.map { it[x] }
        if (vertLine.all { it == '.' }) {
            x
        } else {
            null
        }
    }.toSet()
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
        for (j in i + 1..pts.lastIndex) {
            val l = pts[i]
            val r = pts[j]
            result.add((l.first - r.first).absoluteValue + (l.second - r.second).absoluteValue)
        }
    }
    return result
}

fun distances(multiplier: Long, pts: List<Pair<Int,Int>>, emptyHorizontals: Set<Int>, emptyVerticals: Set<Int>): List<Long> {
    val result = mutableListOf<Long>()
    for (i in 0..pts.lastIndex) {
        for (j in i + 1..pts.lastIndex) {
            val l = pts[i]
            val r = pts[j]
            result.add(distance(multiplier, l, r, emptyHorizontals, emptyVerticals))
        }
    }
    return result
}

fun distance(multiplier: Long, ptA: Pair<Int, Int>, ptB: Pair<Int, Int>, emptyHorizontals: Set<Int>, emptyVerticals: Set<Int>): Long {
    val horz = (min(ptA.first, ptB.first) until max(ptA.first, ptB.first)).intersect(emptyHorizontals)
    val totalVertical = (ptA.first - ptB.first).absoluteValue - horz.size + horz.size * multiplier
    val vert = (min(ptA.second, ptB.second) until max(ptA.second, ptB.second)).intersect(emptyVerticals)
    val totalHorizontal = (ptA.second - ptB.second).absoluteValue - vert.size + vert.size * multiplier

    return totalVertical + totalHorizontal
}

fun part1(input: List<String>): Int {
    val expanded = expand(input)
    val galaxies = galaxies(expanded)
    val distances = distances(galaxies)
    return distances.sum()
}

fun part2(input: List<String>, multiplier: Long = 1000000): Long {
    val galaxies = galaxies(input)
    val emptyHorizontals = emptyHorizontals(input)
    val emptyVerticals = emptyVerticals(input)
    val distances = distances(multiplier, galaxies, emptyHorizontals, emptyVerticals)
    return distances.sum()
}
