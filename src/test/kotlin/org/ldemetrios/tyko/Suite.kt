package org.ldemetrios.tyko

import io.kotest.core.spec.style.FreeSpec
import org.ldemetrios.testing.runTestsGrouping
import org.opentest4j.TestAbortedException
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.isDirectory
import kotlin.io.path.readLines
import kotlin.io.path.toPath
import kotlin.io.path.walk
import kotlin.text.Regex

private object Here

fun <A, B, C> Sequence<Pair<A, B>>.mapSecond(f: (B) -> C): Sequence<Pair<A, C>> = map { it.first to f(it.second) }
fun <A, B, C : Any> Sequence<Pair<A, B>>.mapSecondNotNull(f: (B) -> C?): Sequence<Pair<A, C>> = mapNotNull { f(it.second)?.run { it.first to this } }
fun <A, B, C : Any> Sequence<Pair<A, B>>.flatMapSecond(f: (B) -> List<C>): Sequence<Pair<A, C>> = flatMap { (x, y) -> f(y).map { x to it } }

val TEST_SEPARATOR = Regex("--- [a-z0-9A-Z-]+ ---")
val LINE_SEPARATOR = Regex("\r\n|[\r\n\u2028\u2029\u0085]")

data class Case(val file: String, val name: String, val comment: String, val source: String) {
    val testName = "$file/$name"
}

@OptIn(ExperimentalPathApi::class)
val suite = run {
    val root = Here.javaClass.classLoader
        .getResource("suite")!!
        .toURI()
        .toPath()

    val files = root.walk()
        .filter { !it.isDirectory() }
        .map { it.toString().removePrefixOrThrow(root.toString()) to it.readLines().dropWhile { it.isBlank() } }
        .mapSecondNotNull {
            it.takeIf { !(it.firstOrNull()?.startsWith("// SKIP") ?: false) }
        }
        .flatMapSecond {
            it.split(keepSeparator = true) { it.matches(TEST_SEPARATOR) }.drop(1)
        }
        .map {
            val name = it.second[0].removePrefix("--- ").removeSuffix(" ---")
            val comment = it.second.drop(1).dropWhile { it.isBlank() }.takeWhile { it.startsWith("//") }.map { it.removePrefix("//").trim() }
            val text = it.second.drop(1 + comment.size)
            Case(it.first, name, comment.joinToString("\n"), text.joinToString("\n"))
        }
        .sortedBy { it.testName }
        .asSequence()

    files
}

val erroneous = suite.filter { it.isErroneous() }

private fun Case.isErroneous(): Boolean {
    return comment.lines().any { it.startsWith("Error:") } || source.lines().any { it.startsWith("// Error:") }
}

val nonErroneousSuite = suite.filter { !it.isErroneous() }

fun runCases(spec: FreeSpec?, cases: Sequence<Case>, skipper: (Case) -> Boolean, runner: (Case) -> Unit) {
    if (spec == null) {
        for ((idx, case) in cases.withIndex()) {
            if (skipper(case)) continue
            println("=============================================================\n".repeat(3))
            println("Test ${idx + 1}/${cases.count()}")
            println(case.testName)
            println(case.comment)
            println("=== === ===")
            println(case.source)
            try {
                if (skipper(case)) {
                    println("!! Skipped !!")
                    continue
                }
                runner(case)
            } catch (e: TestAbortedException) {
                println("!! ABORTED : ${e.message} !!")
            } catch (e: Throwable) {
                e.printStackTrace(System.out)
            }
        }
    } else with(spec) {
        runTestsGrouping<Case>(
            cases,
            { it.testName.split("/").dropWhile { it.isBlank() }.dropLastWhile { it.isBlank() }.filter { it.isNotBlank() } }
        ) {
            println(it.testName)
            try {
                if (skipper(it)) throw TestAbortedException()
                runner(it)
            } catch (e: Throwable) {
                println(it.comment)
                println("=== === ===")
                println(it.source)
                throw e
            }
        }
    }
}