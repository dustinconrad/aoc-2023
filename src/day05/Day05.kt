package day05

import byEmptyLines
import merge
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
    return seedOutput(
        seeds.map { it..it },
        almanac
    )
}

fun part2(input: List<String>): Long {
    val byEmpty = input.byEmptyLines();
    val seeds = byEmpty.first().split(" ").drop(1).map { it.toLong() }
    val seedPairs = mutableListOf<Pair<Long,Long>>()
    for (i in 0 until seeds.size step 2) {
        seedPairs.add(seeds[i] to seeds[i + 1])
    }
    val seedRanges = seedPairs.map { LongRange(it.first, it.first + it.second - 1) }
    val almanac = Almanac.parse(byEmpty.drop(1))

    return seedOutput(seedRanges, almanac)
}

fun seedOutput(seedRanges: List<LongRange>, almanac: Almanac): Long {
    return seedRanges.flatMap {
        almanac.lookup(it)
    }
        .minOf { it.first }
}

data class Almanac(val mappers: List<Mapper>) {

    fun lookup(seed: LongRange): Set<LongRange> {
        return mappers.fold(mutableSetOf(seed)) { acc, mapper ->
            val result = acc.flatMap { mapper.lookup(it) }.toSet()
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

    fun lookup(v: LongRange): List<LongRange> {
        val overlappingRules = rules.mapNotNull { it.map(v) }
        if (overlappingRules.isEmpty()) {
//            println("$name: $v -> $v")
            return listOf(v)
        }
        val remapped = overlappingRules.map { it.second }.sortedBy { it.first }
        val usedRange = overlappingRules.map { it.first }
        val mergedUsedRanged = usedRange.merge().sortedBy { it.first }

        val result = remapped.toMutableList()

        var curr = v

        for(r in mergedUsedRanged) {
            val newRange = LongRange(curr.first, r.first - 1)
            if (!newRange.isEmpty()) {
                result.add(newRange)
            }
            curr = LongRange(r.last + 1, curr.last)
        }

        return result.sortedBy { it.first }
    }

    companion object {
        fun parse(map: String): Mapper {
            val lines = map.lines()
            val id = lines.first().split(" ").first()
            val rules = lines.drop(1)
                .map { MappingRule.parse(it) }
            return Mapper(id, rules.sortedBy { it.src })
        }
    }
}

data class MappingRule(val dst: Long, val src: Long, val l: Long) {

    val srcRange = LongRange(src, src + l - 1)

    fun map(i: LongRange): Pair<LongRange, LongRange>? {
        return if(i.overlaps(srcRange)) {
            val middle = LongRange(max(i.first, srcRange.first), min(i.last, srcRange.last))
            return middle to
                    LongRange(middle.first + dst - src, middle.last + dst - src)
        } else {
            null
        }
    }

    companion object {
        fun parse(line: String): MappingRule {
            val (d, s, l) = line.split(" ").map { it.toLong() }
            return MappingRule(d, s, l)
        }
    }
}
