package day14

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("14_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("14_1.txt").readLines())}")
}

class Dish(grid: List<String>) {

    val height = grid.size

    val width = grid[0].length

    val rectangles = grid.flatMapIndexed { y, line -> line.mapIndexedNotNull{ x, c ->
        if(line[x] == '#') {
            y to x
        } else {
            null
        }
    } }.toSet()

    val rectanglesByRow = rectangles.groupBy { it.first }
        .mapValues { (k, v) -> v.sortedBy { it.second } }

    val rectanglesByColumn = rectangles.groupBy { it.second }
        .mapValues { (k, v) -> v.sortedBy { it.first } }

    // simulate horizontal tilt
    private var roundStones: Map<Int, List<Pair<Int, Int>>> = grid.flatMapIndexed { y, line -> line.mapIndexedNotNull{ x, c ->
        if(line[x] == 'O') {
            y to x
        } else {
            null
        }
    } }.groupBy { it.first }

    fun tiltNorth() {
        roundsByColumn(Comparator.naturalOrder())
        // each column
        roundStones = roundStones.map { (col, orderedRounds) ->
            val rectangles = rectanglesByColumn[col] ?: emptyList()
            val colResult = mutableListOf<Pair<Int,Int>>()

            var roundsPtr = 0
            var recPtr = 0
            var lastObstruction = -1
            while (roundsPtr <= orderedRounds.lastIndex) {
                val roundsHead = orderedRounds[roundsPtr]
                if (recPtr > rectangles.lastIndex || roundsHead.first < rectangles[recPtr].first) {
                    val movedStone = lastObstruction + 1 to col
                    colResult.add(movedStone)
                    lastObstruction = movedStone.first
                    roundsPtr++
                } else {
                    lastObstruction = rectangles[recPtr].first
                    recPtr++
                }
            }
            col to colResult
        }.toMap()
    }

    fun tiltSouth() {
        roundsByColumn(Comparator.naturalOrder<Int>().reversed())
        // each column
        roundStones = roundStones.map { (col, orderedRounds) ->
            val rectangles = rectanglesByColumn[col] ?: emptyList()
            val colResult = mutableListOf<Pair<Int,Int>>()

            var roundsPtr = 0
            var recPtr = rectangles.lastIndex
            var lastObstruction = height
            while (roundsPtr <= orderedRounds.lastIndex) {
                val roundsHead = orderedRounds[roundsPtr]
                if (recPtr < 0 || roundsHead.first > rectangles[recPtr].first) {
                    val movedStone = lastObstruction - 1 to col
                    colResult.add(movedStone)
                    lastObstruction = movedStone.first
                    roundsPtr++
                } else {
                    lastObstruction = rectangles[recPtr].first
                    recPtr--
                }
            }
            col to colResult
        }.toMap()
    }

    fun tiltWest() {
        roundsByRow(Comparator.naturalOrder())
        // each column
        roundStones = roundStones.map { (row, orderedRounds) ->
            val rectangles = rectanglesByRow[row] ?: emptyList()
            val rowResult = mutableListOf<Pair<Int,Int>>()

            var roundsPtr = 0
            var recPtr = 0
            var lastObstruction = -1
            while (roundsPtr <= orderedRounds.lastIndex) {
                val roundsHead = orderedRounds[roundsPtr]
                if (recPtr > rectangles.lastIndex || roundsHead.second < rectangles[recPtr].second) {
                    val movedStone = row to lastObstruction + 1
                    rowResult.add(movedStone)
                    lastObstruction = movedStone.second
                    roundsPtr++
                } else {
                    lastObstruction = rectangles[recPtr].second
                    recPtr++
                }
            }
            row to rowResult
        }.toMap()
    }

    fun tiltEast() {
        roundsByRow(Comparator.naturalOrder<Int?>().reversed())
        // each column
        roundStones = roundStones.map { (row, orderedRounds) ->
            val rectangles = rectanglesByRow[row] ?: emptyList()
            val rowResult = mutableListOf<Pair<Int,Int>>()

            var roundsPtr = 0
            var recPtr = rectangles.lastIndex
            var lastObstruction = width
            while (roundsPtr <= orderedRounds.lastIndex) {
                val roundsHead = orderedRounds[roundsPtr]
                if (recPtr < 0 || roundsHead.second > rectangles[recPtr].second) {
                    val movedStone = row to lastObstruction - 1
                    rowResult.add(movedStone)
                    lastObstruction = movedStone.second
                    roundsPtr++
                } else {
                    lastObstruction = rectangles[recPtr].second
                    recPtr--
                }
            }
            row to rowResult
        }.toMap()
    }

    fun cycle() {
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
    }

    private fun roundsByColumn(cmp: Comparator<Int>) {
        roundStones = roundStones.values.flatten().groupBy { it.second }
            .mapValues { it.value.sortedWith { l, r -> cmp.compare(l.first, r.first) } }
    }

    private fun roundsByRow(cmp: Comparator<Int>) {
        roundStones = roundStones.values.flatten().groupBy { it.first }
            .mapValues { it.value.sortedWith { l, r -> cmp.compare(l.second, r.second) } }
    }

    fun loadNorth(): Int {
        roundsByColumn(Comparator.naturalOrder())
        return roundStones.values.sumOf { stones ->
            stones.sumOf { height - it.first }
        }
    }

    override fun toString(): String {
        val rounds = roundStones.values.flatten().toSet()
        return (0 until height).joinToString("\n") { y ->
            (0 until width).joinToString("") { x ->
                when {
                    rectangles.contains(y to x) -> "#"
                    rounds.contains(y to x) -> "O"
                    else -> "."
                }
            }
        }
    }

    companion object {

        fun states(dish: Dish): Sequence<String> = sequence {
            while(true) {
                yield(dish.toString())
                dish.cycle()
            }
        }

    }
}

fun part1(input: List<String>): Int {
    val dish = Dish(input)
    dish.tiltNorth()
    return dish.loadNorth()
}

fun part2(input: List<String>): Int {
    val dish = Dish(input)
    val states = Dish.states(dish)
    val seen = mutableMapOf<String,Int>()
    var step = 0
    var cycleState = ""
    for (state in states) {
        if (seen.contains(state)) {
            cycleState = state
            break
        } else {
            seen[state] = step++
        }
    }
    val cycleStart = seen[cycleState]!!
    val cycleLength = step - cycleStart!!
    val cycle = seen.map { it.value to it.key }
        .filter { cycleStart <= it.first && it.first < step }
        .map { it.first - cycleStart to it.second }
        .toMap()

    val cycleStep = (1000000000 - cycleStart) % cycleLength
    val endState = cycle[cycleStep]!!

    val end = Dish(endState.split("\n"))
    return end.loadNorth()
}
