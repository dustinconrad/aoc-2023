package day08

import byEmptyLines
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("8_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("8_1.txt").readLines())}")
}

data class DesertMap(val nodes: Map<String, Pair<String, String>>) {

    fun endlessInstructions(instr: String) = sequence {
        while (true) {
            yieldAll(instr.asSequence())
        }
    }

    fun move(acc: String, c: Char): String {
        val next = nodes[acc]!!
        return when(c) {
            'R' -> next.second
            'L' -> next.first
            else -> throw IllegalArgumentException("Unknown direction $c")
        }
    }

    fun traverse(instr: String): List<String> {
        val endlessInstructions= endlessInstructions(instr)

        val visits = endlessInstructions.scan("AAA") { acc, c ->
            move(acc, c)
        }

        val steps = visits.takeWhile { it != "ZZZ" }.toList()

        return steps
    }

    fun traverse2(instr: String): List<List<String>> {
        val endlessInstructions= endlessInstructions(instr)

        val startingNodes = nodes.keys.filter { it.endsWith("A") }

        val visits = endlessInstructions.scan(startingNodes) { acc, c ->
            acc.map { move(it, c) }
        }

        val steps = visits.takeWhile { acc -> acc.any { !it.endsWith("Z") } }.toList()

        return steps
    }

    companion object {

        val node = Regex("""(\w+) = \((\w+), (\w+)\)""")
        fun parse(input: String): DesertMap {
            fun parseNode(line: String): Triple<String, String, String> {
                val result = node.matchEntire(line)
                val (_, n, l, r) = result!!.groupValues
                return Triple(n, l, r)
            }

            val nodes = input.split("\n")
                .map { parseNode(it) }
                .map { it.first to Pair(it.second, it.third) }
                .toMap()

            return DesertMap(nodes)
        }
    }
}

fun part1(input: List<String>): Int {
    val (directions, map) = input.byEmptyLines()
    val desertMap = DesertMap.parse(map)
    return desertMap.traverse(directions).size
}

fun part2(input: List<String>): Int {
    val (directions, map) = input.byEmptyLines()
    val desertMap = DesertMap.parse(map)
    return desertMap.traverse2(directions).size
}