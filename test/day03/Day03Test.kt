package day03

import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {

    @Test
    fun testPart1() {
        val input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent()
            .lines()

        assertEquals(4361, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent()
            .lines()

        assertEquals(467835, part2(input))
    }
    
}