//package org.ldemetrios.tyko
//
//import io.kotest.assertions.fail
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.collections.shouldBeSingleton
//import org.ldemetrios.testing.runTestsGrouping
//import org.ldemetrios.tyko.compiler.*
//import org.ldemetrios.tyko.model.TContent
//import org.ldemetrios.tyko.model.TLabel
//import org.ldemetrios.tyko.model.TMetadata
//import org.ldemetrios.tyko.model.repr
//import org.ldemetrios.tyko.operations.t
//import java.io.File
//
//val TEST_SEPARATOR = Regex("--- [a-z0-9A-Z-]+ ---")
//
//data class Case(val file: File, val name: String, val text: String) {
//    val testName = file.relativeTo(SUITE_ROOT).path + "/" + name
//}
//
//val SUITE_ROOT = File("/home/ldemetrios/Workspace/TypstNKotlinInterop/TyKo/src/test/resources/suite")
//
//val typst = Typst(
//    "./typst012",
////    "./typst-custom-serial",
//    executable
//)
//
//class OriginalTests : FreeSpec({
//    beforeAny {
//        TypstDefaultCompiler.compiler = Typst(executable)
//    }
//
//    val cases = SUITE_ROOT
//        .walkTopDown()
//        .filter { it.isFile }
//        .flatMap { file ->
//            file
//                .readLines()
//                .split(keepSeparator = true) { it.matches(TEST_SEPARATOR) }
//                .filter { it.isNotEmpty() && it[0].startsWith("--- ") }
//                .map {
//                    val name = it[0].removePrefix("--- ").removeSuffix(" ---")
//                    Case(file, name, it.drop(1).joinToString("\n"))
//                }
//        }
//        .filter {
//            it.testName !in listOf(
//                // Probably needs more delicate serializing on the rust side
//                "visualize/image.typ/image-decode-svg",
//
//                // set text(stretch: ..) serializes number instead of ratio
//                "text/font.typ/text-font-properties",
//
//                // Label-related issues
//                "foundations/content.typ/content-field-materialized-query",
//                "foundations/content.typ/content-label-has-method",
//                "foundations/content.typ/content-label-field-access",
////                // `#set text(dir: rtl)` isn't serialized
////                "layout/grid/rtl.typ/grid-rtl-vline-position",
////                "layout/flow/place.typ/place-float-flow-size-alone",
////                "layout/flow/place.typ/place-float-flow-size",
////                "layout/flow/place.typ/place-float-fr",
////                "layout/flow/place.typ/place-float-rel-sizing",
//            )
//        }
////        .filter { case ->
////            failed.any { case.name.endsWith(it) }
////        }
//
//    runTestsGrouping(cases, { it.testName.split("/") }) {
//        knownBugsGuard(it)
//    }
//})
//
//const val SKIP_GPU_OVERLOAD = true
//const val SKIP_STATE_MANIPULATIONS = true
//const val SKIP_CONTEXT = true
//const val SKIP_FUNCTIONS = true
//const val SKIP_DYNAMIC_FUNCTIONS = true
//const val SKIP_LABELS_ISSUES = true
//
//fun knownBugsGuard(case: Case) {
//    if (case.testName in listOf(
//            "math/multiline.typ/math-pagebreaking-numbered",
//            "layout/flow/footnote.typ/footnote-self-ref",
//        ) || "gradient.typ" in case.testName
//    ) {
//        if (SKIP_GPU_OVERLOAD) {
//            return println("Skip: GPU OVERLOAD")
//        } else {
//            Thread.sleep(5000)
//        }
//    }
//
//    try {
//        testCase(case)
//    } /*catch (e: TypstSerialException) {
//        if (e.message?.endsWith("num-pos-params") == true) {
//            return println("Skip: CLOSURE")
//        } else throw e
//    }*/
//
//    /*catch (e: SerializationException) {
//        maybeSkip(e, SKIP_CONTEXT, "context") ?: return println("Skip: CONTEXT")
//        maybeSkip(e, SKIP_FUNCTIONS, "func") ?: return println("Skip: CONTEXT")
//        maybeSkip(e, SKIP_STATE_MANIPULATIONS, "state-update", "counter-update")
//            ?: return println("Skip: STATE MANIPULATIONS")
//        if (SKIP_DYNAMIC_FUNCTIONS &&
//            (e.message!!.startsWith("Non-string type descriptor: ")
//                    || e.message == "Dynamic functions aren't supported")
//        ) return println("Skip: DYNAMIC FUNCTIONS")
//
//        throw e
//    }*/ catch (e: org.ldemetrios.tyko.compiler.TypstCompilerException) {
//        if (
//            SKIP_LABELS_ISSUES && e.message!!.matches(Regex("label `.*` does not exist in the document"))
//        ) return println("Skip: LABELS ISSUES")
////        if (e.message == "unexpected argument: alignment" ||
////            e.message == "unexpected argument: italic" ||
////            e.message == "unexpected argument: variant"
////        ) return println("Skip: Improper set rules")
//        if (e.message == "unknown variable: test") return println("Skip: TEST MISSING")
//        throw e
//    }
//}
//
//val failed = setOf<String>(
//    "playground"
//)
//
//fun testCase(case: Case) {
//    println()
////    if (skip.any { case.name.endsWith(it) }) return
////    if (!case.name.endsWith("label-show-where-selector")) return
//    if (case.name.endsWith("transform-rotate")) return
//    println(case.text)
//    println("\n\n")
//
//    val result: TypstCompilerResult<Unit> = typst.compileAndRead(case.text).map { Unit }
//
//    if (!result.success) {
//        result.trace.print()
//        return
//    }
//
//    val meta = typst.query<TMetadata<TContent>>("#metadata[\n${case.text}\n] <lbl>", TLabel("lbl".t))
//    if (!meta.success) {
//        meta.trace.print()
//        fail("Compilation succeeded, but query #metadata[content] failed")
//    }
//
//    val content = meta
//        .orElseThrow()
//        .shouldBeSingleton()
//        .first()
//        .value
//
//    val repr = content.repr()
//
//    println("===============================================\n#$repr")
//
//    val secondaryResult: TypstCompilerResult<Unit> = typst.compileAndRead("#$repr").map { Unit }
//
//    if (!secondaryResult.success) {
//        throw org.ldemetrios.tyko.compiler.TypstCompilerException(secondaryResult.trace)
//    }
//}
