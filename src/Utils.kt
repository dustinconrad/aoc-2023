import java.io.BufferedReader
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun readResourceAsBufferedReader(resourceName: String): BufferedReader {
    val classloader = Thread.currentThread().contextClassLoader
    val stream = classloader.getResourceAsStream(resourceName)

    return stream.bufferedReader()
}

fun List<String>.byEmptyLines(): List<String> {
    val result = mutableListOf<String>()
    val curr = mutableListOf<String>()
    for (line in this) {
        if (line.isBlank()) {
            result.add(curr.joinToString("\n"))
            curr.clear()
        } else {
            curr.add(line)
        }
    }
    if (curr.isNotEmpty()) {
        result.add(curr.joinToString("\n"))
    }
    return result
}

fun <T> Collection<Iterable<T>>.getCartesianProduct(): Set<List<T>> =
    if (isEmpty()) emptySet()
    else drop(1).fold(first().map(::listOf)) { acc, iterable ->
        acc.flatMap { list -> iterable.map(list::plus) }
    }.toSet()

fun IntRange.overlaps(other: IntRange): Boolean = !(this.first > other.last || this.last < other.first)

fun LongRange.overlaps(other: LongRange): Boolean = !(this.first > other.last || this.last < other.first)

fun LongRange.merge(other: LongRange): LongRange = if (this.overlaps(other)) {
    LongRange(min(this.first, other.first), max(this.last, other.last))
} else {
    throw IllegalStateException("$this does not overlap $other")
}

fun List<LongRange>.merge(): List<LongRange>  {
    if (this.size <=  1)  {
        return this
    }

    val result = mutableListOf<LongRange>()
    val workingCopy = this.sortedBy { it.first }.toMutableList()
    var curr = workingCopy.removeFirst()
    while (workingCopy.isNotEmpty()) {
        val next = workingCopy.removeFirst()
        if (curr.overlaps(next)) {
            curr = curr.merge(next)
        } else {
            result.add(curr)
            curr = next
        }
    }

    result.add(curr)

    return result
}

fun quadratic(a: Long, b: Long, c: Long): List<Double> {
    val r = sqrt((b * b - 4 * a * c).toDouble())
    val minus = (-b - r) / (2 * a)
    val plus = (-b + r) / (2 * a)

    return listOf(minus, plus).sorted()
}
data class ExtendedEuclidianResult(
    val gcd: Long,
    val s: Long,
    val t: Long
)

fun extendedEuclidean(a: Long, b: Long): ExtendedEuclidianResult {
    var ri = b
    var riSubOne = a
    var si = 0L
    var siSubOne = 1L
    var ti = 1L
    var tiSubOne = 0L

    while (ri != 0L) {
        val quotient = riSubOne / ri
        val riPlusOne = riSubOne - quotient * ri
        val siPlusOne = siSubOne - quotient * si
        val tiPlusOne = tiSubOne - quotient * ti

        riSubOne = ri
        siSubOne = si
        tiSubOne = ti

        ri = riPlusOne;
        si = siPlusOne;
        ti = tiPlusOne;
    }

    return ExtendedEuclidianResult(
        riSubOne,
        siSubOne,
        tiSubOne
    )
}

fun lcm(vararg numbers: Long): Long {
    val (a, b) = numbers
    val gcd = extendedEuclidean(a, b).gcd
    val result = (a * b) / gcd;
    return numbers.drop(2).fold(result) { acc, n -> lcm(acc, n) }
}

fun Pair<Int, Int>.addVec(vec: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + vec.first to this.second + vec.second
}

fun Pair<Int, Int>.subtract(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first - other.first to this.second - other.second
}