package org.ldemetrios.tyko.original

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import org.ldemetrios.js.JSParser
import org.ldemetrios.testing.runTestsGrouping
import org.ldemetrios.tyko.compiler.RResult.Err
import org.ldemetrios.tyko.compiler.RResult.Ok
import org.ldemetrios.tyko.compiler.TypstCompilerException
import org.ldemetrios.tyko.compiler.compilePng
import org.ldemetrios.tyko.compiler.compileSvg
import org.ldemetrios.tyko.compiler.format
import org.ldemetrios.tyko.model.TNativeFunc
import org.ldemetrios.tyko.model.deserialize
import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.model.t
import org.opentest4j.TestAbortedException
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.sqrt

const val heavyLogTest = false
const val runForDocumentation = false
const val DIST_CLEARANCE = 2.0

class Testing : FreeSpec({
    runCases(this, tests, false, ::skip)
})

fun main() { // Kotest framework doesn't work well with segfaults and panics
    runCases(null, tests, true, ::skip)
}


data class Case(val group: String, val name: String, val source: String, val action: () -> Unit)

val testFiles = localFiles.filterValues { !it.startsWith("// SKIP") }.toList().sortedBy { it.first }

val ERROR_MARKER = Regex("\\s*// Error:.*")
fun String.shouldCompile() = lines().none { it.matches(ERROR_MARKER) }
fun Case.shouldCompile() = source.shouldCompile()

val world = TestWorld("")

val compiler = TestCompiler(world)

val tests = mutableListOf<Case>().apply {
    addAll(
        testFiles.map { (path, source) ->
            Case("compiles", path, source) {
                world.currentMain = path

                if (source.shouldCompile()) {
                    compiler.compileSvg()
                } else {
                    shouldThrow<TypstCompilerException> {
                        compiler.compileSvg()
                    }
                }
            }
        }
    )

    addAll(
        testFiles.filter { it.second.shouldCompile() }.map { (path, source) ->
            Case("eval-recompile", path, source) {
                world.currentMain = path

                val pngReference = compiler.compilePng()

                val result = compiler.evalDetachedRaw("[\n$source\n]")

                val evaled = when (result) {
                    is Ok -> deserialize(result.value)!!
                    is Err -> {
                        if (result.error.any { it.message.contains("cannot access file system from here") }) {
                            throw TestAbortedException("Eval can't access file system")
                        }
                        throw AssertionError(
                            "Regular file compiled, but its source failed to eval",
                            TypstCompilerException(result.error)
                        )
                    }
                }
                val json = result.value

                val repr = evaled.repr()

                world.overriden = path to "#$repr"

                try {
                    val pngRecompiled = try {
                        compiler.compilePng()
                    } catch (e: Throwable) {
                        for (i in pngReference.indices) {
                            write("test-output/$path/d-${i.toString().padStart(4, '0')}_reference.png", pngReference[i])
                        }
                        throw e
                    }

                    if (pngReference.size != pngRecompiled.size) {
                        for (i in pngRecompiled.indices) {
                            write(
                                "test-output/$path/d-${i.toString().padStart(4, '0')}_recompiled.png",
                                pngRecompiled[i]
                            )
                        }
                        throw AssertionError("Reference: ${pngReference.size} pages, Recompiled: ${pngRecompiled.size} pages")
                    }

                    if (pngReference.indices.all { pngReference[it].contentEquals(pngRecompiled[it]) }) return@Case

                    val unequal = mutableListOf<Int>()

                    for (page in pngReference.indices) {
//                        if (pngReference[page].contentEquals(pngRecompiled[page])) continue

                        val reference = pngReference[page].toImage()
                        val newImage = pngRecompiled[page].toImage()

                        val (diff, maxDist) = diff(reference, newImage)

                        if (maxDist > DIST_CLEARANCE || runForDocumentation) {
                            unequal.add(page)
                        }

                        if (reference.width != newImage.width || reference.height != newImage.width) {
                            println("Reference: ${reference.width} x ${reference.height}; New: ${newImage.width} x ${newImage.height}")
                        }
                        write("test-output/$path/d-${page.toString().padStart(4, '0')}_diff.png", diff)
                        write(
                            "test-output/$path/d-${page.toString().padStart(4, '0')}_reference.png",
                            pngReference[page]
                        )
                        write(
                            "test-output/$path/d-${page.toString().padStart(4, '0')}_recompiled.png",
                            pngRecompiled[page]
                        )
                        println("Max distance on page $page: $maxDist")
                    }

                    if (unequal.isNotEmpty()) throw AssertionError("Pages $unequal differ (${unequal.size} of ${pngReference.size})")
                } catch (e: Throwable) {
                    write("test-output/$path/a_source.typ", source)
                    write("test-output/$path/b_queried.json", JSParser.parseValue(json).toString(4) + "\n")
                    write("test-output/$path/c_repred.typ", sharedLib.format("#$repr\n", 50, 2))
                    throw e
                }
            }
        }
    )
}

