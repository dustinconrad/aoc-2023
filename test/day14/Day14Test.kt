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

        val dish = Dish(input)
        dish.cycle()
        assertEquals("""
            .....#....
            ....#...O#
            ...OO##...
            .OO#......
            .....OOO#.
            .O#...O#.#
            ....O#....
            ......OOOO
            #...O###..
            #..OO#....
        """.trimIndent(),
            dish.toString())
    }
}