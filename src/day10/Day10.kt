package day10

import addVec
import readResourceAsBufferedReader
import subtract


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("10_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("10_1.txt").readLines())}")
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


    fun cleaned(): List<String> {
        // tiles are either in the loop or not
        val loop = loop()

        val startMoves = moves(start)
            .map { it.subtract(start) }

        val together = startMoves.reduce { l, r -> l.addVec(r) }
        val startChar  = when {
            together == 1 to 1 -> 'F'
            together == 1 to -1 -> '7'
            together == -1 to 1 -> 'L'
            together == -1 to -1 -> 'J'
            startMoves.all { it.first == 0 } -> '-'
            startMoves.all { it.second == 0 } -> '|'
            else -> throw IllegalArgumentException("$together unmapped")
        }

        return maze.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                when {
                    y to x == start -> startChar
                    loop.containsKey(y to x) -> c
                    else -> '.'
                }
            }.joinToString("")
        }
    }
}

fun enhance(maze: PipeMaze): List<String> {
    val cleaned = maze.cleaned()
    return cleaned.flatMap { enhanceLine(it) }
}

fun outsideLoop(enhanced: List<String>): Set<Pair<Int, Int>> {
    val outside = enhanced[0].mapIndexedNotNull { x, c ->
        if (c == '.') {
            0 to x
        } else {
            null
        }
    }.toMutableSet()

    enhanced.last().mapIndexedNotNull { x, c ->
        if (c == '.') {
            enhanced.size - 1 to x
        } else {
            null
        }
    }.forEach(outside::add)

    enhanced.mapIndexedNotNull { idx, line ->
        if (line[0] == '.') {
            idx to 0
        } else  {
            null
        }
    }.forEach(outside::add)

    enhanced.mapIndexedNotNull { idx, line ->
        if (line[line.lastIndex] == '.') {
            idx to line.lastIndex
        } else  {
            null
        }
    }.forEach(outside::add)

    var q = ArrayDeque<Pair<Int,Int>>()

    q.addAll(outside)

    while(q.isNotEmpty()) {
        val curr = q.removeFirst()

        val next = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
            .map { curr.addVec(it) }
            .filter { (0 until enhanced.size).contains(it.first) && (0 until enhanced[0].length).contains(it.second) }
            .filter { !outside.contains(it) }
            .filter { enhanced[it.first][it.second] == '.' }

        outside.addAll(next)
        q.addAll(next)
    }

    return outside
}

fun unenhance(c: Pair<Int, Int>): Pair<Int, Int> {
    return c.first / 3 to c.second / 3
}

fun enhanceLine(line: String): List<String> {
    val chars = line.map { enhanceChar(it) }
    val combined = chars.reduce { acc, next -> acc.zip(next) { l, r -> l + r } }
    return combined
}

fun enhanceChar(c: Char): List<String> {
    return when (c) {
        '|' -> listOf(".#.", ".#.", ".#.")
        '-' -> listOf("...", "###", "...")
        'L' -> listOf(".#.", ".##", "...")
        'J' -> listOf(".#.", "##.", "...")
        '7' -> listOf("...", "##.", ".#.")
        'F' -> listOf("...", ".##", ".#.")
        '.' -> listOf("...", "...", "...")
        else -> throw IllegalArgumentException("unknown $c")
    }
}

fun printEnhanced(enhancedMaze: List<String>) {
    println(enhancedMaze.joinToString("\n"))
}

fun part1(input: List<String>): Int {
    val maze = PipeMaze(input)
    val loop = maze.loop()
    return loop.size / 2
}

fun part2(input: List<String>): Int {
    val maze = PipeMaze(input)
    val enhanced = enhance(maze)
    val outsideLoop = outsideLoop(enhanced)
    val remapOutsideCoords = outsideLoop.map { unenhance(it) }.toSet()
    val loop = maze.loop()

    var countInside = 0;
    for (y in 0 until maze.maze.size) {
        for (x in 0 until maze.maze[y].length) {
            val coord = y to x
            if (!loop.containsKey(coord) && !remapOutsideCoords.contains(coord)) {
                countInside++
            }
        }
    }

    return countInside
}

