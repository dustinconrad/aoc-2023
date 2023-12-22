package day18

import Pos
import addVec
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("18_1.txt").readLines())}")
    // println("part 2: ${part2(readResourceAsBufferedReader("18_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val instructions = input.map { DigInstruction.parse(it) }
    val lagoon = Lagoon()
    lagoon.run(instructions)
    //println(lagoon.debugTrench())
    return lagoon.digout()
}

fun part2(input: List<String>): Int {
    return 0
}

data class DigInstruction(
    val dir: String,
    val distance: Int,
    val color: String
) {

    val vector = when(dir) {
        "R" -> 0 to 1
        "L" -> 0 to -1
        "U" -> -1 to 0
        "D" -> 1 to 0
        else -> throw IllegalStateException("Unknown dir: $dir")
    }

    val hexColor = color.toInt(16)

    companion object {

        private val pattern = Regex("""(\w) (\d+) \(#(\w+)\)""")

        fun parse(line: String): DigInstruction {
            val match = pattern.matchEntire(line)
            val (_, dir, magnitude, color) = match!!.groupValues
            return DigInstruction(dir, magnitude.toInt(), color)
        }
    }
}

data class Lagoon(
    val state: MutableMap<Pos, String> = mutableMapOf((0 to 0) to "000000"),
    var pos: Pos = 0 to 0
) {

    fun digout(): Int {
        val notDugout = mutableSetOf<Pos>()

        val minY = state.minOf { it.key.first - 1 }
        val minX = state.minOf { it.key.second - 1 }
        val maxY = state.maxOf { it.key.first + 1}
        val maxX = state.maxOf { it.key.second + 1}

        val q = mutableSetOf<Pos>()
        q.add(minY to minX)

        while(q.isNotEmpty()) {
            val curr = q.first()
            q.remove(curr)
            notDugout.add(curr)

            val next = listOf(curr.addVec(0 to 1), curr.addVec(0 to -1),
                curr.addVec(1 to 0), curr.addVec(-1 to 0))
                .filter { (minX .. maxX).contains(it.second) && (minY .. maxY).contains(it.first) }
                .filter { !state.contains(it) }
                .filter { !notDugout.contains(it) }
                .filter { !q.contains(it) }

            q.addAll(next)
        }

        val width = maxX - minX + 1
        val height = maxY - minY + 1

        val totalArea = width * height

        val filled = notDugout.size

        return totalArea - filled
    }

    fun run(instructions: List<DigInstruction>) {
        instructions.forEach { execute(it) }
    }

    fun execute(instr: DigInstruction) {
        repeat(instr.distance) {
            pos = pos.addVec(instr.vector)
            state[pos] = instr.color
        }
    }

    fun debugTrench(): String {
        val minY = state.minOf { it.key.first - 1 }
        val minX = state.minOf { it.key.second - 1 }
        val maxY = state.maxOf { it.key.first + 1}
        val maxX = state.maxOf { it.key.second + 1}

        return (minY .. maxY).joinToString("\n") { y ->
            (minX .. maxX).joinToString("") { x ->
                if (state.contains(y to x)) {
                    "#"
                } else {
                    "."
                }
            }
        }
    }

}