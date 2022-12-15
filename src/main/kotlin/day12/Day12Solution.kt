package day12

fun main() {
    solution1().also(::println)
    solution2().also(::println)
}

private fun solution1(): Int = solution { matrix ->
    matrix.flatten().filter { it.type == NodeType.Start }
}

private fun solution2(): Int = solution { matrix ->
    matrix.flatten().filter { it.z == 1 }
}

private fun solution(startingPointsSelector: (List<List<Node>>) -> List<Node>): Int {
    val matrix = deserializedInput()

    var pathHeads = startingPointsSelector(matrix)

    generateSequence(0) { it + 1 }
        .takeWhile { pathHeads.isNotEmpty() }
        .forEach { cost ->
            pathHeads.forEach { it.score = cost }
            pathHeads = pathHeads.flatMap { head ->
                val x = head.x
                val y = head.y

                listOfNotNull(
                    matrix.getOrNull(y)?.getOrNull(x + 1),
                    matrix.getOrNull(y)?.getOrNull(x - 1),
                    matrix.getOrNull(y + 1)?.getOrNull(x),
                    matrix.getOrNull(y - 1)?.getOrNull(x),
                ).filter { newHead ->
                    newHead.score == null && (head.z >= newHead.z - 1)
                }
            }.distinct()
        }

    return matrix.flatten().first { it.type == NodeType.End }.score!!
}

data class Node(
    val x: Int,
    val y: Int,
    val z: Int,
    val type: NodeType
) {
    var score: Int? = null
}

enum class NodeType {
    Start, Middle, End
}

private fun deserializedInput(): List<List<Node>> = input.split("\n")
    .mapIndexed { y, line ->
        line.mapIndexed { x, height ->
            val (z, type) = when (height) {
                'S' -> 1 to NodeType.Start
                'E' -> 26 to NodeType.End
                else -> (height.code - 96) to NodeType.Middle
            }
            Node(x, y, z, type)
        }
    }

