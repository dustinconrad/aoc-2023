package day01

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    @Test
    fun testPart1() {
        val input = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent()
            .lines()

        assertEquals(142, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
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