fun write(dest: String, image: BufferedImage) {
    val f = File(dest.replace("__part__", "/"))
    f.parentFile.mkdirs()
    ImageIO.write(image, "png", f)
}

fun write(dest: String, text: String) {
    val f = File(dest.replace("__part__", "/"))
    f.parentFile.mkdirs()
    f.writeText(text)
}

fun write(dest: String, text: ByteArray) {
    val f = File(dest.replace("__part__", "/"))
    f.parentFile.mkdirs()
    f.writeBytes(text)
}

fun skip(case: Case): Boolean {
    if (runForDocumentation) return false
    val skipGroup = when (case.group) {
        "compiles" -> listOf(
            // Strange behaviour of equality on plugins and modules
            "plugin-transition",
            "import-nested-item",

            // Missing font attached to a test world
            "math-attach-kerning",
            "math-attach-kerning-mixed",
            "math-equation-font",

            "import-cyclic-in-other-file", // Isn't marked erroneous

            "import-from-string-renamed-invalid" // Directory loaded?! TODO fix now
        )

        "eval-recompile" -> listOf(
            // Missing fonts
            "math-attach-kerning",
            "math-attach-kerning-mixed",
            "math-equation-font",

            // Strange behaviour of equality
            "plugin-transition",
            "ops-equality",
            "import-nested-item",

            // Text elem being stored as set text
            "page-marginal-style-shared-initial-interaction",
            "page-marginal-style-text-call",
            "page-marginal-style-text-call-around-set-page",
            "page-marginal-style-text-call-code",

            // Some query issues?
            "gradient-linear-sharp-repeat-and-mirror",
            "gradient-linear-sharp-and-repeat",
            "gradient-linear-repeat-and-mirror-3",

            // Not marked erroneous
            "import-cyclic-in-other-file",

            // Unsupported cramped style
            "math-size"
        )

        else -> listOf()
    }

    return skipGroup.any { case.name.endsWith(it) }
}


fun runCases(spec: FreeSpec?, cases: List<Case>, invert: Boolean, skipper: (Case) -> Boolean) {
    if (spec == null) {
        for ((idx, case) in cases.withIndex()) {
            if (invert xor skipper(case)) continue
            println("=============================================================\n".repeat(3))
            println("Test $idx/${cases.size}: ${case.group}/${case.name}")
            println(case.source)
            try {
                case.action()
            } catch (e: TestAbortedException) {
                println("!! ABORTED : ${e.message} !!")
            } catch (e: Throwable) {
                e.printStackTrace(System.out)
            }
        }
    } else with(spec) {
        runTestsGrouping<Case>(
            cases.asSequence(),
            {
                listOf(it.group) +
                        it.name
                            .replace("__part__", "/")
                            .split("/")
                            .dropWhile { it.isBlank() }.dropLastWhile { it.isBlank() }
                            .filter { it.isNotBlank() }
            }
        ) {
            val lines = it.source.lines()
            val padLen = lines.size.toString().length
            if (heavyLogTest) {
                println(
                    lines.withIndex().joinToString("\n") { (idx, it) ->
                        (idx + 1).toString().padStart(padLen, ' ') + ": " + it
                    }
                )
            }
            if (invert xor skipper(it)) throw TestAbortedException()
            it.action()
        }
    }
}

fun ByteArray.toImage(): BufferedImage {
    return ImageIO.read(ByteArrayInputStream(this))
}

fun diff(image1: BufferedImage, image2: BufferedImage): Pair<BufferedImage, Double> {
    val width = maxOf(image1.width, image2.width)
    val height = maxOf(image1.height, image2.height)
    val diffImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    var maxDist = .0

    for (x in 0 until width) {
        for (y in 0 until height) {
            val rgb1 = if (x < image1.width && y < image1.height) image1.getRGB(x, y) else 0x000000
            val rgb2 = if (x < image2.width && y < image2.height) image2.getRGB(x, y) else 0xFFFFFF
            val r1 = rgb1 and 0xFF0000.toInt() shr (24 - 8)
            val r2 = rgb2 and 0xFF0000.toInt() shr (24 - 8)
            val g1 = rgb1 and 0xFF00 shr (16 - 8)
            val g2 = rgb2 and 0xFF00 shr (16 - 8)
            val b1 = rgb1 and 0xFF shr (8 - 8)
            val b2 = rgb2 and 0xFF shr (8 - 8)

            val dist = sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2 + .0))

            maxDist = max(dist, maxDist)


            val r = (127.5 + (r1 - r2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
            val g = (127.5 + (g1 - g2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
            val b = (127.5 + (b1 - b2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)

            val rgb = (r shl 16) or (g shl 8) or (b)

            diffImage.setRGB(x, y, rgb or (0xFF shl 24))
        }
    }
    return diffImage to maxDist
}
