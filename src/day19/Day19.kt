package day19

import byEmptyLines
import readResourceAsBufferedReader
import java.util.UUID


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("19_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("19_1.txt").readLines())}")
}

fun part1(input: List<String>): Long {
    val (workflows, p) = input.byEmptyLines()
    val factory = Factory.parse(workflows.lines())
    val parts = p.lines().map { Part2.parse(it) }

    parts.forEach { factory.execute(it) }

    return factory.part1()
}

fun part2(input: List<String>): Int {
    return 0
}

val NULL_LABEL = UUID.randomUUID().toString()

data class Part(val x: Long, val m: Long, val a: Long, val s: Long) {

    fun getByCategory(category: String): Long {
        return when(category) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
    }

    val part1 = x + m + a + s

    companion object {

        val p = Regex("""\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""")

        fun parse(line: String): Part {
            val (_, x, m, a, s) = p.matchEntire(line)!!.groupValues
            return Part(x.toLong(), m.toLong(), a.toLong(), s.toLong())
        }
    }
}

data class Part2(val x: LongRange, val m: LongRange, val a: LongRange, val s: LongRange) {

    fun getByCategory(category: String): LongRange {
        return when(category) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
    }

    fun rating(): Long {
        val xSum = x.sumOf { it * m.count() * a.count() * s.count() }
        val mSum = m.sumOf { it * x.count() * a.count() * s.count() }
        val aSum = a.sumOf { it * x.count() * m.count() * s.count() }
        val sSum = s.sumOf { it * x.count() * m.count() * a.count() }
        return xSum + mSum + aSum + sSum
    }

    fun copyBut(category: String, newRange: LongRange): Part2 {
        return when(category) {
            "x" -> copy(x = newRange)
            "m" -> copy(m = newRange)
            "a" -> copy(a = newRange)
            "s" -> copy(s = newRange)
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
    }

    companion object {

        val p = Regex("""\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""")

        fun parse(line: String): Part2 {
            val (_, xs, ms, `as`, ss) = p.matchEntire(line)!!.groupValues
            val x = xs.toLong()
            val m = ms.toLong()
            val a = `as`.toLong()
            val s = ss.toLong()
            return Part2(x until x + 1, m until m + 1, a until a + 1, s until s + 1)
        }
    }
}

data class Workflow(val label: String, val rules: List<Rule>) {

    fun execute(part: Part2): Map<String, List<Part2>> {
        return rules.fold(mutableMapOf<String, MutableList<Part2>>(
            NULL_LABEL to mutableListOf(part)
        )) { acc, rule ->
            val unJumped = acc.remove(NULL_LABEL) ?: emptyList()
            unJumped.forEach { unJumpedPart ->
                rule.execute(unJumpedPart).forEach { (k, v) ->
                    val curr = acc.getOrPut(k) { mutableListOf() }
                    curr.add(v)
                }
            }
            acc
        }
    }

    companion object {

        val p = Regex("""(\w+)\{([^}]+)}""")

        fun parse(line: String): Workflow {
            val (_, label, ruleBody) = p.matchEntire(line)!!.groupValues
            val rules = ruleBody.split(',').map { Rule.parse(it) }
            return Workflow(label, rules)
        }
    }

}

class Factory(private val workflows: Map<String, Workflow>) {

    private val accepted = mutableSetOf<Part2>()
    private val rejected = mutableSetOf<Part2>()

    fun part1(): Long {
        return accepted.sumOf { it.rating() }
    }

    fun execute(part: Part2) {
        val parts = linkedMapOf("in" to mutableListOf(part))
        while(parts.isNotEmpty()) {
            val currLabel = parts.keys.first()
            val currParts = parts.remove(currLabel)!!
            val currWorkflow = workflows[currLabel]!!
            for (currPart in currParts) {
                currWorkflow.execute(part).forEach { (k, v) ->
                    val mergeValue = parts.getOrPut(k) { mutableListOf() }
                    mergeValue.addAll(v)
                }
            }
            parts.remove("A")?.also {
                accepted.addAll(it)
            }
            parts.remove("R")?.also {
                rejected.addAll(it)
            }
        }
    }

    companion object {

        fun parse(input: List<String>): Factory {
            val workflows = input.map { Workflow.parse(it) }.associateBy { it.label }

            return Factory(workflows)
        }

    }

}

sealed class Rule {

    abstract fun execute(part: Part2): Map<String, Part2>

    data class ConditionalJump(val category: String, val cond: (LongRange, Long) -> Pair<LongRange, LongRange>, val const: Long, val jmp: String): Rule() {

        override fun execute(part: Part2): Map<String, Part2> {
            val partRange = part.getByCategory(category)

            val (included, excluded) = cond.invoke(partRange, const)

            return mapOf(
                jmp to part.copyBut(category, included),
                NULL_LABEL to part.copyBut(category, excluded)
            )
        }

    }

    data class Jump(val jmp: String): Rule() {

        override fun execute(part: Part2): Map<String, Part2> {
            return mapOf(jmp to part)
        }

    }

    companion object {

        val condition = Regex("""(\w+)(.)(\d+):(\w+)""")

        private fun compareLt(pRange: LongRange, c: Long): Pair<LongRange, LongRange> {
            return when {
                pRange.last < c  -> pRange to LongRange.EMPTY
                pRange.first >= c -> LongRange.EMPTY to pRange
                else  -> (pRange.first until c) to (c .. pRange.last)
            }
        }

        private fun compareGt(pRange: LongRange, c: Long): Pair<LongRange, LongRange> {
            return when {
                pRange.first > c -> pRange to LongRange.EMPTY
                pRange.last <= c -> LongRange.EMPTY to pRange
                else  ->  (c + 1 .. pRange.last) to (pRange.first .. c)
            }
        }

        fun parse(sec: String): Rule {
            val conditionMatch = condition.matchEntire(sec)
            return if (conditionMatch != null) {
                val (_, cat, cmp, const, label) = conditionMatch.groupValues
                val compare: (LongRange, Long) -> Pair<LongRange, LongRange> = if (cmp == "<") {
                    ::compareLt
                } else {
                    ::compareGt
                }
                ConditionalJump(
                    cat,
                    compare,
                    const.toLong(),
                    label
                )
            } else {
                Jump(sec)
            }
        }
    }

}