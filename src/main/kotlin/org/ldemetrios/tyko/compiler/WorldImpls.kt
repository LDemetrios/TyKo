package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.ffi.NativeWorld
import org.ldemetrios.tyko.ffi.TyKoFFIEntity
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.model.*

@OptIn(TyKoFFIEntity::class)
class WorldBasedTypstCompiler(val owner: TypstSharedLibrary, world: World) : TypstCompiler {

    private val nativeDelegate = lazy { owner.NativeWorld(world) }
    private val native by nativeDelegate

    override fun evalDetachedRaw(source: String): RResult<String, List<SourceDiagnostic>> {
        return native.evalDetachedRaw(source)
    }

    override fun queryRaw(selector: String, format: SerialFormat): Warned<RResult<String, List<SourceDiagnostic>>> {
        return native.queryRaw(selector, format)
    }

    override fun compileHtmlRaw(): Warned<RResult<String, List<SourceDiagnostic>>> {
        return native.compileHtmlRaw()
    }

    override fun compileSvgRaw(fromPage: Int, toPage: Int): Warned<RResult<List<String>, List<SourceDiagnostic>>> {
        return native.compileSvgRaw(fromPage, toPage)
    }

    override fun compilePngRaw(fromPage: Int, toPage: Int, ppi: Float): Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>> {
        return native.compilePngRaw(fromPage, toPage, ppi)
    }

    override fun close() = if (nativeDelegate.isInitialized()) native.close() else Unit

    override fun reset() = if (nativeDelegate.isInitialized()) native.reset() else Unit
}

class SingleFileWorld(
    val source: String,
    val features: List<Feature> = listOf(),
    val inputs: TDictionary<TValue> = TEmptyDictionaryImpl,
    val time: WorldTime? = WorldTime.System,
) : World {
    override fun library(): StdlibProvider = object : StdlibProvider.Standard {
        override val inputs: TDictionary<TValue> get() = this@SingleFileWorld.inputs
        override val features: List<Feature> get() = this@SingleFileWorld.features
    }

    override fun mainFile(): FileDescriptor = FileDescriptor(null, "/main.typ")

    override fun file(file: FileDescriptor): RResult<ByteArray, FileError> {
        if (file.pack == null && file.path == "/main.typ") return RResult.Ok(source.encodeToByteArray())
        return RResult.Err(FileError.NotFound(file.path))
    }

    override fun now(): WorldTime? = time
}

fun DetachedWorld(features: List<Feature> = listOf()) = SingleFileWorld("", features, time = null)
//
//fun main() {
//    val world = DetachedWorld()
//
//    val compiler = WorldBasedTypstCompiler(TypstSharedLibrary.INSTANCE, world)
//
//    println(compiler.evalDetached("1 + 2").repr())
//
//    val another = WorldBasedTypstCompiler(
//        TypstSharedLibrary.INSTANCE,
//        SingleFileWorld("#metadata((1, 2))<x>")
//    )
//
//    println(another.query(TLabel("x".t)).repr())
//
//    val forHtml = WorldBasedTypstCompiler(
//        TypstSharedLibrary.INSTANCE,
//        SingleFileWorld(
//            """
//                This is _emphasized._ \
//                This is #emph[too.]
//
//                #show emph: it => {
//                  text(blue, it.body)
//                }
//
//                This is _emphasized_ differently.
//            """.trimIndent(),
//            features = listOf(Feature.Html)
//        )
//    )
//    val png = forHtml.compilePng(0, Int.MAX_VALUE).first()
//    File("test.png").run {
//        writeBytes(png)
//    }
//}

const val SHARED_LIBRARY_PATH = "/home/ldemetrios/Workspace/TypstNKotlinInterop/typst-custom-serialize/target/debug/libtypst_shared.so"

//fun main() {
//    fun compileWithColor(color: String) = WorldBasedTypstCompiler(
//        TypstSharedLibrary.INSTANCE,
//        SingleFileWorld(
//            """
//                #set page(width:auto, height: auto)
//                This is _emphasized. with $color _ \
//                This is #emph[too.]
//
//                #show emph: it => {
//                  text($color, it.body)
//                }
//
//                This is _emphasized_ differently.
//            """.trimIndent(),
//            features = listOf(Feature.Html)
//        )
//    ).compilePng().first()
//
//    val r = compileWithColor("red")
//    File("test-red.png").run { writeBytes(r) }
//
//    val b = compileWithColor("blue")
//    File("test-blue.png").run { writeBytes(b) }
//
//    val diff = generateDifferenceImage(byteArrayToImage(r), byteArrayToImage(b))
//    ImageIO.write(diff, "png", File("test-diff.png"))
//}
//
//fun byteArrayToImage(byteArray: ByteArray): BufferedImage {
//    return ImageIO.read(ByteArrayInputStream(byteArray))
//}
//
//fun generateDifferenceImage(image1: BufferedImage, image2: BufferedImage): BufferedImage {
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
//            val b1 = rgb1 and 0xFF shr( 8 - 8)
//            val b2 = rgb2 and 0xFF shr( 8 - 8)
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
