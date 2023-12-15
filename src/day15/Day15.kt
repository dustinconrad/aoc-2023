package day15

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("15_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("15_1.txt").readLines())}")
}

fun String.hashalgo(): Int {
    return this.fold(0) { a, c ->
        var acc = a
        acc += c.code
        acc *= 17
        acc % 256
    }
}

fun execute(state: Array<LinkedHashMap<String,Int>>, instr: String) {
    val split = instr.split("=")
    if (split.size == 2) {
        // equals
        val label = split[0]
        val focus = split[1].toInt()
        val box = label.hashalgo()
        state[box][label] = focus
    } else {
        // minus
        val label = instr.dropLast(1)
        val box = label.hashalgo()
        state[box].remove(label)
    }
}

fun focalLength(state: Array<LinkedHashMap<String,Int>>): Int {
    return state.mapIndexed { box, lenses ->
        var slot = 0
        var sum = 0
        for ((label, focalLength) in lenses) {
            sum += (box + 1)*(slot++ + 1)*focalLength
        }
        sum
    }.sum()
}

fun part1(input: List<String>): Int {
    return input.joinToString("")
        .split(",")
        .sumOf { it.hashalgo() }
}

fun part2(input: List<String>): Int {
    val endState = input.joinToString("").split(",")
        .fold(Array(256) { LinkedHashMap<String, Int>() }) { acc, instr ->
            execute(acc, instr)
            acc
        }

    return focalLength(endState)
}