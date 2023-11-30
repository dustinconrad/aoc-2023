package day01

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("1_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("1_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}