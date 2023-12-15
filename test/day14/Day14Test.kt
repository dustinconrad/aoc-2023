package day14

import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {

    @Test
    fun testPart2() {
        val input = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()
            .lines()
        assertEquals(64, part2(input))
    }
}