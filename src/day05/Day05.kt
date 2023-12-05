package day05

import byEmptyLines
import overlaps
import readResourceAsBufferedReader
import kotlin.math.max
import kotlin.math.min


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("5_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("5_1.txt").readLines())}")
}

fun part1(input: List<String>): Long {
    val byEmpty = input.byEmptyLines();
    val seeds = byEmpty.first().split(" ").drop(1).map { it.toLong() }
    val almanac = Almanac.parse(byEmpty.drop(1))
    return seeds.minOf {
        almanac.lookup(it)
    }
}

fun part2(input: List<String>): Long {
    val byEmpty = input.byEmptyLines();
    val seeds = byEmpty.first().split(" ").drop(1).map { it.toLong() }
    val seedPairs = mutableListOf<Pair<Long,Long>>()
    for (i in 0 until seeds.size step 2) {
        seedPairs.add(seeds[i] to seeds[i + 1])
    }
    val seedRanges = seedPairs.map { LongRange(it.first, it.first + it.second) }
    val almanac = Almanac.parse(byEmpty.drop(1))

    return seedRanges.flatMap {
        println()
        almanac.lookup(it)
    }
        .minOf { it.first }
}

data class Almanac(val mappers: List<Mapper>) {

    fun lookup(seed: Long): Long = mappers.fold(seed) { acc, mapper ->
        mapper.lookup(acc)
    }

    fun lookup(seed: LongRange): Set<LongRange> {
        return mappers.fold(mutableSetOf(seed)) { acc, mapper ->
            val result = acc.flatMap { mapper.lookup(it) }.toSet()
            println("result: $result from $acc" )
            result.toMutableSet()
        }
    }

    companion object {
        fun parse(mappers: List<String>): Almanac {
            return Almanac(mappers.map { Mapper.parse(it) })
        }
    }

}

data class Mapper(
    val name: String,
    val rules: List<MappingRule>
) {

    fun lookup(v: Long): Long {
        return rules.firstNotNullOfOrNull { it.map(v) } ?: v
    }

    fun lookup(v: LongRange): List<LongRange> {
        val lookups = rules.flatMap { it.map(v) }
        return if (lookups.isEmpty()) {
            listOf(v)
        } else {
            lookups
        }
    }

    companion object {
        fun parse(map: String): Mapper {
            val lines = map.lines()
            val id = lines.first().split(" ").first()
            val rules = lines.drop(1)
                .map { MappingRule.parse(it) }
            return Mapper(id, rules)
        }
    }
}

data class MappingRule(val dst: Long, val src: Long, val l: Long) {

    private val srcRange = LongRange(src, src + l - 1)

    fun map(i: Long): Long? {
        return if (srcRange.contains(i)) {
            dst + (i - srcRange.first)
        } else {
            null
        }
    }

    fun map(i: LongRange): List<LongRange> {
        return if(i.overlaps(srcRange)) {
            val left = LongRange(i.first, max(i.first, srcRange.first) - 1)
            val middle = LongRange(max(i.first, srcRange.first), min(i.last, srcRange.last))
            val right = LongRange(min(i.last, srcRange.last) + 1, i.last)

            return listOf(
                left,
                LongRange(middle.first + dst - src, middle.last + dst - src),
                right
            ).filter { !it.isEmpty() }
        } else {
            emptyList()
        }
    }

    companion object {
        fun parse(line: String): MappingRule {
            val (d, s, l) = line.split(" ").map { it.toLong() }
            return MappingRule(d, s, l)
        }
    }
}
