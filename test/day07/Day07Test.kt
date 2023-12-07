package day07

import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {

    @Test
    fun testPart1() {
        val input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent()
            .lines()

        assertEquals(6440, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
        """.trimIndent()
            .lines()

        assertEquals(281, part2(input))
    }

}