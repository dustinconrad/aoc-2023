package day17

import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun testPart1() {
        val input = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent()
            .lines()

        assertEquals(102, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
        """.trimIndent()
            .lines()

        assertEquals(281, part2(input))
    }
}