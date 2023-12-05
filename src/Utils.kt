import java.io.BufferedReader
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

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