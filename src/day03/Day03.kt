package day03

import Coord2
import overlaps
import readResourceAsBufferedReader
import kotlin.math.max
import kotlin.math.min


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("3_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("3_1.txt").readLines())}")
}

typealias PartCoord = Pair<Int, IntRange>

typealias PartNumber = Pair<PartCoord, Int>

fun partNumber(y: Int, r: IntRange, v: Int) = (y to r) to v

val numberRegex = Regex("""\d+""")
val symbol = Regex("""[^\d.]""")

data class EngineSchematic(
    val grid: List<String>
) {

    val partNumbers: List<PartNumber> = grid.flatMapIndexed { idx, line ->
        numberRegex.findAll(line).map { mr ->
            partNumber(idx, mr.range, mr.value.toInt())
        }.toList()
    }

    val validPartNumbers = partNumbers.filter { isValidPn(it) }

    val validPartsByRow = validPartNumbers.groupBy { it.first.first }

    fun isValidPc(pc: PartCoord): Boolean {
        val minX = max(0, pc.second.first - 1)
        val maxX = min(grid[0].length - 1, pc.second.last + 1)

        val xRange = IntRange(minX, maxX)

        if (pc.first > 0 && symbol.containsMatchIn(grid[pc.first - 1].substring(xRange))) {
            return true
        }
        if (symbol.matches(grid[pc.first][minX].toString())) {
            return true
        }
        if (symbol.matches(grid[pc.first][maxX].toString())) {
            return true
        }
        if (pc.first < grid.size - 1 && symbol.containsMatchIn(grid[pc.first + 1].substring(xRange))) {
            return true
        }
        return false
    }

    fun isValidPn(pn: PartNumber): Boolean = isValidPc(pn.first)

    val gears: List<Coord2> = grid.flatMapIndexed { idx, line ->
        line.mapIndexed { idx, c -> idx to c }
            .filter { it.second == '*' }
            .map { idx to it.first }
    }

    fun partsForGear(coord: Coord2): List<PartNumber> {
        val minX = max(0, coord.second - 1)
        val maxX = min(grid[0].length - 1, coord.second + 1)
        val range = IntRange(minX, maxX)

        val gears = mutableListOf<PartNumber>()

        fun partsForRow(r: Int): List<PartNumber> = validPartsByRow.getOrDefault(r, emptyList())
            .filter { range.overlaps(it.first.second) }

        gears.addAll(partsForRow(coord.first - 1))
        gears.addAll(partsForRow(coord.first))
        gears.addAll(partsForRow(coord.first + 1))

        return gears.toList()
    }

    fun validGears(): List<Pair<PartNumber, PartNumber>> = gears.map { partsForGear(it) }
            .filter { it.size == 2 }
            .map { it[0] to it[1] }

}

fun part1(input: List<String>): Int {
    val engine = EngineSchematic(input)
    return engine.validPartNumbers.sumOf { it.second }
}

fun part2(input: List<String>): Int {
    val engine = EngineSchematic(input)
    return engine.validGears().sumOf { it.first.second * it.second.second }
}
