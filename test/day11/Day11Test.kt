package day11

import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {

    @Test
    fun testPart1() {
        val input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()
            .lines()

        assertEquals(374, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
        """.trimIndent()
            .lines()

        assertEquals(281, part2(input))
    }

    @Test
    fun testPart2EdgeCase() {
        val input = """
            trknlxnv43zxlrqjtwonect
        """.trimIndent()
            .lines()

        assertEquals(41, part2(input))
    }
}