package day15

import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {

    @Test
    fun testHash() {
        assertEquals(52, "HASH".hashalgo())
    }

    @Test
    fun testPart2() {
        val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        assertEquals(145, part2(listOf(input)))
    }
}