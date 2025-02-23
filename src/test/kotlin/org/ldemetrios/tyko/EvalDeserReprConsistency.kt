//package org.ldemetrios.tyko
//
//import io.kotest.core.spec.style.FreeSpec
//import org.ldemetrios.tyko.compiler.DetachedWorld
//import org.ldemetrios.tyko.compiler.TypstCompiler
//import org.ldemetrios.tyko.compiler.TypstCompilerException
//import org.ldemetrios.tyko.compiler.WorldBasedTypstCompiler
//import org.ldemetrios.tyko.compiler.evalDetached
//import org.ldemetrios.tyko.ffi.TypstSharedLibrary
//import org.ldemetrios.tyko.model.repr
//import org.opentest4j.TestAbortedException
//
//
//class EvalDeserReprConsistency : FreeSpec({
//    val compiler = TestCompiler()
//
//    val templates = mapOf(
//        "content" to { it: String -> "[\n$it\n]" },
//        "with-test" to { it: String -> "let test(a, b) = metadata((a, b))\n[\n$it\n]" },
//    )
//
//    val tests = suite.flatMap { case ->
//        templates.map { (name, template) ->
//            Case(name + case.file,  case.name, case.comment, template(case.source))
//        }
//    }
//
//    runCases(this, tests, ::knownBugsGuard) {
//        testCase(it, compiler)
//    }
//})
//
//private fun knownBugsGuard(case: Case): Boolean {
//    val list = listOf(
//        "gradient-linear-repeat-and-mirror-3",
//        "gradient-linear-sharp-and-repeat",
//        "gradient-linear-sharp-repeat-and-mirror",
//    )
//
//    return list.any { case.testName.contains(it) }
//}
//
//private fun testCase(case: Case, compiler: TypstCompiler) {
//    val evaluated = try {
//        compiler.evalDetached((case.source))
//    } catch (e: TypstCompilerException) {
//        e.printStackTrace()
//        throw TestAbortedException()
//    }
//    val repr = evaluated.repr()
//    println(repr)
//    val onceAgain = compiler.evalDetached(repr)
//}
//
//fun main() {
////    val world = WorldBasedTypstCompiler(TypstSharedLibrary.INSTANCE, DetachedWorld()) TODO Check why it paniced
//    val world = TestCompiler()
//
//    val templates = mapOf(
//        "content" to { it: String -> "[\n$it\n]" },
////        "with-test" to { it: String -> "let test(a, b) = metadata((a, b))\n[\n$it\n]" },
//    )
//
//    val tests = suite.flatMap { case ->
//        templates.map { (name, template) ->
//            Case(case.file + "/" + case.name, name, case.comment, template(case.source))
//        }
//    }
//
//    runCases(null, tests, ::knownBugsGuard) {
//        testCase(it, world)
//    }
//}