private val input = """
    abcccccaaaaaacccaaaccaaaaaaaacccaaaaaaccccccccccccccccccccccccccccaaaaaaaaaaaaaacacccccccccccccccccccccccccccccccaaaaaaaacccccccccccccccccccccccccccccccccccccccccccccaaaaa
    abcccccaaaaaaaacaaaaccaaaaaaccccaaaaaaccccccccccaaacccccccccccccccaaaaaaaaaaaaaaaacccccccccccccccccccccccccccccccaaaaaaaaaccccccaaaccccccccccccccccccccccccccccccccccaaaaaa
    abccccaaaaaaaaacaaaaccaaaaaaccccaaaaaaaaccccccccaaaccccccccccccccccaaaaaaaaaaaaaaccccaaaccccccccccccccccccccccccccaaaaaaaaccccacaaaccccccccccccccccaaccccccccccccccccaaaaaa
    abcccaaaaaaaaaacaaaccaaaaaaaacccccaaccaaacaaccccaaaaaaaccccccccccccaacaaaaaaaaaccccccaaaaccccccccccccccccccccaaacaaaaaaccccccaaaaaaaacccccccccccccaaaacccccccccccccccaaacaa
    abcccaaacaaaccccccccaaaaaaaaaaccccccccaaaaaacaaaaaaaaaaccccccccccccccaaaaaaaaaaccccccaaaaccccccccccccccccaaccaaacaaaaaaacccccaaaaaaaacccccccccccccaaaaccaaaccccccccccccccaa
    abcccccccaaaccccccccaaaaaaaaaaccccccaaaaaaaccaaaaaaaaacccccccccccccccaaaacaaaaaaaacccaaaaccccccccccccccccaaaaaaacaaccaaacccccccaaaaacccccccccccccccaaaaaaaaacccccccccccccaa
    abcccccccaacccccccccacaaaaaccaccccccaaaaaaacccaaaaaaaccccccaaacccccccaacacaaaaaaaacccccccccccccccccccccccaaaaaaccccccaaaccccccaaaaaccccccccccccjjkkaaaaaaaacccccccccccccccc
    abaccccccccccccccccccccaaaacccccccccccaaaaaaccccaaaaaaccccaaaacccccccccccccaaaaaaccccccccccaccaaccccccccccaaaaaaaaccccccccccccaaaaaaccccccccccjjjkkkkkaaaaccccccccaaccccccc
    abaccccccccaaaccccccccccaaccccccccccccaacaaacccaaaaaaaccccaaaacccccccccccccaaaaaaccccccccccaaaaacccccccccaaaaaaaaaccccccccccccaccaaacccccccccjjjjkkkkkkkaacccccccccaccccccc
    abaacccccccaaaaccccccccccaaaccccccccccaacccccccaaaacaaccccaaaacccccccccccccaaaaaacccccccccccaaaaaccccccccaaaaaaaacccccccaaccccccccccccccccccjjjjoooookkkkkllllllccccaaacccc
    abaacccccccaaaaccccccccccaaaaccccccccccccccccccaacaaacaaaccccccccccaaccccccaaacaaccccccccccaaaaaacccccccaaaaaaaccccccccaaaacccccccccccccccccjjjoooooopkkkklllllllccccaacccc
    abaccccccccaaacccccccccccaaaacccccccccccccccccccccaaaaaaacccccccccaaaccccccccccccccccccccccaaaaccccccaaaccccaaaccccccccaaaacccccccccccccccccjjooooooopppklppplllllcccaccccc
    abacccaaaaaccccccccccccccaaaccccccccccccccccccccccaaaaaaccccccaaaaaaaccccccccccccccccccccccccaaacccccaaacacccaaccccccccaaaaccccccccccccccccjjjooouuuupppppppppplllcccaccccc
    abccccaaaaacccccccccccccccccccccccccccccccccccccaaaaaaaaccccccaaaaaaaaaaccaacccccccccccccccccccccccaacaaaaaccccccccccccccccaaacccccaccaccccjjjoouuuuuupppppppppllllccaccccc
    abcccaaaaaacccccccccccccaaccccccaaacccccccccccccaaaaaaaaaccccccaaaaaaaaacaaaaccccccccccccccccccccccaaaaaaaaccccccccccccacccaaccccccaaaacccjjjjoouuuuuuupuuuvvpqqlllccaccccc
    abcccaaaaaaccccccccccccaaacaacccaaaaacccccccccccaaaaaaaaaaccccccaaaaaaaccaaaacccccccccccccccccccccccaaaaaccccccccccccccaaaaaaacccccaaaaaccijjooouuuxxxuuuuvvvqqqlmccccccccc
    abcccaaaaaaccccaacccccccaaaaaccaaaaaccccccccccccaaaaaacaaacccccaaaaaaccccaaaacccccccccccccccccccaaaccaaaaaccccaaaccccccaaaaaaaacccaaaaaaciiiinootuxxxxuuyyyvvqqqmmccccccccc
    abcccacaacccccaaacaaccaaaaaacccaaaaaccccccccccccaaaaaacccccccccaaaaaaaccccccccccccaaccacccccccccaaacaaacaaccaaaaaccccccaaaaaaaaaccaaaaaaiiinnnnnttxxxxxyyyyvvqqqmmdddcccccc
    abcccaaacaaacccaaaaaccaaaaaaaaccaaaaacccccccccccaaacaaaccccccccaaccaaaccccccccccccaaaaaccccccaaaaaaaaaacccccaaaaaacccccaaaaaaaaaccccaaciiinnnnttttxxxxxyyyyvvqqmmmdddcccccc
    abcccaaaaaaacaaaaaacccaacaaaaaccaacccccccccccaaaaaaaaaaaccccccccccccaacccccccccccaaaaacccccccaaaaaaaacccccccaaaaaacccccaaaaaaaaccccccciiinnnnttttxxxxxyyyyvvqqqmmmdddcccccc
    SbcccaaaaaaccaaaaaaaaccccaacccccccccccaacccccaaaaaaaaaaccccccccccccccccccccccccccaaaaaaccccccccaaaaaccccccccaaaaacccccaaaaaaaccccccccciiinnntttxxxEzzzzyyvvvqqqmmmdddcccccc
    abccaaaaaaaacaacaaaaacccaaccccccccccccaaacccccaaaaaaaaaaaccccccccccccccccccccccccccaaaacccccccaaaaaaccccccccaaaaaccccccacaaaaccccccccciiinnntttxxxxxyyyyyyvvvqqmmmdddcccccc
    abcaaaaaaaaaacccaacccccaacccccccccacacaaaccccccaaaaaaaaaacccccccccccccccccccccacccaaccccccccccaaaaaaccccccccccccccccccccaaaaaccccccccciiinnntttxxxxyyyyyyyyvvvqqmmmdddccccc
    abcaaaaaaaaaaccaaccaaaaaacccccccccaaaaaaaaaacccaaaaaaaaaacaaccccccccccccaaaaaaaaccccccccccccccaccaaaccccccccccccccccaaacaaacccccccccaaiiinnnttttxxwwyyyyyyyvvvqqmmmdddccccc
    abaaccaaacaaaccccccaaaaaaaccccccccaaaaaaaaaacccaaaaaaaacccaaaccccccccccccaaaaaacccccccccccccccccccccccccccccccccccccaaaaaaaaaacccaaaachhhnnnntttsswwyywwwwwvvvrrqkmdddccccc
    abaaacaaacccccccccccaaaaaaacccccccccaaaaaacccccaacccaaacccaaaaaaaccccccccaaaaaaccccccccccccccaaccccccccccccccccccccccaaaaaaaaacccaaaaaahhhmmmmmsssswwywwwwwwvrrrrkkdddccccc
    abaaaaaaacccccccccccaaaaaaacccccccccaaaaaaccccccaaccccccaaaaaaaaccccccccaaaaaaaaccccccccaaccaaaccccccccccccccccaacccccaaaaaaacccccaaaaahhhhhmmmmssswwwwwrrrrrrrrkkkeeeccccc
    abcaaaaaaccccccccccaaaaaacccccccccccaaaaaacccccaaaaccccaaaaaaaaacccccccaaaaaaaaaacccccccaaacaaacccccccccccccacaaaccccaaaaaaccccccaaaaacchhhhhmmmmsswwwwrrrrrrrrrkkkeeeccccc
    abaaaaaacccccccccccaaaaaaccccccccccaaaaaaaaccccaaaaccccaaaaaaaaccccccccaaaaaaaaaacaaacccaaaaaaccccccaacccccaaaaaccaccaaaaaaacccccaccaacccchhhhmmmssswwsrrrrrrrkkkkkeeeccccc
    abaaaaaaaccccccccaaccccaaccccccccccaaaaaaacccccaaaacccccccaaaaaacccaaccacaaaaaccccaaaccccaaaaaaaaaacaaaccccaaaaaaaaccaaacaaaccccccccccccccchhhhmmssssssrlllkkkkkkkeeecccccc
    abaaaaaaaaccccccaaacaacccccccccccccaaaaaaaaccccccccccccccaaaaaaacccaacccccaaaaccccaaaaaaaaaaaaaaaaaaaacccccccaaaaaccccccccaaaacccccccccccccchhgmmmsssssllllkkkkkeeeeeaacccc
    abcaaacaaacccccccaaaaaccccccccccccaaaaaaaaaccccccccccccccaaaccaaaaaaaaaacccaaccaaaaaaaaaaaaaaaaacaaaaaaaccccaaaaacccccccaaaaaacccccccccccccccggmmmlssslllllffeeeeeeeaaacccc
    abcaaacccccccccaaaaaacccccccaaacaaacaaaaaaacccccccccccccaaaaccccaaaaaaaacccccccaaaaaaaaaaaaaaaccaaaaaaaaccccaacaacccccccaaaaaacccccccccccccccgggmmlllllllfffffeeeeaaaaacccc
    abcaaccccccccccaaaaaaaacccccaaaaaaaaaaaaaccccccccccccccccaaaacccccaaaacccccaacccaaaaaaaccccaaaccaaaaaaaacccccccaaccccccccaaaaaaaccccccccccccccggglllllllffffffecacaaacccccc
    abcccccccccaaccaacaaaaaccccccaaaaaaaaaaaaccccccaaccaaccaaaaaaccccaaaaaccccaaccccccaaaaaacccaaaccaacaaaccaaaacccccccccccccaaaaaaaccccccccccccccggggllllfffffcccccccaaacccccc
    abcccccccaaaaaaccaaacccccccccaaaaaaaaccaaacccccaaaaaaccaaaaacccccaaaaaacccaaaccaaaaaaaaacccccccccccaaaccaaaaacccccccccccaaaaaaaaaccaaccccccccccggggggfffffccccccccccccccccc
    abcaaccccaaaaaaccaaccccccccaaaaaaaaaacccaacccccaaaaaccccaaaaaccccacccaaccaaaaccaaaaaccaacccccccccccccccaaaaaacccccccaaacaaaaaaaaaaaaacccccccccccgggggfffaacccccccccccccaccc
    abaaaccccaaaaaaccccccccaaacaaaaaaaaaaaaaaaaaaccaaaaaacccaaccacccccccaaaaaaaaaaaaaaaccccccccccccccaaaaccaaaaaacccccccaaaaaaaaaacaaaaaaccccccccccccagggfcaaaccccccccccccaaaaa
    abaaaacccaaaaaccccccccaaaacaaacaaacccaaaaaaaacaaaaaaaaccccccccccccccaaaaaaaaaaaaaaacccccccccccccaaaaacccaaaaaccccccccaaaaaacaaaaaaaaccccccccccccccaccccaaacccccccccccccaaaa
    abaaaaccccaaaaccccccccaaaacccccaaacccccaaaacccaaaaaaaacccccccccccccccaaaaaaaaaaaaaacccccccccccccaaaaaaccaaaccccccccccaaaaaaaaaaaaaaaaccccccccccccccccccccacccccccccccccaaaa
    abaacccccccccccccccccccaaacccccaacccccaaaaaacccccaacccccccccccccccccccccaaaaaaaaacccccccccccccccaaaaaacccccccccccccaaaaaaaaaaaaaaaaaaacccccccccccccccccccccccccccccccaaaaaa
""".trimIndent()