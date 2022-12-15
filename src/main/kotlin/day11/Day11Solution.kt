package day11

fun main() {
    solution1().let(::println)
    solution2().let(::println)
}

private fun solution1(): Long = solution(20) { value, _ ->
    value / 3
}

private fun solution2(): Long = solution(10000) { value, divisor ->
    value % divisor
}

private fun solution(
    numberOfRounds: Int,
    worryRelief: (value: Long, divisor: Long) -> Long,
): Long {
    val monkeys = deserializedInput()
    val monkeysByNumber = monkeys.associateBy { it.number }

    repeat(numberOfRounds) {
        monkeys.forEach { monkey ->
            monkey.itemsByModuloValue.forEach { itemModuleValues ->
                val newModuloValuesForItem = itemModuleValues.mapValues { (key, value) ->
                    worryRelief(monkey.operation(value), key)
                }
                val nextMonkeyDecision = newModuloValuesForItem[monkey.testDivisor]!! % monkey.testDivisor == 0L
                val nextMonkeyNumber = monkey.nextMonkeyNumber(nextMonkeyDecision)
                monkeysByNumber[nextMonkeyNumber]?.itemsByModuloValue?.add(newModuloValuesForItem)
            }
            monkey.inspections += monkey.itemsByModuloValue.size.toLong()
            monkey.itemsByModuloValue.clear()
        }
    }

    val (best, secondBest) = monkeys.sortedBy { it.inspections }.takeLast(2)
    return best.inspections * secondBest.inspections
}

typealias ModuloValues = Map<Long, Long>

data class Monkey(
    val number: Long,
    val itemsByModuloValue: MutableList<ModuloValues>,
    val operation: (Long) -> Long,
    val testDivisor: Long,
    val nextMonkeyNumber: (Boolean) -> Long,
) {
    var inspections: Long = 0L
}

private fun deserializedInput(): List<Monkey> = input.split("\n\n")
    .map { monkeyData ->
        val lines = monkeyData.split("\n")
        val number = lines[0].substringAfter(" ").substringBefore(":").toLong()
        val itemsByModuloValues = lines[1].substringAfter(": ").split(", ").map { it.toLong() }
            .map { item -> primes.associateWith { item } }.toMutableList()
        val (leftOperand, operator, rightOperand) = lines[2].substringAfter("new = ").split(" ")
        val operation = when (operator) {
            "+" -> { a: Long, b: Long -> a + b }
            "*" -> { a: Long, b: Long -> a * b }
            else -> throw IllegalArgumentException("$operator is not a valid operation")
        }.let { binaryOperator ->
            when {
                leftOperand == "old" && rightOperand == "old" -> { a: Long -> binaryOperator(a, a) }
                leftOperand == "old" -> { a: Long -> binaryOperator(a, rightOperand.toLong()) }
                else -> { a: Long -> binaryOperator(a, leftOperand.toLong()) }
            }
        }
        val testDivisor = lines[3].substringAfterLast(" ").toLong()
        val trueMonkeyNumber = lines[4].substringAfterLast(" ").toLong()
        val falseMonkeyNumber = lines[5].substringAfterLast(" ").toLong()
        val nextMonkeyNumber = { test: Boolean ->
            if (test) trueMonkeyNumber else falseMonkeyNumber
        }

        Monkey(
            number,
            itemsByModuloValues,
            operation,
            testDivisor,
            nextMonkeyNumber
        )
    }

private val primes = listOf(2L, 3, 5, 7, 11, 13, 17, 19)

private val input = """
    Monkey 0:
      Starting items: 50, 70, 89, 75, 66, 66
      Operation: new = old * 5
      Test: divisible by 2
        If true: throw to monkey 2
        If false: throw to monkey 1

    Monkey 1:
      Starting items: 85
      Operation: new = old * old
      Test: divisible by 7
        If true: throw to monkey 3
        If false: throw to monkey 6

    Monkey 2:
      Starting items: 66, 51, 71, 76, 58, 55, 58, 60
      Operation: new = old + 1
      Test: divisible by 13
        If true: throw to monkey 1
        If false: throw to monkey 3

    Monkey 3:
      Starting items: 79, 52, 55, 51
      Operation: new = old + 6
      Test: divisible by 3
        If true: throw to monkey 6
        If false: throw to monkey 4

    Monkey 4:
      Starting items: 69, 92
      Operation: new = old * 17
      Test: divisible by 19
        If true: throw to monkey 7
        If false: throw to monkey 5

    Monkey 5:
      Starting items: 71, 76, 73, 98, 67, 79, 99
      Operation: new = old + 8
      Test: divisible by 5
        If true: throw to monkey 0
        If false: throw to monkey 2

    Monkey 6:
      Starting items: 82, 76, 69, 69, 57
      Operation: new = old + 7
      Test: divisible by 11
        If true: throw to monkey 7
        If false: throw to monkey 4

    Monkey 7:
      Starting items: 65, 79, 86
      Operation: new = old + 5
      Test: divisible by 17
        If true: throw to monkey 5
        If false: throw to monkey 0
""".trimIndent()
