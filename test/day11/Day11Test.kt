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
    fun testPart2_1() {
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

        assertEquals(374, part2(input,2))
    }

    @Test
    fun testPart2() {
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
        val multiplier = 10L

        assertEquals(1030, part2(input, multiplier))
    }
}