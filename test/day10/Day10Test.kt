package day10

import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {

    @Test
    fun testPart1() {
        val input = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent()
            .lines()

        assertEquals(4, part1(input))
    }
    @Test
    fun testPart1_1() {
        val input = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
        """.trimIndent()
            .lines()

        assertEquals(4, part1(input))
    }

    @Test
    fun testPart1_2() {
        val input = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
        """.trimIndent()
            .lines()

        assertEquals(8, part1(input))
    }


    @Test
    fun testPart2() {
        val input = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
        """.trimIndent()
            .lines()

        assertEquals(8, part2(input))
    }
}