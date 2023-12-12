package day12

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {

    @Test
    fun testPart1_1() {
        val input = "#.#.### 1,1,3"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(1, result.size)
    }

    @Test
    fun testPart1_2() {
        val input = "???.### 1,1,3"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(1, result.size)
    }

    @Test
    fun testPart1_3() {
        val input = ".??..??...?##. 1,1,3"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(4, result.size)
    }

    @Test
    fun testPart1_4() {
        val input = "?#?#?#?#?#?#?#? 1,3,1,6"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(1, result.size)
    }

    @Test
    fun testPart1_5() {
        val input = "????.#...#... 4,1,1"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(1, result.size)
    }

    @Test
    fun testPart1_6() {
        val input = "????.######..#####. 1,6,5"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(4, result.size)
    }

    @Test
    fun testPart1_7() {
        val input = "?###???????? 3,2,1"

        val row = SpringRow.parse(input)
        val result = row.combos()

        assertEquals(10, result.size)
    }

    @Test
    fun testPart1() {
        val input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()
            .lines()

        assertEquals(21, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
        """.trimIndent()
            .lines()

        assertEquals(1030, part2(input))
    }
}