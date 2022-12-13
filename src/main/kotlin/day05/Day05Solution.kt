package day05

import java.util.Stack

fun main() {
    solution1(input).also(::println)
    solution2(input).also(::println)
}

private fun solution1(serializedInput: String): String {
    val (crateStacks, instructions) = deserializeInput(serializedInput)

    instructions.forEach { instruction ->
        repeat(instruction.move) {
            val toMove = crateStacks[instruction.from - 1].pop()
            crateStacks[instruction.to - 1].push(toMove)
        }
    }

    return crateStacks.joinToString("") { it.pop() }
}

/**
 * Same as above, but use an intermediate stack to preserve the order
 */
private fun solution2(serializedInput: String): String {
    val (crateStacks, instructions) = deserializeInput(serializedInput)

    instructions.forEach { instruction ->
        val intermediateStack = Stack<String>()
        repeat(instruction.move) {
            val toMove = crateStacks[instruction.from - 1].pop()
            intermediateStack.push(toMove)
        }
        repeat(instruction.move) {
            val toMove = intermediateStack.pop()
            crateStacks[instruction.to - 1].push(toMove)
        }
    }

    return crateStacks.joinToString("") { it.pop() }
}

private fun deserializeInput(serializedInput: String): Pair<List<Stack<String>>, List<Instruction>> {
    val (serializedCrates, serializedInstructions) = serializedInput.split("\n\n")
    val crateStacks = deserializeCrates(serializedCrates)
    val instructions = deserializeInstructions(serializedInstructions)
    return crateStacks to instructions
}

private fun deserializeInstructions(serializedInstructions: String) = serializedInstructions
    .replace("move ", "")
    .replace("from ", "")
    .replace("to ", "")
    .split("\n")
    .map {
        val (move, from, to) = it.split(" ").map(String::toInt)
        Instruction(move, from, to)
    }

data class Instruction(
    val move: Int,
    val from: Int,
    val to: Int,
)

private fun deserializeCrates(serializedCrates: String): List<Stack<String>> {
    val lines = serializedCrates.split("\n")
        .map { "$it " }
        .dropLast(1)
        .reversed()

    val stacks = (0 until (lines.first().length / 4)).map { Stack<String>() }

    lines.map { it.toCharArray() }
        .forEach { line ->
            stacks.forEachIndexed { index, stack ->
                val letter = line[(index * 4) + 1].toString()
                if (letter.isNotBlank()) stack.push(letter)
            }
        }

    return stacks
}

