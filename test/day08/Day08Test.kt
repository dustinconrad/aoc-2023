package day08

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {

    @Test
    fun testPart1() {
        val input = """
        """.trimIndent()
            .lines()

        assertEquals(142, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """s
        """.trimIndent()
            .lines()

        assertEquals(281, part2(input))
    }
}