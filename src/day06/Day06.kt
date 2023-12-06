package day06

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("6_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("6_1.txt").readLines())}")
}

val whitespace = Regex("\\s+")

data class Race(val time: Long, val distance: Long) {

    fun race(wait: Long): Long {
        val speed = wait
        val remaining = time - wait
        return remaining * speed
        //return wait*time - wait*wait
    }

    fun winners(): List<Long> = (1 until distance).map { race(it) }
        .filter { it > distance }

}

fun parseRaces(input: List<String>): List<Race> {
    val (timeString, distanceString) = input
    val times = whitespace.split(timeString.split(":")[1].trim()).map { it.toLong() }
    val distances = whitespace.split(distanceString.split(":")[1].trim()).map { it.toLong() }

    return times.zip(distances).map { Race(it.first, it.second) }
}

fun parseRace(input: List<String>): Race {
    val (timeString, distanceString) = input
    val time = whitespace.replace(timeString.split(":")[1], "").toLong()
    val distance = whitespace.replace(distanceString.split(":")[1], "").toLong()

    return Race(time, distance)
}

fun part1(input: List<String>): Int {
    val races = parseRaces(input)
    return races.map { it.winners().size }
        .reduce(Int::times)
}

fun part2(input: List<String>): Int {
    val races = parseRace(input)
    return races.winners().size
}

