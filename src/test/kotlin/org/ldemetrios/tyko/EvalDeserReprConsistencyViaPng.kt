//package org.ldemetrios.tyko
//
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.shouldBe
//import org.ldemetrios.tyko.compiler.*
//import org.ldemetrios.tyko.ffi.TypstSharedLibrary
//import org.ldemetrios.tyko.model.deserialize
//import org.ldemetrios.tyko.model.repr
//import org.opentest4j.TestAbortedException
//import java.awt.image.BufferedImage
//import java.io.ByteArrayInputStream
//import java.io.File
//import javax.imageio.ImageIO
//
//
//class EvalDeserReprConsistencyViaPng : FreeSpec({
//    runCases(this, nonErroneousSuite, ::knownBugsGuard) {
//        testCase(it)
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
////   return !case.testName.contains("array-basic-syntax")
//}
//
//private fun testCase(case: Case) {
//    val (json, repr, pngsRef) = WorldBasedTypstCompiler(
//        TypstSharedLibrary.INSTANCE,
//        SingleFileWorld("#let test(a, b) = metadata((a, b))\n" + case.source)
//    ).use { compiler ->
//        val (json, evaluated) = try {
//            val result = compiler.evalDetachedRaw("let test(a, b) = metadata((a, b)); [\n" + case.source + "\n]")
//            when (result) {
//                is RResult.Ok -> result.value to deserialize(result.value)!!
//                is RResult.Err -> {
//                    throw TypstCompilerException(result.error)
//                }
//            }
//        } catch (e: TypstCompilerException) {
//            e.printStackTrace()
//            throw TestAbortedException()
//        }
//        Triple(json, evaluated.repr(), compiler.compilePng())
//    }
//    println(repr)
//    val pngsNew = WorldBasedTypstCompiler(
//        TypstSharedLibrary.INSTANCE,
//        SingleFileWorld("#$repr")
//    ).compilePng()
//    pngsRef.size shouldBe pngsNew.size
//
//    val inequal = mutableListOf<Int>()
//    for (i in pngsRef.indices) {
//        if (pngsRef[i].contentEquals(pngsNew[i])) continue
//        inequal.add(i)
//
//        val reference = pngsRef[i].toImage()
//        val newImage = pngsNew[i].toImage()
//
//        if (reference.width != newImage.width || reference.height != newImage.width) {
//            println("Reference: ${reference.width} x ${reference.height}; New: ${newImage.width} x ${newImage.height}")
//        }
//        val diff = diff(reference, newImage)
//
//        val output = "diffs/" + case.testName + "/" + i + ".png"
//        File(output).run {
//            if (!exists()) {
//                parentFile.mkdirs()
//            }
//            ImageIO.write(diff, "png", this)
//        }
//        File("diffs/" + case.testName + "/" + i + "-ref.png").writeBytes(pngsRef[i])
//        File("diffs/" + case.testName + "/" + i + "-new.png").writeBytes(pngsNew[i])
//    }
//
//    if (inequal.isNotEmpty()) {
//        File("diffs/" + case.testName + "/source.typ").writeText(case.source)
//        File("diffs/" + case.testName + "/queried.typ").writeText("#" + repr)
//        File("diffs/" + case.testName + "/queried.json").writeText(json)
//
//        throw AssertionError("Difference in pages $inequal")
//    }
//}
//
//fun ByteArray.toImage(): BufferedImage {
//    return ImageIO.read(ByteArrayInputStream(this))
//}
//
//fun diff(image1: BufferedImage, image2: BufferedImage): BufferedImage {
//    val width = minOf(image1.width, image2.width)
//    val height = minOf(image1.height, image2.height)
//    val diffImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
//
//    for (x in 0 until width) {
//        for (y in 0 until height) {
//            val rgb1 = image1.getRGB(x, y)
//            val rgb2 = image2.getRGB(x, y)
//            val r1 = rgb1 and 0xFF0000.toInt() shr (24 - 8)
//            val r2 = rgb2 and 0xFF0000.toInt() shr (24 - 8)
//            val g1 = rgb1 and 0xFF00 shr (16 - 8)
//            val g2 = rgb2 and 0xFF00 shr (16 - 8)
//            val b1 = rgb1 and 0xFF shr (8 - 8)
//            val b2 = rgb2 and 0xFF shr (8 - 8)
//
//            val r = (127.5 + (r1 - r2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
//            val g = (127.5 + (g1 - g2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
//            val b = (127.5 + (b1 - b2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
//
//            val rgb = (r shl 16) or (g shl 8) or (b)
//
//            diffImage.setRGB(x, y, rgb or (0xFF shl 24))
//        }
//    }
//    return diffImage
//}
