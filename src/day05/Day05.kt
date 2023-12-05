package day05

import byEmptyLines
import readResourceAsBufferedReader
import kotlin.math.max
import kotlin.math.min


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("5_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("5_1.txt").readLines())}")
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


    val almanac = Almanac.parse(byEmpty.drop(1))
    return seeds.minOf {
        almanac.lookup(it)
    }
}

data class Almanac(val mappers: List<Mapper>) {

    fun lookup(seed: Long): Long = mappers.fold(seed) { acc, mapper ->
        println("$acc -> ${mapper.name} -> ${mapper.lookup(acc)}")
        mapper.lookup(acc)
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

    companion object {
        fun parse(line: String): MappingRule {
            val (d, s, l) = line.split(" ").map { it.toLong() }
            return MappingRule(d, s, l)
        }
    }
}
