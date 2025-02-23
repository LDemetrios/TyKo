//package org.ldemetrios.tyko
//
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.FreeSpec
//import org.ldemetrios.tyko.compiler.TypstCompiler
//import org.ldemetrios.tyko.compiler.TypstCompilerException
//import org.ldemetrios.tyko.compiler.evalDetached
//
//class Errors : FreeSpec({
//    val errorneousCases = suite.filter { it.comment.startsWith("Error:") }
//
//    val compiler = TestCompiler()
//
//    runCases(this, errorneousCases, ::knownBugsGuard) {
//        testCase(it, compiler)
//    }
//})
//
//private fun knownBugsGuard(case: Case): Boolean {
//    return case.testName.trim('/') in listOf(
//        "math/attach.typ/issue-math-attach-realize-panic" // `#context oops` is not evaluated at the first try
//    )
//}
//
//context(FreeSpec)
//private fun testCase(case: Case, compiler: TypstCompiler) {
//    shouldThrow<TypstCompilerException> {
//        println(compiler.evalDetached(case.source))
//    }
//}
