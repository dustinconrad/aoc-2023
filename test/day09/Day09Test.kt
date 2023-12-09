package day09

import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    @Test
    fun testPart2() {
        val input = """
            10 13 16 21 30 45
        """.trimIndent()
            .lines()

        assertEquals(5, part2(input))
    }
    
}