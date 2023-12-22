package day19

import byEmptyLines
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("19_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("19_1.txt").readLines())}")
}

fun part1(input: List<String>): Long {
    val (workflows, p) = input.byEmptyLines()
    val factory = Factory.parse(workflows.lines())
    val parts = p.lines().map { Part.parse(it) }

    parts.forEach { factory.execute(it) }

    return factory.part1()
}

fun part2(input: List<String>): Int {
    return 0
}

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

data class Workflow(val label: String, val rules: List<Rule>) {

    fun execute(part: Part): String? {
        return rules.firstNotNullOfOrNull { it.execute(part) }
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

    private val accepted = mutableSetOf<Part>()
    private val rejected = mutableSetOf<Part>()
    private val cycle = mutableSetOf<Part>()

    fun part1(): Long {
        return accepted.sumOf { it.part1 }
    }

    fun execute(part: Part) {
        val seen = mutableSetOf<String>()
        val labels = generateSequence("in") { label ->
            when(label) {
                "A" -> null
                "R" -> null
                else -> {
                    val workflow = workflows[label]!!
                    workflow.execute(part)
                }
            }
        }

        var last: String? = null

        for (label in labels) {
            last = label
            if (!seen.add(label)) {
                break
            }
        }

        when (last) {
            "A" -> {
                accepted.add(part)
            }
            "R" -> {
                rejected.add(part)
            }
            else -> {
                cycle.add(part)
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

    abstract fun execute(part: Part): String?

    data class ConditionalJump(val category: String, val cond: (Long, Long) -> Boolean, val const: Long, val jmp: String): Rule() {

        override fun execute(part: Part): String? {
            val l = part.getByCategory(category)
            return if (cond.invoke(l, const)) {
                jmp
            } else {
                null
            }
        }
    }

    data class Jump(val jmp: String): Rule() {

        override fun execute(part: Part): String {
            return jmp
        }

    }

    companion object {

        val condition = Regex("""(\w+)(.)(\d+):(\w+)""")

        fun parse(sec: String): Rule {
            val conditionMatch = condition.matchEntire(sec)
            return if (conditionMatch != null) {
                val (_, cat, cmp, const, label) = conditionMatch.groupValues
                val compare: (Long, Long) -> Boolean = if (cmp == "<") {
                    { l, r -> l < r }
                } else {
                    { l, r -> l > r }
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