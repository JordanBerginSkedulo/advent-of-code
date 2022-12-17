package day15

import kotlin.math.absoluteValue


fun main() {
    solution1().also(::println)
    solution2().also(::println)
}

private fun solution1(): Int {
    val mapData = deserializeInput()
    val rowToCheck = 2000000L

    return mapData.xRange.map {
        it to rowToCheck
    }.count { (x, y) ->
        !mapData.ableToContainBeacon(x, y) && !mapData.containsBeacon(x, y)
    }
}

/**
 * I don't think this solution would work if there were more than 1 distress beacon, this relies on the fact
 * there's exactly one, which implies we only need to check the exclusive border of each blocking "diamond" formed
 * by each sensor (the distress beacon has to lie on this border). So we iterate over only those borders.
 */
private fun solution2(): Long {
    val mapData = deserializeInput()
    val upperBound = 4000000L
    val validXRange = 0L..upperBound
    val validYRange = 0L..upperBound

    return mapData.blockingBorderExclusive()
        .first { (x, y) ->
            (x in validXRange) && (y in validYRange) && !mapData.containsBeacon(x, y)
        }.let { (x, y) -> (x * upperBound) + y }

}

class MapData(
    private val sensors: List<Sensor>,
    private val beacons: List<Beacon>,
) {
    val xRange = sensors.minOf { it.x - it.blockingDistance } .. sensors.maxOf { it.x + it.blockingDistance }

    fun blockingBorderExclusive(): Sequence<Pair<Long, Long>> {
        return sensors.asSequence().flatMap {
            it.blockingBorderExclusive()
        }.filter { (x, y) ->
            ableToContainBeacon(x, y)
        }
    }

    fun ableToContainBeacon(x: Long, y: Long): Boolean = sensors.none { it.isWithinBlockingDistanceOf(x, y) }

    fun containsBeacon(x: Long, y: Long): Boolean = beacons.any { it.x == x && it.y == y }
}

data class Sensor(
    val x: Long,
    val y: Long,
    val blockingDistance: Long,
) {
    fun isWithinBlockingDistanceOf(otherX: Long, otherY: Long) =
        (x to y) manhattanDistance (otherX to otherY) <= blockingDistance

    fun blockingBorderExclusive(): Sequence<Pair<Long, Long>> {
        val topPoint = x to (y + blockingDistance + 1)
        val bottomPoint = x to (y - blockingDistance + 1)

        fun LongProgression.traverseBorderHalf(): Sequence<Pair<Long, Long>> = asSequence().flatMapIndexed { index, y ->
            listOf(
                (x + index) to y,
                (x - index) to y
            )
        }

        val topHalfBorder = (topPoint.second downTo y).traverseBorderHalf()
        val bottomHalfBorder = (bottomPoint.second..y).traverseBorderHalf()
        return topHalfBorder + bottomHalfBorder
    }
}

private infix fun Pair<Long, Long>.manhattanDistance(other: Pair<Long, Long>) =
    (other.first - first).absoluteValue + (other.second - second).absoluteValue

data class Beacon(
    val x: Long,
    val y: Long,
)

private fun deserializeInput(): MapData = input.replace("x=", "")
    .replace("y=", "")
    .split("\n")
    .associate {
        val (sensor, beacon) = it.split(":")
            .map {
                val (x, y) = it.substringAfter("at ").split(", ").map { it.toLong() }
                x to y
            }
        val blockingDistance = (sensor.first to sensor.second) manhattanDistance (beacon.first to beacon.second)

        Sensor(sensor.first, sensor.second, blockingDistance) to Beacon(beacon.first, beacon.second)
    }
    .let { MapData(it.keys.toList(), it.values.toList()) }

private val input = """
    Sensor at x=2983166, y=2813277: closest beacon is at x=3152133, y=2932891
    Sensor at x=2507490, y=122751: closest beacon is at x=1515109, y=970092
    Sensor at x=3273116, y=2510538: closest beacon is at x=3152133, y=2932891
    Sensor at x=1429671, y=995389: closest beacon is at x=1515109, y=970092
    Sensor at x=2465994, y=2260162: closest beacon is at x=2734551, y=2960647
    Sensor at x=2926899, y=3191882: closest beacon is at x=2734551, y=2960647
    Sensor at x=1022491, y=1021177: closest beacon is at x=1515109, y=970092
    Sensor at x=1353273, y=1130973: closest beacon is at x=1515109, y=970092
    Sensor at x=1565476, y=2081049: closest beacon is at x=1597979, y=2000000
    Sensor at x=1841125, y=1893566: closest beacon is at x=1597979, y=2000000
    Sensor at x=99988, y=71317: closest beacon is at x=86583, y=-1649857
    Sensor at x=3080600, y=3984582: closest beacon is at x=3175561, y=4138060
    Sensor at x=3942770, y=3002123: closest beacon is at x=3724687, y=3294321
    Sensor at x=1572920, y=2031447: closest beacon is at x=1597979, y=2000000
    Sensor at x=218329, y=1882777: closest beacon is at x=1597979, y=2000000
    Sensor at x=1401723, y=1460526: closest beacon is at x=1515109, y=970092
    Sensor at x=2114094, y=985978: closest beacon is at x=1515109, y=970092
    Sensor at x=3358586, y=3171857: closest beacon is at x=3152133, y=2932891
    Sensor at x=1226284, y=3662922: closest beacon is at x=2514367, y=3218259
    Sensor at x=3486366, y=3717867: closest beacon is at x=3724687, y=3294321
    Sensor at x=1271873, y=831354: closest beacon is at x=1515109, y=970092
    Sensor at x=3568311, y=1566400: closest beacon is at x=3152133, y=2932891
    Sensor at x=3831960, y=3146611: closest beacon is at x=3724687, y=3294321
    Sensor at x=2505534, y=3196726: closest beacon is at x=2514367, y=3218259
    Sensor at x=2736967, y=3632098: closest beacon is at x=2514367, y=3218259
    Sensor at x=3963402, y=3944423: closest beacon is at x=3724687, y=3294321
    Sensor at x=1483115, y=2119639: closest beacon is at x=1597979, y=2000000
""".trimIndent()