package day16

import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {

    @Test
    fun testPart1() {
        val input = """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
        """.trimIndent()
            .lines()

        assertEquals(46, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
        """.trimIndent()
            .lines()

        assertEquals(281, part2(input))
    }
}