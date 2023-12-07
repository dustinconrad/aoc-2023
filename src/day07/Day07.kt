package day07

import readResourceAsBufferedReader


fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("7_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("7_1.txt").readLines())}")
}

object Part1HandComparator : Comparator<CamelHand> {

    private val cardStrength = "AKQJT98765432".reversed().mapIndexed { idx, c -> c to idx}.toMap()
    override fun compare(o1: CamelHand, o2: CamelHand): Int {
        val typeCompare = type(o1) - type(o2)
        if (typeCompare != 0) {
            return typeCompare
        }
        return o1.hand.zip(o2.hand).map { (l, r) -> cardStrength[l]!! - cardStrength[r]!!}
            .first { it != 0 }
    }

    private fun type(hand: CamelHand): Int {
        val denoms = hand.hand.groupBy { it }.mapValues { it.value.size }
            .toList().sortedByDescending { it.second }

        // 5 of a kind
        if (denoms.size == 1) {
            return 7
        }
        if (denoms.size == 2) {
            if (denoms[0].second == 4) {
                return 6
            }
            if (denoms[0].second == 3) {
                return 5
            }
        }
        if (denoms.size == 3) {
            if (denoms[0].second == 3) {
                return 4
            }
            if (denoms[0].second == 2) {
                return 3
            }
        }
        if (denoms.size == 4) {
            return 2
        }
        return 1
    }
}

object Part1BidComparator : Comparator<CamelBid> {
    override fun compare(o1: CamelBid, o2: CamelBid): Int {
        return Part1HandComparator.compare(o1.hand, o2.hand)
    }
}

object Part2HandComparator : Comparator<CamelHand> {

    private val cardStrength = "AKQT98765432J".reversed().mapIndexed { idx, c -> c to idx}.toMap()
    override fun compare(o1: CamelHand, o2: CamelHand): Int {
        val typeCompare = type(o1) - type(o2)
        if (typeCompare != 0) {
            return typeCompare
        }
        return o1.hand.zip(o2.hand).map { (l, r) -> cardStrength[l]!! - cardStrength[r]!!}
            .first { it != 0 }
    }

    private fun type(hand: CamelHand): Int {
        val lookup = hand.hand.groupBy { it }.mapValues { it.value.size }
        val denoms = lookup
            .toList().sortedByDescending { it.second }

        // 5 of a kind
        if (denoms.size == 1) {
            return 7
        }
        if (denoms.size == 2) {
            // full house and four of a kind with a joker(s) is 5 of a kind
            if (lookup.containsKey('J')) {
                return 7
            }
            // 4 of a kind
            if (denoms[0].second == 4) {
                return 6
            }
            // full house
            if (denoms[0].second == 3) {
                return 5
            }
        }
        if (denoms.size == 3) {
            val jokers = lookup['J'] ?: 0
            // three of a kind
            if (denoms[0].second == 3) {
                if (jokers > 0) {
                    // four of a kind
                    return 6
                }
                return 4
            }
            // two pair
            if (denoms[0].second == 2) {
                if (jokers == 2) {
                    // four of a kind
                    return 6
                } else if (jokers == 1){
                    // full house
                    return 5
                }
                return 3
            }
        }
        // pair
        if (denoms.size == 4) {
            // pair with a joker is always 3 of a kind
            if (lookup.containsKey('J')) {
                return 4
            }
            return 2
        }
        // high card with joker is a pair
        if (lookup.containsKey('J')) {
            return 2
        }
        // high card
        return 1
    }
}

object Part2BidComparator : Comparator<CamelBid> {
    override fun compare(o1: CamelBid, o2: CamelBid): Int {
        return Part2HandComparator.compare(o1.hand, o2.hand)
    }
}

data class CamelBid(val hand: CamelHand, val bid: Long) {
    companion object {
        fun parse(line: String): CamelBid {
            val (hand, bid) = line.split(" ")
            return CamelBid(CamelHand(hand), bid.toLong())
        }
    }

}

data class CamelHand(val hand: String)

fun part1(input: List<String>): Long {
    val bids = input.map { CamelBid.parse(it) }
    val sorted = bids.sortedWith(Part1BidComparator)
    return sorted.mapIndexed { idx, bid -> (idx + 1) * bid.bid}
        .sum()
}

fun part2(input: List<String>): Long {
    val bids = input.map { CamelBid.parse(it) }
    val sorted = bids.sortedWith(Part2BidComparator)
    return sorted.mapIndexed { idx, bid -> (idx + 1) * bid.bid}
        .sum()
}