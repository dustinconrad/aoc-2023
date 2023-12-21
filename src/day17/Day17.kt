package day17

import Pos
import Vec
import addVec

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("17_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("17_1.txt").readLines())}")
}

data class Tile(
    val min: MutableSet<Vec> = mutableSetOf(),
    val momentumValues: MutableMap<Vec,Int> = mutableMapOf(),
    var minValue: Int = Int.MAX_VALUE
) {
    fun setMin(momentum: Vec, v: Int) {
        momentumValues[momentum] = v
        if (v == minValue) {
            min.add(momentum)
        } else if (v < minValue) {
            min.clear()
            min.add(momentum)
            minValue = v
        }
    }
}

fun notTooStraightPath(input: List<String>): Array<Array<Tile>> {
    val height = input.size
    val width = input[0].length
    val dp = Array(height) { Array(width) { Tile() } }
    val cmp: Comparator<Pos> = Comparator.comparingInt { dp[it.first][it.second].minValue }

    val visited = mutableSetOf<Pos>()
    val tentative = mutableListOf<Pos>()
    val seen = mutableSetOf<Pos>()
    tentative.add(0 to 0)
    dp[0][0].minValue = 0

    while(tentative.isNotEmpty()) {
        tentative.sortWith(cmp)
        val curr = tentative.removeFirst()
        visited.add(curr)

        val currValues = dp[curr.first][curr.second]

        // update right tentative values
        val right = curr.first to curr.second + 1
        if (right.second < width && !visited.contains(right)) {
            val rightValues = dp[right.first][right.second]
            val heat = input[right.first][right.second].code - '0'.code
            val min = if (currValues.min == setOf(0 to 3)) {
                // min value is from rolling in a row
                currValues.momentumValues.filterNot { it.key == 0 to 3 }
                    .minBy { it.value }
                    .value
            } else {
                currValues.minValue
            }
            rightValues.setMin(0 to 1, min + heat)
            (1 .. 2).map { 0 to it }
                .filter { currValues.momentumValues.contains(it) }
                .forEach {
                    rightValues.setMin(it.addVec(0 to 1), currValues.momentumValues[it]!! + heat)
                }
            if (seen.add(right)) {
                tentative.add(right)
            }
        }
        //update left
        val left = curr.first to curr.second - 1
        if (left.second > 0 && !visited.contains(left)) {
            val leftValues = dp[left.first][left.second]
            val heat = input[left.first][left.second].code - '0'.code
            val min = if (currValues.min == setOf(0 to -3)) {
                // min value is from rolling in a row
                currValues.momentumValues.filterNot { it.key == 0 to -3 }
                    .minBy { it.value }
                    .value
            } else {
                currValues.minValue
            }
            leftValues.setMin(0 to -1, min + heat)
            (-2 .. -1).map { 0 to it }
                .filter { currValues.momentumValues.contains(it) }
                .forEach {
                    leftValues.setMin(it.addVec(0 to -1), currValues.momentumValues[it]!! + heat)
                }
            if (seen.add(left)) {
                tentative.add(left)
            }
        }
        // update down
        val down = curr.first + 1 to curr.second
        if (down.first < height && !visited.contains(down)) {
            val downValues = dp[down.first][down.second]
            val heat = input[down.first][down.second].code - '0'.code
            val min = if (currValues.min == setOf(3 to 0)) {
                // min value is from rolling in a row
                currValues.momentumValues.filterNot { it.key == 3 to 0 }
                    .minBy { it.value }
                    .value
            } else {
                currValues.minValue
            }
            downValues.setMin(1 to 0, min + heat)
            (1 .. 2).map { it to 0 }
                .filter { currValues.momentumValues.contains(it) }
                .forEach {
                    downValues.setMin(it.addVec(1 to 0), currValues.momentumValues[it]!! + heat)
                }
            if (seen.add(down)) {
                tentative.add(down)
            }
        }
        // update up
        val up = curr.first - 1 to curr.second
        if (up.first > 0 && !visited.contains(up)) {
            val upValues = dp[up.first][up.second]
            val heat = input[up.first][up.second].code - '0'.code
            val min = if (currValues.min == setOf(-3 to 0)) {
                // min value is from rolling in a row
                currValues.momentumValues.filterNot { it.key == -3 to 0 }
                    .minBy { it.value }
                    .value
            } else {
                currValues.minValue
            }
            upValues.setMin(-1 to 0, min + heat)
            (-2 .. -1).map { it to 0 }
                .filter { currValues.momentumValues.contains(it) }
                .forEach {
                    upValues.setMin(it.addVec(-1 to 0), currValues.momentumValues[it]!! + heat)
                }
            if (seen.add(up)) {
                tentative.add(right)
            }
        }
    }

    return dp
}

fun printDp(result: Array<Array<Tile>>) {
    val mins = result.joinToString("\n") { it.joinToString(" ") { it.minValue.toString() } }
    println(mins)
}

fun part1(input: List<String>): Int {
    val dp = notTooStraightPath(input)

    println(input.joinToString("\n"))
    printDp(dp)

    return dp[input.lastIndex][input[0].lastIndex].minValue
}

fun part2(input: List<String>): Int {
    return 0
}