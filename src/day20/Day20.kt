package day20

import readResourceAsBufferedReader
import kotlin.reflect.KClass


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("20_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("20_1.txt").readLines())}")
}

fun part1(input: List<String>): Long {
    val propagator = Propagator.create(input)
    repeat(1000) {
        propagator.pressButton()
    }
    return propagator.part1()
}

fun part2(input: List<String>): Int {
    val propagator = Propagator.create(input)
    var counter = 1
    while(!propagator.pressButton2{ !it.isHigh && it.dest == "rx" }) {
        counter++
    }
    return counter
}

const val BUTTON = "Button"
const val BROADCASTER = "broadcaster"

data class Signal(val isHigh: Boolean, val source: String, val dest: String)

sealed class Module(val label: String, val inputs: List<String>, val outputs: List<String>) {

    abstract fun execute(sig: Signal): List<Signal>

    class FlipFlop(label: String, inputs: List<String>, outputs: List<String>) : Module(label, inputs, outputs) {

        private var isOn = false

        override fun execute(sig: Signal): List<Signal> =
                if (!sig.isHigh) {
                    isOn = !isOn
                    outputs.map { out -> Signal(isOn, label, out) }
                } else {
                    emptyList()
                }
    }

    class ConjunctionModule(label: String, inputs: List<String>, outputs: List<String>) : Module(label, inputs, outputs) {

        val states = inputs.associateWithTo(mutableMapOf()) { false }

        override fun execute(sig: Signal): List<Signal> {
            states[sig.source] = sig.isHigh
            return outputs.map { out ->
                Signal(!states.values.all { it },
                        label,
                        out
                )
            }
        }
    }

    class BroadcastModule(label: String, inputs: List<String>, outputs: List<String>) : Module(label, inputs, outputs) {

        init {
            if (inputs.size != 1) {
                throw IllegalArgumentException("input must be of size 1")
            }
        }

        override fun execute(sig: Signal): List<Signal> = outputs.map {
            sig.copy(source = label, dest = it)
        }

    }

    data object OutputModule: Module("output", emptyList(), emptyList()) {
        override fun execute(sig: Signal): List<Signal> = emptyList()

    }

    class ButtonModule(outputs: List<String>) : Module(BUTTON, emptyList(), outputs) {

        init {
            if (outputs.size != 1) {
                throw IllegalArgumentException("output must be of size 1")
            }
        }

        override fun execute(sig: Signal): List<Signal> =
                outputs.map { Signal(false, label, it) }

    }

    companion object {

        val l = Regex("""(.+)\s+->\s+(.+)""")

        fun parse(line: String): List<ModuleSpec> {
            val match = l.matchEntire(line)
            val (_, labelString, outputString) = match?.groupValues ?: throw IllegalArgumentException("No match `$line`")
            val outputs = outputString.split(", ")
            return if (labelString == "broadcaster") {
                listOf(ModuleSpec(labelString, BroadcastModule::class, outputs),
                        ModuleSpec(BUTTON, ButtonModule::class, listOf(BROADCASTER)))
            } else {
                val symbol = labelString.first()
                val label = labelString.drop(1)
                val type = if (symbol == '%') {
                    FlipFlop::class
                } else {
                    ConjunctionModule::class
                }
                listOf(ModuleSpec(label, type, outputs))
            }
        }

    }
}

class Propagator(private val modules: Map<String, Module>) {

    private var signals = mutableListOf<Signal>()

    fun pressButton() {
        val q = ArrayDeque<Signal>()
        val button = modules[BUTTON]!!
        q.addAll(button.execute(Signal(true, "a", BUTTON)))
        while (q.isNotEmpty()) {
            val currSignal = q.removeFirst()
            signals.add(currSignal)
            val destModule = modules[currSignal.dest] ?: Module.OutputModule
            q.addAll(destModule.execute(currSignal))
        }
    }

    fun part1(): Long {
        val low = signals.count { !it.isHigh }
        val high = signals.count { it.isHigh }

        return low.toLong() * high
    }

    fun pressButton2(target: (Signal) -> Boolean): Boolean {
        val q = ArrayDeque<Signal>()
        val button = modules[BUTTON]!!
        q.addAll(button.execute(Signal(true, "a", BUTTON)))
        while (q.isNotEmpty()) {
            val currSignal = q.removeFirst()
            if (target.invoke(currSignal)) {
                return true
            } else {
                val destModule = modules[currSignal.dest] ?: Module.OutputModule
                q.addAll(destModule.execute(currSignal))
            }
        }
        return false
    }

    companion object {

        fun create(lines: List<String>): Propagator {
            val moduleSpecs = lines.flatMap { Module.parse(it) }.associateBy { it.label }

            val outputsToLabels = moduleSpecs.values.flatMap { m -> m.outputs.map { it to m.label } }
                    .groupBy { it.first }
                    .mapValues { it.value.map { it.second } }

            val modules = moduleSpecs.values.map {
                val inputs = outputsToLabels[it.label] ?: emptyList()
                when(it.type) {
                    Module.BroadcastModule::class -> Module.BroadcastModule(
                            it.label, inputs, it.outputs
                    )
                    Module.ButtonModule::class -> Module.ButtonModule(it.outputs)
                    Module.FlipFlop::class -> Module.FlipFlop(it.label, inputs, it.outputs)
                    Module.ConjunctionModule::class -> Module.ConjunctionModule(it.label, inputs, it.outputs)
                    else -> throw IllegalArgumentException("Unexpected spec: $it")
                }
            }

            val moduleMap = modules.map { it.label to it }.toMap()

            return Propagator(moduleMap)
        }

    }

}

data class ModuleSpec(val label: String, val type: KClass<out Module>, val outputs: List<String>)