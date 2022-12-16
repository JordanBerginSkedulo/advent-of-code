package day13

import java.lang.RuntimeException
import kotlin.math.min

/**
 * NB - heavily plagiarised off of an old talk I went to, source here:
 * https://github.com/neilgall/kotlin-parser-combinators
 *
 * Credit goes to Neil Gall.
 */

private val packetParserRef = ParserRef<Packet>()

private fun token(s: String): Parser<Unit> = string(s).between(::whitespace, ::whitespace)

private val packetNumber: Parser<Packet> = ::integer.map(Packet::Number)

private val packetArray: Parser<Packet> = (packetParserRef.get() sepBy token(","))
    .between(token("["), token("]"))
    .map(Packet::Array)

val packetParser = (packetArray or packetNumber).apply(packetParserRef::set)

sealed class Packet: Comparable<Packet> {
    override fun compareTo(other: Packet): Int {
        return when {
            this is Number && other is Number -> this.value - other.value
            this is Array && other is Array -> this.compareTo(other)
            this is Number -> this.toArray().compareTo(other)
            other is Number -> this.compareTo(other.toArray())
            else -> throw IllegalStateException("Should not be possible...")
        }
    }
    data class Number(val value: Int) : Packet()
    data class Array(val value: List<Packet>) : Packet() {
        fun compareTo(that: Array): Int {
            val max = min(this.value.size, that.value.size)
            val nonZeroComparison = (0 until max).asSequence().map {
                this.value[it].compareTo(that.value[it])
            }.firstOrNull { it != 0 }

            return nonZeroComparison ?: (this.value.size - that.value.size)
        }
    }
}

private fun Packet.Number.toArray(): Packet.Array = Packet.Array(listOf(this))

sealed class ParseResult<T> {
    data class Ok<T>(val value: T, val remaining: String) : ParseResult<T>()
    data class Err<T>(val expected: String, val actual: String) : ParseResult<T>()

    fun mapExpected(f: (String) -> String): ParseResult<T> = when (this) {
        is Ok -> this
        is Err -> Err(f(expected), actual)
    }

    fun getOrThrow(): T = when (this) {
        is Ok -> this.value
        is Err -> throw RuntimeException("Parse error\n\nExpected: $expected\n\nActual: $actual")
    }
}

typealias Parser<T> = (String) -> ParseResult<T>

fun string(s: String): Parser<Unit> = { input ->
    if (input.isNotEmpty() && input.substring(s.indices) == s)
        ParseResult.Ok(Unit, input.substring(s.length))
    else
        ParseResult.Err("'$s'", input)
}

fun integer(input: String): ParseResult<Int> {
    if (input.isEmpty() || !input[0].isDigit()) {
        return ParseResult.Err("an integer", input)
    } else {
        var value = 0
        for (i in input.indices) {
            if (input[i].isDigit()) {
                value = (value * 10) + (input[i] - '0')
            } else {
                return ParseResult.Ok(value, input.substring(i))
            }
        }
        return ParseResult.Ok(value, "")
    }
}

fun <T, U> ParseResult<T>.map(f: (T) -> U): ParseResult<U> = when (this) {
    is ParseResult.Err -> ParseResult.Err(expected, actual)
    is ParseResult.Ok -> ParseResult.Ok(f(value), remaining)
}

fun <T, U> ParseResult<T>.flatMap(f: (T, String) -> ParseResult<U>): ParseResult<U> = when (this) {
    is ParseResult.Err -> ParseResult.Err(expected, actual)
    is ParseResult.Ok -> f(value, remaining)
}

fun <T, U> Parser<T>.map(f: (T) -> U): Parser<U> = { input ->
    this@map(input).map(f)
}

infix fun <P1, P2> Parser<P1>.then(p2: Parser<P2>): Parser<Pair<P1, P2>> = { input ->
    this@then(input).flatMap { r1, rest1 ->
        p2(rest1).map { r2 -> Pair(r1, r2) }
    }
}

infix fun <P> Parser<P>.or(p2: Parser<P>): Parser<P> = { input ->
    when (val r1 = this@or(input)) {
        is ParseResult.Ok -> r1
        is ParseResult.Err -> p2(input).mapExpected { e2 -> "${r1.expected} or $e2" }
    }
}

infix fun <X, T> Parser<X>.before(p: Parser<T>): Parser<T> = (this@before then p).map { it.second }
infix fun <T, X> Parser<T>.followedBy(p: Parser<X>): Parser<T> = (this@followedBy then p).map { it.first }

fun <T, X, Y> Parser<T>.between(p1: Parser<X>, p2: Parser<Y>): Parser<T> =
    p1 before this followedBy p2

infix fun <T, X> Parser<T>.sepBy(sep: Parser<X>): Parser<List<T>> = { input ->
    val list = mutableListOf<T>()
    var remaining = input
    while (remaining.isNotEmpty()) {
        val r = this@sepBy(remaining)
        if (r is ParseResult.Ok) {
            list += r.value
            remaining = r.remaining
            val s = sep(remaining)
            if (s is ParseResult.Ok)
                remaining = s.remaining
            else
                break
        } else
            break
    }
    ParseResult.Ok(list, remaining)
}

fun whitespace(input: String): ParseResult<Unit> {
    for (i in input.indices) {
        if (!input[i].isWhitespace())
            return ParseResult.Ok(Unit, input.substring(i))
    }
    return ParseResult.Ok(Unit, input)
}

class ParserRef<T> {
    lateinit var p: Parser<T>
    fun set(p: Parser<T>) {
        this.p = p
    }

    fun get(): Parser<T> = { input -> p(input) }
}