private val input = """
    [S]                 [T] [Q]        
    [L]             [B] [M] [P]     [T]
    [F]     [S]     [Z] [N] [S]     [R]
    [Z] [R] [N]     [R] [D] [F]     [V]
    [D] [Z] [H] [J] [W] [G] [W]     [G]
    [B] [M] [C] [F] [H] [Z] [N] [R] [L]
    [R] [B] [L] [C] [G] [J] [L] [Z] [C]
    [H] [T] [Z] [S] [P] [V] [G] [M] [M]
     1   2   3   4   5   6   7   8   9 

    move 6 from 1 to 7
    move 2 from 2 to 4
    move 2 from 7 to 4
    move 6 from 4 to 3
    move 1 from 5 to 1
    move 3 from 8 to 3
    move 15 from 3 to 4
    move 6 from 5 to 9
    move 14 from 4 to 2
    move 3 from 2 to 7
    move 1 from 2 to 7
    move 9 from 9 to 1
    move 3 from 2 to 1
    move 7 from 6 to 7
    move 1 from 6 to 8
    move 2 from 9 to 1
    move 9 from 2 to 3
    move 8 from 3 to 9
    move 1 from 1 to 4
    move 1 from 8 to 6
    move 1 from 6 to 2
    move 5 from 9 to 8
    move 2 from 9 to 1
    move 1 from 4 to 2
    move 17 from 1 to 9
    move 1 from 3 to 1
    move 3 from 2 to 3
    move 2 from 4 to 5
    move 12 from 7 to 3
    move 16 from 9 to 2
    move 5 from 7 to 5
    move 2 from 1 to 2
    move 1 from 3 to 6
    move 1 from 4 to 6
    move 1 from 7 to 3
    move 1 from 6 to 3
    move 7 from 3 to 4
    move 5 from 8 to 3
    move 1 from 6 to 7
    move 7 from 3 to 4
    move 6 from 3 to 1
    move 2 from 4 to 8
    move 1 from 5 to 2
    move 10 from 4 to 5
    move 3 from 5 to 2
    move 2 from 8 to 9
    move 5 from 2 to 8
    move 1 from 3 to 5
    move 2 from 5 to 8
    move 12 from 5 to 7
    move 1 from 4 to 2
    move 5 from 9 to 4
    move 1 from 2 to 5
    move 6 from 1 to 3
    move 6 from 3 to 5
    move 10 from 7 to 4
    move 2 from 7 to 3
    move 4 from 7 to 6
    move 1 from 9 to 5
    move 12 from 2 to 1
    move 1 from 8 to 7
    move 3 from 7 to 4
    move 4 from 4 to 8
    move 7 from 5 to 3
    move 1 from 2 to 4
    move 10 from 1 to 5
    move 2 from 1 to 2
    move 4 from 6 to 7
    move 8 from 8 to 3
    move 5 from 4 to 9
    move 12 from 3 to 8
    move 4 from 3 to 8
    move 2 from 9 to 2
    move 3 from 5 to 4
    move 1 from 3 to 5
    move 1 from 7 to 6
    move 14 from 4 to 6
    move 6 from 5 to 9
    move 8 from 2 to 8
    move 3 from 5 to 7
    move 21 from 8 to 4
    move 16 from 4 to 9
    move 8 from 6 to 2
    move 4 from 6 to 1
    move 1 from 4 to 6
    move 2 from 4 to 8
    move 3 from 1 to 8
    move 2 from 4 to 6
    move 1 from 6 to 2
    move 3 from 8 to 4
    move 2 from 2 to 5
    move 2 from 5 to 7
    move 1 from 8 to 9
    move 1 from 4 to 9
    move 1 from 1 to 6
    move 3 from 6 to 3
    move 3 from 2 to 3
    move 1 from 4 to 6
    move 3 from 6 to 7
    move 10 from 9 to 7
    move 1 from 4 to 7
    move 6 from 8 to 3
    move 1 from 6 to 8
    move 2 from 2 to 5
    move 1 from 2 to 1
    move 1 from 8 to 9
    move 1 from 2 to 8
    move 1 from 1 to 9
    move 7 from 9 to 1
    move 1 from 8 to 5
    move 7 from 1 to 7
    move 3 from 5 to 8
    move 3 from 7 to 2
    move 1 from 8 to 4
    move 1 from 2 to 4
    move 2 from 4 to 6
    move 5 from 3 to 1
    move 9 from 7 to 2
    move 6 from 3 to 8
    move 8 from 2 to 7
    move 2 from 6 to 4
    move 2 from 1 to 7
    move 2 from 1 to 4
    move 24 from 7 to 4
    move 4 from 8 to 9
    move 2 from 7 to 5
    move 1 from 5 to 2
    move 1 from 3 to 8
    move 4 from 2 to 8
    move 13 from 9 to 2
    move 2 from 8 to 6
    move 3 from 9 to 6
    move 26 from 4 to 2
    move 1 from 5 to 7
    move 2 from 6 to 2
    move 2 from 4 to 1
    move 7 from 2 to 1
    move 15 from 2 to 6
    move 8 from 2 to 8
    move 4 from 6 to 8
    move 9 from 2 to 9
    move 13 from 6 to 7
    move 6 from 1 to 9
    move 2 from 2 to 4
    move 4 from 1 to 6
    move 3 from 8 to 3
    move 1 from 4 to 9
    move 2 from 6 to 7
    move 1 from 4 to 3
    move 3 from 3 to 2
    move 14 from 7 to 4
    move 5 from 9 to 5
    move 9 from 8 to 5
    move 7 from 9 to 6
    move 2 from 5 to 6
    move 2 from 9 to 2
    move 10 from 5 to 1
    move 1 from 3 to 1
    move 2 from 8 to 1
    move 1 from 9 to 2
    move 1 from 7 to 5
    move 4 from 2 to 1
    move 1 from 9 to 8
    move 3 from 4 to 1
    move 1 from 8 to 6
    move 12 from 1 to 5
    move 1 from 1 to 6
    move 1 from 7 to 5
    move 4 from 6 to 9
    move 2 from 2 to 4
    move 1 from 9 to 6
    move 1 from 1 to 5
    move 2 from 9 to 7
    move 10 from 6 to 5
    move 1 from 6 to 7
    move 20 from 5 to 1
    move 1 from 7 to 9
    move 2 from 9 to 1
    move 3 from 5 to 1
    move 2 from 8 to 4
    move 2 from 8 to 7
    move 1 from 5 to 9
    move 1 from 8 to 4
    move 22 from 1 to 7
    move 5 from 4 to 8
    move 1 from 5 to 9
    move 19 from 7 to 4
    move 2 from 9 to 1
    move 1 from 5 to 9
    move 10 from 1 to 8
    move 1 from 9 to 1
    move 1 from 8 to 3
    move 8 from 4 to 7
    move 1 from 5 to 6
    move 3 from 4 to 5
    move 1 from 5 to 9
    move 11 from 7 to 4
    move 4 from 4 to 9
    move 1 from 6 to 2
    move 1 from 3 to 9
    move 5 from 9 to 4
    move 5 from 7 to 9
    move 23 from 4 to 2
    move 17 from 2 to 7
    move 2 from 2 to 8
    move 4 from 4 to 7
    move 1 from 4 to 5
    move 2 from 5 to 2
    move 5 from 8 to 9
    move 5 from 2 to 7
    move 9 from 7 to 5
    move 11 from 9 to 2
    move 1 from 4 to 3
    move 5 from 8 to 7
    move 3 from 8 to 5
    move 2 from 1 to 3
    move 2 from 3 to 9
    move 1 from 5 to 8
    move 5 from 7 to 5
    move 15 from 5 to 4
    move 2 from 8 to 1
    move 2 from 5 to 1
    move 4 from 4 to 1
    move 1 from 8 to 7
    move 8 from 2 to 1
    move 4 from 2 to 8
    move 2 from 7 to 4
    move 5 from 8 to 6
    move 5 from 7 to 9
    move 4 from 6 to 5
    move 7 from 4 to 8
    move 1 from 6 to 1
    move 1 from 3 to 1
    move 2 from 5 to 1
    move 7 from 1 to 5
    move 5 from 1 to 3
    move 4 from 7 to 9
    move 4 from 3 to 9
    move 2 from 9 to 7
    move 6 from 9 to 2
    move 1 from 4 to 1
    move 1 from 3 to 5
    move 1 from 2 to 5
    move 5 from 9 to 4
    move 4 from 4 to 6
    move 1 from 8 to 9
    move 8 from 4 to 3
    move 7 from 7 to 3
    move 5 from 1 to 3
    move 11 from 5 to 9
    move 1 from 7 to 6
    move 2 from 3 to 5
    move 1 from 3 to 1
    move 3 from 6 to 2
    move 2 from 5 to 1
    move 2 from 1 to 2
    move 3 from 1 to 5
    move 5 from 9 to 2
    move 2 from 6 to 8
    move 2 from 3 to 8
    move 4 from 9 to 7
    move 3 from 5 to 2
    move 2 from 1 to 8
    move 1 from 9 to 8
    move 1 from 9 to 2
    move 4 from 7 to 9
    move 11 from 8 to 7
    move 1 from 8 to 2
    move 6 from 9 to 7
    move 3 from 7 to 1
    move 13 from 2 to 7
    move 24 from 7 to 1
    move 2 from 2 to 6
    move 1 from 8 to 3
    move 1 from 9 to 3
    move 5 from 2 to 4
    move 1 from 2 to 5
    move 1 from 6 to 2
    move 1 from 6 to 3
    move 1 from 2 to 4
    move 3 from 7 to 3
    move 2 from 1 to 7
    move 2 from 3 to 8
    move 2 from 7 to 8
    move 9 from 3 to 2
    move 3 from 4 to 8
    move 1 from 5 to 1
    move 9 from 2 to 1
    move 3 from 4 to 9
    move 1 from 7 to 8
    move 6 from 3 to 9
    move 2 from 1 to 5
    move 15 from 1 to 3
    move 13 from 3 to 9
    move 11 from 1 to 4
    move 5 from 4 to 1
    move 6 from 3 to 6
    move 4 from 4 to 8
    move 6 from 1 to 4
    move 1 from 5 to 2
    move 1 from 2 to 1
    move 3 from 4 to 2
    move 2 from 8 to 5
    move 2 from 4 to 2
    move 9 from 9 to 3
    move 9 from 3 to 5
    move 2 from 9 to 4
    move 5 from 2 to 6
    move 1 from 1 to 8
    move 1 from 4 to 1
    move 10 from 9 to 2
    move 9 from 2 to 4
    move 10 from 4 to 1
    move 3 from 1 to 3
    move 4 from 1 to 2
    move 5 from 2 to 4
    move 2 from 5 to 2
    move 4 from 1 to 7
    move 10 from 5 to 4
    move 2 from 2 to 4
    move 1 from 9 to 2
    move 2 from 3 to 5
    move 1 from 3 to 5
    move 3 from 6 to 7
    move 8 from 4 to 9
    move 6 from 6 to 1
    move 4 from 9 to 5
    move 2 from 9 to 1
    move 1 from 2 to 6
    move 6 from 5 to 2
    move 3 from 7 to 9
    move 4 from 8 to 2
    move 1 from 7 to 9
    move 1 from 5 to 3
    move 2 from 7 to 4
    move 1 from 7 to 1
    move 14 from 1 to 9
    move 1 from 1 to 9
    move 1 from 3 to 8
    move 3 from 2 to 5
    move 2 from 4 to 2
    move 6 from 8 to 1
    move 1 from 2 to 1
    move 5 from 1 to 9
    move 1 from 1 to 7
    move 2 from 8 to 5
    move 1 from 5 to 4
    move 1 from 6 to 1
    move 8 from 2 to 7
    move 2 from 6 to 1
    move 9 from 9 to 5
    move 11 from 4 to 8
    move 4 from 7 to 4
    move 6 from 4 to 6
    move 1 from 7 to 4
    move 6 from 6 to 7
    move 1 from 5 to 9
    move 6 from 8 to 9
    move 8 from 9 to 5
    move 1 from 4 to 5
    move 15 from 9 to 3
    move 3 from 1 to 4
    move 6 from 7 to 2
    move 3 from 4 to 9
    move 2 from 7 to 3
    move 1 from 7 to 3
    move 1 from 7 to 2
    move 2 from 8 to 1
    move 3 from 8 to 5
    move 2 from 1 to 7
    move 8 from 3 to 6
    move 3 from 6 to 5
    move 1 from 6 to 1
    move 10 from 5 to 7
    move 6 from 5 to 4
    move 4 from 2 to 4
    move 6 from 5 to 1
    move 6 from 1 to 8
    move 2 from 9 to 2
    move 2 from 9 to 7
    move 6 from 3 to 7
    move 1 from 3 to 5
    move 1 from 1 to 9
    move 2 from 8 to 1
    move 2 from 5 to 4
    move 3 from 3 to 7
    move 10 from 4 to 6
    move 1 from 9 to 7
    move 12 from 7 to 3
    move 12 from 3 to 8
    move 2 from 1 to 5
    move 1 from 1 to 3
    move 13 from 8 to 1
    move 7 from 7 to 1
    move 13 from 6 to 9
    move 1 from 7 to 4
    move 6 from 5 to 3
    move 3 from 4 to 3
    move 6 from 3 to 1
    move 10 from 9 to 4
    move 2 from 7 to 6
    move 8 from 1 to 9
    move 3 from 2 to 9
    move 1 from 3 to 5
    move 1 from 3 to 5
    move 1 from 1 to 4
    move 6 from 9 to 3
    move 2 from 6 to 7
    move 4 from 9 to 5
    move 4 from 1 to 6
    move 1 from 2 to 4
    move 6 from 1 to 4
    move 3 from 9 to 3
    move 3 from 6 to 8
    move 3 from 8 to 7
    move 5 from 5 to 1
    move 1 from 3 to 9
    move 1 from 9 to 5
    move 1 from 3 to 2
    move 2 from 5 to 1
    move 1 from 6 to 9
    move 1 from 6 to 3
    move 2 from 9 to 7
    move 2 from 8 to 1
    move 1 from 3 to 2
    move 1 from 2 to 5
    move 1 from 7 to 1
    move 7 from 7 to 9
    move 12 from 1 to 9
    move 1 from 5 to 2
    move 1 from 7 to 1
    move 13 from 4 to 7
    move 1 from 9 to 4
    move 5 from 7 to 3
    move 4 from 9 to 1
    move 8 from 7 to 9
    move 3 from 2 to 3
    move 4 from 3 to 7
    move 5 from 4 to 6
    move 3 from 9 to 4
    move 10 from 1 to 5
    move 3 from 4 to 7
    move 16 from 9 to 2
    move 3 from 9 to 2
    move 6 from 5 to 3
    move 4 from 6 to 2
    move 1 from 4 to 6
    move 2 from 6 to 8
    move 1 from 5 to 2
    move 1 from 5 to 8
    move 7 from 7 to 2
    move 16 from 2 to 1
    move 1 from 5 to 1
    move 10 from 2 to 8
    move 14 from 8 to 5
    move 2 from 2 to 6
    move 1 from 2 to 5
    move 2 from 2 to 1
    move 8 from 1 to 7
    move 4 from 1 to 7
    move 2 from 1 to 7
    move 5 from 3 to 2
    move 1 from 1 to 6
    move 2 from 2 to 5
    move 4 from 1 to 7
    move 1 from 2 to 8
    move 1 from 2 to 8
    move 3 from 6 to 7
    move 10 from 7 to 5
    move 1 from 2 to 8
    move 27 from 5 to 9
    move 1 from 5 to 6
    move 1 from 6 to 4
    move 1 from 4 to 3
    move 3 from 3 to 7
    move 4 from 3 to 6
    move 2 from 6 to 4
    move 3 from 8 to 1
    move 2 from 6 to 1
    move 12 from 7 to 8
    move 2 from 3 to 9
    move 1 from 9 to 2
    move 1 from 2 to 8
    move 2 from 1 to 2
    move 6 from 3 to 8
    move 1 from 7 to 4
    move 15 from 9 to 5
    move 7 from 9 to 4
    move 1 from 2 to 1
    move 16 from 8 to 2
    move 8 from 5 to 2
    move 24 from 2 to 9
    move 3 from 1 to 2
    move 24 from 9 to 1
    move 5 from 5 to 9
    move 3 from 4 to 1
    move 1 from 7 to 6
    move 1 from 6 to 3
    move 1 from 3 to 2
    move 3 from 2 to 3
    move 1 from 5 to 6
    move 1 from 2 to 7
""".trimIndent()