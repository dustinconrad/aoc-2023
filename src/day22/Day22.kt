package day22

import Coord3
import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("22_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("1_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return 0
}

fun part2(input: List<String>): Int {
    return 0
}

data class Brick(val p1: Coord3, val p2: Coord3) {

    val x = p1.first .. p2.first
    val y = p1.second .. p2.second
    val z = p1.third .. p2.third

    val isOnGround = z.first == 1

}

class BrickPile(private var bricks: List<Brick>) {

    private val xStart = bricks.sortedBy { it.x.first }
    private val xEnd = bricks.sortedBy { it.x.last }
    private val yStart = bricks.sortedBy { it.y.first }
    private val yEnd = bricks.sortedBy { it.y.last }
    private val zEnd = bricks.sortedBy { it.z.last }

    fun settle(brick: Brick): Brick {
        return if (brick.isOnGround) {
            brick
        } else {
            // get xy overlaps
            val xo = overlapsXPlane(brick)
            val yo = overlapsYPlane(brick)

            val zs = zEndLt(brick)

            // retain only overlaps
            zs.retainAll(xo.intersect(yo))
            // last element is highest z
            val restsOn = zs.last()
            brick.copy(p1 = brick.p1.copy(third = restsOn.z.last + 1))
        }
    }

    private fun zEndLt(brick: Brick): LinkedHashSet<Brick> {
        // other bricks with an end z before this brick start z
        return bSearchLte(zEnd, brick.z.first - 1) { it.z.last }
    }

    private fun overlapsXPlane(brick: Brick): Set<Brick> {
        // other bricks with a start x lte this end x
        val startXCandidates = bSearchLte(xStart, brick.x.last) { it.x.first }
        // other bricks with an endX gte this start x
        val endXCandidates = bSearchGte(xEnd, brick.x.first) { it.x.last}
        return startXCandidates.intersect(endXCandidates)
    }

    private fun overlapsYPlane(brick: Brick): Set<Brick> {
        // other bricks with a start y lte this end y
        val startYCandidates = bSearchLte(yStart, brick.y.last) { it.y.first }
        // other bricks with an endX gte this start y
        val endYCandidates = bSearchGte(yEnd, brick.y.first) { it.y.last}
        return startYCandidates.intersect(endYCandidates)
    }

    private fun bSearch(sorted: List<Brick>, target: Int, selector: (Brick) -> Int): LinkedHashSet<Brick> {
        val foundLt = sorted.binarySearchBy(key = target - 0.5, selector = { brick: Brick -> selector.invoke(brick).toDouble() })
        val ipLt = (foundLt + 1) * -1
        val foundGt = sorted.binarySearchBy(key = target + 0.5, fromIndex = ipLt, selector = { brick: Brick -> selector.invoke(brick).toDouble() })
        val ipGt = (foundGt + 1) * -1
        val candidates = sorted.subList(ipLt, ipGt)
        return LinkedHashSet(candidates)
    }

    private fun bSearchLte(sorted: List<Brick>, target: Int, selector: (Brick) -> Int): LinkedHashSet<Brick> {
        val found = sorted.binarySearchBy(key = target + 0.5, selector = { brick: Brick -> selector.invoke(brick).toDouble() })
        val ip = (found + 1) * -1
        val candidates = sorted.subList(0, ip)
        return LinkedHashSet(candidates)
    }

    private fun bSearchGte(sorted: List<Brick>, target: Int, selector: (Brick) -> Int): LinkedHashSet<Brick> {
        val found = sorted.binarySearchBy(key = target - 0.5, selector = { brick: Brick -> selector.invoke(brick).toDouble() })
        val ip = (found + 1) * -1
        val candidates = sorted.subList(ip, sorted.size)
        return LinkedHashSet(candidates)
    }
}