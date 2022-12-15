package day10

import kotlin.math.absoluteValue


fun main() {
    solution1().also(::println)
    solution2().also(::println)
}

private fun solution1(): Int {
    val registerValues = registerValues()
    return (20..220 step 40).sumOf { registerValues[it - 1] * it }
}

private fun solution2(): String {
    return registerValues().mapIndexed { index, registerValue ->
        if ((registerValue - (index % 40)).absoluteValue <= 1) "#" else "."
    }.chunked(40).joinToString("\n") { it.joinToString("") }
}

private fun registerValues(): List<Int> {
    var register = 1
    return deserializedInput().flatMap { instruction ->
        List(instruction.cycles) { register }.also {
            when (instruction) {
                is AddX -> register += instruction.value
                Noop -> {}
            }
        }
    }
}

sealed interface Instruction {
    val cycles: Int
}

object Noop : Instruction {
    override val cycles: Int = 1
}

data class AddX(val value: Int) : Instruction {
    override val cycles: Int = 2
}

private fun deserializedInput(): List<Instruction> = input.split("\n").map { instruction ->
    when {
        instruction == "noop" -> Noop
        instruction.startsWith("addx") -> instruction.split(" ")[1].toInt().let(::AddX)
        else -> throw IllegalArgumentException("$instruction is not a valid instruction.")
    }
}

private val input = """
    addx 1
    addx 4
    addx 1
    noop
    addx 4
    addx 3
    addx -2
    addx 5
    addx -1
    noop
    addx 3
    noop
    addx 7
    addx -1
    addx 1
    noop
    addx 6
    addx -1
    addx 5
    noop
    noop
    noop
    addx -37
    addx 7
    noop
    noop
    noop
    addx 5
    noop
    noop
    noop
    addx 9
    addx -8
    addx 2
    addx 5
    addx 2
    addx 5
    noop
    noop
    addx -2
    noop
    addx 3
    addx 2
    noop
    addx 3
    addx 2
    noop
    addx 3
    addx -36
    noop
    addx 26
    addx -21
    noop
    noop
    noop
    addx 3
    addx 5
    addx 2
    addx -4
    noop
    addx 9
    addx 5
    noop
    noop
    noop
    addx -6
    addx 7
    addx 2
    noop
    addx 3
    addx 2
    addx 5
    addx -39
    addx 34
    addx 5
    addx -35
    noop
    addx 26
    addx -21
    addx 5
    addx 2
    addx 2
    noop
    addx 3
    addx 12
    addx -7
    noop
    noop
    noop
    noop
    noop
    addx 5
    addx 2
    addx 3
    noop
    noop
    noop
    noop
    addx -37
    addx 21
    addx -14
    addx 16
    addx -11
    noop
    addx -2
    addx 3
    addx 2
    addx 5
    addx 2
    addx -15
    addx 6
    addx 12
    addx -2
    addx 9
    addx -6
    addx 7
    addx 2
    noop
    noop
    noop
    addx -33
    addx 1
    noop
    addx 2
    addx 13
    addx 15
    addx -21
    addx 21
    addx -15
    noop
    noop
    addx 4
    addx 1
    noop
    addx 4
    addx 8
    addx 6
    addx -11
    addx 5
    addx 2
    addx -35
    addx -1
    noop
    noop
""".trimIndent()