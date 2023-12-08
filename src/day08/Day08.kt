package day08

import byEmptyLines
import lcm
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("8_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("8_1.txt").readLines())}")
}

data class CycleInfo(
    val offset: Int,
    val length: Int,
    val end: String
)

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

    fun traverse(instr: String, start: String): Sequence<String> {
        val endlessInstructions= endlessInstructions(instr)

        return endlessInstructions.scan(start) { acc, c ->
            move(acc, c)
        }
    }

    fun traverse1(instr: String): List<String> {
        return traverse(instr, "AAA").takeWhile { it != "ZZZ" }.toList()
    }

    fun cycle(instr: String, start: String, end: (String) -> Boolean): CycleInfo {
        val seen = mutableMapOf<String, Int>()
        var curr = start
        var counter = 0

        while(!end(curr) || !seen.containsKey(curr)) {
            if (end(curr)) {
                seen[curr] = counter
            }
            val dir = instr[counter % instr.length]
            curr = move(curr, dir)
            counter++
        }

        return CycleInfo(seen[curr]!!, counter - seen[curr]!!, curr)
    }

    fun traverse2(instr: String): Long {
        val startingNodes = nodes.keys.filter { it.endsWith("A") }
        val cycles = startingNodes.map { cycle(instr, it) { inner -> inner.endsWith("Z") } }
        // offsets == cycle length
        val lengths = cycles.map { it.length.toLong() }.toTypedArray().toLongArray()
        return lcm(*lengths)
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
    return desertMap.traverse1(directions).size
}

fun part2(input: List<String>): Any {
    val (directions, map) = input.byEmptyLines()
    val desertMap = DesertMap.parse(map)

    return desertMap.traverse2(directions)
}