package day08

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {

    @Test
    fun testPart2() {
        val input = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent()
            .lines()

        assertEquals(6, part2(input))
    }
}