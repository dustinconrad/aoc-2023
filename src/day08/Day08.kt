package day08

import byEmptyLines
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("8_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("8_1.txt").readLines())}")
}

data class DesertMap(val nodes: Map<String, Pair<String, String>>) {

    fun traverse(instr: String): List<String> {
        val endlessInstructions = sequence {
            while (true) {
                yieldAll(instr.asSequence())
            }
        }

        val visits = endlessInstructions.scan("AAA") { acc, c ->
            val next = nodes[acc]!!
            when(c) {
                'R' -> next.second
                'L' -> next.first
                else -> throw IllegalArgumentException("Unknown direction $c")
            }
        }

        val steps = visits.takeWhile { it != "ZZZ" }.toList()

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
    return 0
}