package day06

import quadratic
import readResourceAsBufferedReader


fun main() {
    //println("part 1: ${part1(readResourceAsBufferedReader("6_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("6_1.txt").readLines())}")
}

val whitespace = Regex("\\s+")

data class Race(val time: Long, val distance: Long) {

    fun race(wait: Long): Long {
        val speed = wait
        val remaining = time - wait
        return remaining * speed
    }

    fun winners(): List<Long> = (1 until distance).map { race(it) }
        .filter { it > distance }

    fun winnersQ(): Long {
        //return wait*time - wait*wait = distance
        // wait*time - wait*wait - distance = 0
        // -w^2 + w * time - distance = 0
        val (l, r) = quadratic(-1, time, -distance).map { it.toLong() }

        return r - l
    }
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

fun part1(input: List<String>): Long {
    val races = parseRaces(input)
    return races.map { it.winnersQ() }
        .reduce(Long::times)
}

fun part2(input: List<String>): Long {
    val races = parseRace(input)
    return races.winnersQ()
}

