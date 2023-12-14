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
    } }

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
        roundsByColumn()
        // each column
        roundStones = roundStones.map { (col, orderedRounds) ->
            val rectangles = rectanglesByColumn[col]!!
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

    private fun roundsByColumn() {
        roundStones = roundStones.values.flatten().groupBy { it.second }
            .mapValues { it.value.sortedBy { it.first } }
    }

    fun loadNorth(): Int {
        roundsByColumn()
        return roundStones.values.sumOf { stones ->
            stones.sumOf { height - it.first }
        }
    }
}

fun part1(input: List<String>): Int {
    val dish = Dish(input)
    dish.tiltNorth()
    return dish.loadNorth()
}

fun part2(input: List<String>): Int {
    return 0
}
