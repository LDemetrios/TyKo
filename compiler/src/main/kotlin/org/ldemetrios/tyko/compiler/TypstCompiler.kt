package org.ldemetrios.tyko.compiler

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ldemetrios.tyko.driver.api.FlattenedSyntaxTree
import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.RawNow
import org.ldemetrios.tyko.driver.api.TypstCore
import java.io.Closeable
import java.util.Base64

@Serializable
enum class Feature {
    Html,
    A11yExtras
}
@JvmName("generic_sanitize")
private fun <T> Warned<T>.sanitize() = Warned(
    output,
    warnings.sanitize(),
)
private fun <T> Warned<RResult<T, List<SourceDiagnostic>>>.sanitize() = Warned(
    output.sanitize(),
    warnings.sanitize(),
)

private fun <T> RResult<T, List<SourceDiagnostic>>.sanitize() = mapError { it.sanitize() }
private fun List<SourceDiagnostic>.sanitize() = map { it.sanitize() }
private fun SourceDiagnostic.sanitize() =
    copy(hints = hints.filter { it != "set `RUST_BACKTRACE` to `1` or `full` to capture a backtrace" })

data class Library(internal val pointer: Pointer, internal val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()

    fun withInputs(
        inputs: String,
        fonts: FontCollection,
        closePrevious: Boolean
    ): RResult<Library, List<SourceDiagnostic>> {
        val list: RResult<Long, List<SourceDiagnostic>> =
            bridge.withInputs(pointer, fonts.pointer, inputs, closePrevious).deserialize()
        return list.sanitize().map { Library(pointer.another(it), bridge) }
    }

    fun withInputsOrThrow(inputs: String, fonts: FontCollection, closePrevious: Boolean): Library {
        return withInputs(inputs, fonts, closePrevious).orThrow()
    }

    fun withTestDefinitions(closePrevious: Boolean): Library {
        return Library(bridge.withTestDefinitions(pointer, closePrevious), bridge)
    }

    fun withStyles(
        styled: String,
        fonts: FontCollection,
        closePrevious: Boolean,
        append: Boolean,
    ): RResult<Library, List<SourceDiagnostic>> {
        val list: RResult<Long, List<SourceDiagnostic>> =
            bridge.withStyles(pointer, fonts.pointer, styled, closePrevious, append).deserialize()
        return list.sanitize().map { Library(pointer.another(it), bridge) }
    }

    fun withStylesOrThrow(
        styled: String,
        fonts: FontCollection,
        closePrevious: Boolean,
        append: Boolean,
    ): Library {
        return withStyles(styled, fonts, closePrevious, append).orThrow()
    }

}

data class FSContext(val pointer: Pointer, val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()

    fun resetFile(file: FileDescriptor) {
        bridge.resetFile(pointer, file.serialize())
    }
}

data class FontCollection(val pointer: Pointer, val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()
}

data class PagedDocument(val pointer: Pointer, val carriedContext: FSContext, val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()

    fun renderPng(from: Int = 0, to: Int = Int.MAX_VALUE, ppi: Double = 144.0) =
        bridge.renderPng(pointer, from, to, ppi)
            .deserialize<List<Base64Bytes>>().map { it.bytes }

    fun renderSvg(from: Int = 0, to: Int = Int.MAX_VALUE) = bridge.renderSvg(pointer, from, to)
        .deserialize<List<String>>()

    fun renderPdfRaw(options: PdfOptionsConfig? = null) =
        bridge.renderPdf(pointer, carriedContext.pointer, options?.serialize() ?: "")
            .deserialize<RResult<Base64Bytes, List<SourceDiagnostic>>>()
            .sanitize()
            .map { it.bytes }

    fun renderPdf(options: PdfOptionsConfig? = null) = renderPdfRaw(options).orThrow()

    fun enforceTitle(title: String) = bridge.enforceTitle(pointer, title)
}

@Serializable
data class PackageSpec(val namespace: String, val name: String, val version: PackageVersion)

@Serializable
data class PackageVersion(val major: Int, val minor: Int, val patch: Int)

@Serializable
@JsonClassDiscriminator("type")
sealed class Now {
    @Serializable
    @SerialName("System")
    data object System : Now()

    @Serializable
    @SerialName("Fixed")
    data class Fixed(val millis: Long, val nanos: Int) : Now()
}

@Serializable
data class Warned<T>(val output: T, val warnings: List<SourceDiagnostic>)

@Serializable
data class SourceDiagnostic(
    val severity: Severity,
    val span: Span,
    val message: String,
    val trace: List<Spanned<Tracepoint>>,
    val hints: List<String>
)

@Serializable
enum class Severity {
    Error,
    Warning
}

@Serializable
data class Span(
    val native: ULong,
    val file: FileDescriptor?,
    @SerialName("start_ind") val startInd: Long,
    @SerialName("end_ind") val endInd: Long,
    @SerialName("start_line") val startLine: Long,
    @SerialName("start_col") val startCol: Long,
    @SerialName("end_line") val endLine: Long,
    @SerialName("end_col") val endCol: Long,
)

@Serializable
data class FileDescriptor(
    @SerialName("pack")
    val packageSpec: PackageSpec?,
    @SerialName("path")
    val virtualPath: String
)

@Serializable
data class Spanned<T>(
    @SerialName("v")
    val value: T,
    val span: Span
)

@Serializable
@JsonClassDiscriminator("type")
sealed class Tracepoint {
    @Serializable
    @SerialName("Call")
    data class Call(val function: String?) : Tracepoint()

    @Serializable
    @SerialName("Show")
    data class Show(val string: String) : Tracepoint()

    @Serializable
    @SerialName("Import")
    data object Import : Tracepoint()
}

@Serializable(with = RResultSerializer::class)
sealed class RResult<out T, out E> {
    @Serializable
    data class Ok<out T>(val value: T) : RResult<T, Nothing>()

    @Serializable
    data class Err<out E>(val error: E) : RResult<Nothing, E>()

    inline fun <R> map(transform: (T) -> R): RResult<R, E> = when (this) {
        is Ok -> Ok(transform(value))
        is Err -> this
    }

    inline fun <F> mapError(transform: (E) -> F): RResult<T, F> = when (this) {
        is Ok -> this
        is Err -> Err(transform(error))
    }

    inline fun <R> flatMap(transform: (T) -> RResult<R, @UnsafeVariance E>): RResult<R, E> = when (this) {
        is Ok -> transform(value)
        is Err -> this
    }
}

@Serializable
enum class SyntaxMode {
    Markup, Code, Math
}

@Serializable(with = Base64BytesSerializer::class)
data class Base64Bytes(val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean =
        this === other || other is Base64Bytes && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = bytes.contentHashCode()
}

class RResultSerializer<T, E>(
    private val tSerializer: KSerializer<T>,
    private val eSerializer: KSerializer<E>
) : KSerializer<RResult<T, E>> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("RResult") {
            element("Ok", tSerializer.descriptor, isOptional = true)
            element("Err", eSerializer.descriptor, isOptional = true)
        }

    override fun serialize(encoder: Encoder, value: RResult<T, E>) {
        val jsonEncoder = encoder as? kotlinx.serialization.json.JsonEncoder
            ?: error("Only JsonEncoder is supported")
        val jsonElement = when (value) {
            is RResult.Ok -> kotlinx.serialization.json.JsonObject(
                mapOf("Ok" to jsonEncoder.json.encodeToJsonElement(tSerializer, value.value))
            )

            is RResult.Err -> kotlinx.serialization.json.JsonObject(
                mapOf("Err" to jsonEncoder.json.encodeToJsonElement(eSerializer, value.error))
            )
        }
        jsonEncoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): RResult<T, E> {
        val jsonDecoder = decoder as? kotlinx.serialization.json.JsonDecoder
            ?: error("Only JsonDecoder is supported")
        val jsonElement = jsonDecoder.decodeJsonElement()

        require(jsonElement is kotlinx.serialization.json.JsonObject) { "Expected JSON object" }

        return when {
            "Ok" in jsonElement -> RResult.Ok(
                jsonDecoder.json.decodeFromJsonElement(tSerializer, jsonElement["Ok"]!!)
            )

            "Err" in jsonElement -> RResult.Err(
                jsonDecoder.json.decodeFromJsonElement(eSerializer, jsonElement["Err"]!!)
            )

            else -> error("Invalid RResult format")
        }
    }
}

object Base64BytesSerializer : KSerializer<Base64Bytes> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Base64Bytes", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Base64Bytes) {
        val encoded = Base64.getEncoder().encodeToString(value.bytes)
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): Base64Bytes {
        val decoded = Base64.getDecoder().decode(decoder.decodeString())
        return Base64Bytes(decoded)
    }
}

private fun Now?.toRawNow() = when (this) {
    null -> RawNow(-1, 0)
    is Now.System -> RawNow(-2, 0)
    is Now.Fixed -> RawNow(millis, nanos)
}

private inline fun <reified T> T.serialize(): String = Json.encodeToString<T>(this)

private inline fun <reified T> String.deserialize(): T = Json.decodeFromString<T>(this)

private fun <T> RResult<T, List<SourceDiagnostic>>.orThrow(): T = when (this) {
    is RResult.Ok -> value
    is RResult.Err -> throw TypstCompilerException(error)
}

private fun <T> Warned<RResult<T, List<SourceDiagnostic>>>.orThrowIgnoringWarnings(): T = output.orThrow()

private fun <T> Warned<RResult<T, List<SourceDiagnostic>>>.orThrowWithWarnings(): Warned<T> =
    Warned(output.orThrow(), warnings)

private fun FlattenedSyntaxTree.decodeErrors(): List<String> {
    if (errorStarts.isEmpty()) return emptyList()
    val messages = ArrayList<String>(errorStarts.size)
    for (i in errorStarts.indices) {
        val start = errorStarts[i]
        val end = if (i + 1 < errorStarts.size) errorStarts[i + 1] else errors.size
        if (start < 0 || end < start || start > errors.size) {
            messages.add("")
            continue
        }
        val safeEnd = end.coerceAtMost(errors.size)
        val slice = errors.copyOfRange(start, safeEnd)
        messages.add(String(slice, Charsets.UTF_8))
    }
    return messages
}

private fun FlattenedSyntaxTree.toSyntaxTree(): SyntaxTree {
    val errorsMessages = decodeErrors()
    val marks = marks.map { encoded ->
        val code = (encoded ushr 32).toInt()
        val index = encoded.toInt()
        val mark = when (code) {
             0 -> SyntaxMark.NodeStart(SyntaxKind.End)
             1 -> SyntaxMark.NodeStart(SyntaxKind.Error)
             2 -> SyntaxMark.NodeStart(SyntaxKind.Shebang)
             3 -> SyntaxMark.NodeStart(SyntaxKind.LineComment)
             4 -> SyntaxMark.NodeStart(SyntaxKind.BlockComment)
             5 -> SyntaxMark.NodeStart(SyntaxKind.Markup)
             6 -> SyntaxMark.NodeStart(SyntaxKind.Text)
             7 -> SyntaxMark.NodeStart(SyntaxKind.Space)
             8 -> SyntaxMark.NodeStart(SyntaxKind.Linebreak)
             9 -> SyntaxMark.NodeStart(SyntaxKind.Parbreak)
             10 -> SyntaxMark.NodeStart(SyntaxKind.Escape)
             11 -> SyntaxMark.NodeStart(SyntaxKind.Shorthand)
             12 -> SyntaxMark.NodeStart(SyntaxKind.SmartQuote)
             13 -> SyntaxMark.NodeStart(SyntaxKind.Strong)
             14 -> SyntaxMark.NodeStart(SyntaxKind.Emph)
             15 -> SyntaxMark.NodeStart(SyntaxKind.Raw)
             16 -> SyntaxMark.NodeStart(SyntaxKind.RawLang)
             17 -> SyntaxMark.NodeStart(SyntaxKind.RawDelim)
             18 -> SyntaxMark.NodeStart(SyntaxKind.RawTrimmed)
             19 -> SyntaxMark.NodeStart(SyntaxKind.Link)
             20 -> SyntaxMark.NodeStart(SyntaxKind.Label)
             21 -> SyntaxMark.NodeStart(SyntaxKind.Ref)
             22 -> SyntaxMark.NodeStart(SyntaxKind.RefMarker)
             23 -> SyntaxMark.NodeStart(SyntaxKind.Heading)
             24 -> SyntaxMark.NodeStart(SyntaxKind.HeadingMarker)
             25 -> SyntaxMark.NodeStart(SyntaxKind.ListItem)
             26 -> SyntaxMark.NodeStart(SyntaxKind.ListMarker)
             27 -> SyntaxMark.NodeStart(SyntaxKind.EnumItem)
             28 -> SyntaxMark.NodeStart(SyntaxKind.EnumMarker)
             29 -> SyntaxMark.NodeStart(SyntaxKind.TermItem)
             30 -> SyntaxMark.NodeStart(SyntaxKind.TermMarker)
             31 -> SyntaxMark.NodeStart(SyntaxKind.Equation)
             32 -> SyntaxMark.NodeStart(SyntaxKind.Math)
             33 -> SyntaxMark.NodeStart(SyntaxKind.MathText)
             34 -> SyntaxMark.NodeStart(SyntaxKind.MathIdent)
             35 -> SyntaxMark.NodeStart(SyntaxKind.MathShorthand)
             36 -> SyntaxMark.NodeStart(SyntaxKind.MathAlignPoint)
             37 -> SyntaxMark.NodeStart(SyntaxKind.MathDelimited)
             38 -> SyntaxMark.NodeStart(SyntaxKind.MathAttach)
             39 -> SyntaxMark.NodeStart(SyntaxKind.MathPrimes)
             40 -> SyntaxMark.NodeStart(SyntaxKind.MathFrac)
             41 -> SyntaxMark.NodeStart(SyntaxKind.MathRoot)
             42 -> SyntaxMark.NodeStart(SyntaxKind.Hash)
             43 -> SyntaxMark.NodeStart(SyntaxKind.LeftBrace)
             44 -> SyntaxMark.NodeStart(SyntaxKind.RightBrace)
             45 -> SyntaxMark.NodeStart(SyntaxKind.LeftBracket)
             46 -> SyntaxMark.NodeStart(SyntaxKind.RightBracket)
             47 -> SyntaxMark.NodeStart(SyntaxKind.LeftParen)
             48 -> SyntaxMark.NodeStart(SyntaxKind.RightParen)
             49 -> SyntaxMark.NodeStart(SyntaxKind.Comma)
             50 -> SyntaxMark.NodeStart(SyntaxKind.Semicolon)
             51 -> SyntaxMark.NodeStart(SyntaxKind.Colon)
             52 -> SyntaxMark.NodeStart(SyntaxKind.Star)
             53 -> SyntaxMark.NodeStart(SyntaxKind.Underscore)
             54 -> SyntaxMark.NodeStart(SyntaxKind.Dollar)
             55 -> SyntaxMark.NodeStart(SyntaxKind.Plus)
             56 -> SyntaxMark.NodeStart(SyntaxKind.Minus)
             57 -> SyntaxMark.NodeStart(SyntaxKind.Slash)
             58 -> SyntaxMark.NodeStart(SyntaxKind.Hat)
             60 -> SyntaxMark.NodeStart(SyntaxKind.Dot)
             61 -> SyntaxMark.NodeStart(SyntaxKind.Eq)
             62 -> SyntaxMark.NodeStart(SyntaxKind.EqEq)
             63 -> SyntaxMark.NodeStart(SyntaxKind.ExclEq)
             64 -> SyntaxMark.NodeStart(SyntaxKind.Lt)
             65 -> SyntaxMark.NodeStart(SyntaxKind.LtEq)
             66 -> SyntaxMark.NodeStart(SyntaxKind.Gt)
             67 -> SyntaxMark.NodeStart(SyntaxKind.GtEq)
             68 -> SyntaxMark.NodeStart(SyntaxKind.PlusEq)
             69 -> SyntaxMark.NodeStart(SyntaxKind.HyphEq)
             70 -> SyntaxMark.NodeStart(SyntaxKind.StarEq)
             71 -> SyntaxMark.NodeStart(SyntaxKind.SlashEq)
             72 -> SyntaxMark.NodeStart(SyntaxKind.Dots)
             73 -> SyntaxMark.NodeStart(SyntaxKind.Arrow)
             74 -> SyntaxMark.NodeStart(SyntaxKind.Root)
             75 -> SyntaxMark.NodeStart(SyntaxKind.Bang)
             76 -> SyntaxMark.NodeStart(SyntaxKind.Not)
             77 -> SyntaxMark.NodeStart(SyntaxKind.And)
             78 -> SyntaxMark.NodeStart(SyntaxKind.Or)
             79 -> SyntaxMark.NodeStart(SyntaxKind.None)
             80 -> SyntaxMark.NodeStart(SyntaxKind.Auto)
             81 -> SyntaxMark.NodeStart(SyntaxKind.Let)
             82 -> SyntaxMark.NodeStart(SyntaxKind.Set)
             83 -> SyntaxMark.NodeStart(SyntaxKind.Show)
             84 -> SyntaxMark.NodeStart(SyntaxKind.Context)
             85 -> SyntaxMark.NodeStart(SyntaxKind.If)
             86 -> SyntaxMark.NodeStart(SyntaxKind.Else)
             87 -> SyntaxMark.NodeStart(SyntaxKind.For)
             88 -> SyntaxMark.NodeStart(SyntaxKind.In)
             89 -> SyntaxMark.NodeStart(SyntaxKind.While)
             90 -> SyntaxMark.NodeStart(SyntaxKind.Break)
             91 -> SyntaxMark.NodeStart(SyntaxKind.Continue)
             92 -> SyntaxMark.NodeStart(SyntaxKind.Return)
             93 -> SyntaxMark.NodeStart(SyntaxKind.Import)
             94 -> SyntaxMark.NodeStart(SyntaxKind.Include)
             95 -> SyntaxMark.NodeStart(SyntaxKind.As)
             96 -> SyntaxMark.NodeStart(SyntaxKind.Code)
             97 -> SyntaxMark.NodeStart(SyntaxKind.Ident)
             98 -> SyntaxMark.NodeStart(SyntaxKind.Bool)
             99 -> SyntaxMark.NodeStart(SyntaxKind.Int)
             100 -> SyntaxMark.NodeStart(SyntaxKind.Float)
             101 -> SyntaxMark.NodeStart(SyntaxKind.Numeric)
             102 -> SyntaxMark.NodeStart(SyntaxKind.Str)
             103 -> SyntaxMark.NodeStart(SyntaxKind.CodeBlock)
             104 -> SyntaxMark.NodeStart(SyntaxKind.ContentBlock)
             105 -> SyntaxMark.NodeStart(SyntaxKind.Parenthesized)
             106 -> SyntaxMark.NodeStart(SyntaxKind.Array)
             107 -> SyntaxMark.NodeStart(SyntaxKind.Dict)
             108 -> SyntaxMark.NodeStart(SyntaxKind.Named)
             109 -> SyntaxMark.NodeStart(SyntaxKind.Keyed)
             110 -> SyntaxMark.NodeStart(SyntaxKind.Unary)
             111 -> SyntaxMark.NodeStart(SyntaxKind.Binary)
             112 -> SyntaxMark.NodeStart(SyntaxKind.FieldAccess)
             113 -> SyntaxMark.NodeStart(SyntaxKind.FuncCall)
             114 -> SyntaxMark.NodeStart(SyntaxKind.Args)
             115 -> SyntaxMark.NodeStart(SyntaxKind.Spread)
             116 -> SyntaxMark.NodeStart(SyntaxKind.Closure)
             117 -> SyntaxMark.NodeStart(SyntaxKind.Params)
             118 -> SyntaxMark.NodeStart(SyntaxKind.LetBinding)
             119 -> SyntaxMark.NodeStart(SyntaxKind.SetRule)
             120 -> SyntaxMark.NodeStart(SyntaxKind.ShowRule)
             121 -> SyntaxMark.NodeStart(SyntaxKind.Contextual)
             122 -> SyntaxMark.NodeStart(SyntaxKind.Conditional)
             123 -> SyntaxMark.NodeStart(SyntaxKind.WhileLoop)
             124 -> SyntaxMark.NodeStart(SyntaxKind.ForLoop)
             125 -> SyntaxMark.NodeStart(SyntaxKind.ModuleImport)
             126 -> SyntaxMark.NodeStart(SyntaxKind.ImportItems)
             127 -> SyntaxMark.NodeStart(SyntaxKind.ImportItemPath)
             128 -> SyntaxMark.NodeStart(SyntaxKind.RenamedImportItem)
             129 -> SyntaxMark.NodeStart(SyntaxKind.ModuleInclude)
             130 -> SyntaxMark.NodeStart(SyntaxKind.LoopBreak)
             131 -> SyntaxMark.NodeStart(SyntaxKind.LoopContinue)
             132 -> SyntaxMark.NodeStart(SyntaxKind.FuncReturn)
             133 -> SyntaxMark.NodeStart(SyntaxKind.Destructuring)
             134 -> SyntaxMark.NodeStart(SyntaxKind.DestructAssignment)
            135 -> SyntaxMark.NodeEnd
            else -> SyntaxMark.Error(errorsMessages[code - 136])
        }
        IndexedMark(mark, index)
    }
    return SyntaxTree(marks)
}

class TypstCompiler(val bridge: TypstCore) {
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return bridge.formatSource(content, column, tabWidth)
    }

    fun libraryProvider(features: Set<Feature>, inputs: String): Library {
        val features = features.map { 1 shl it.ordinal }.fold(0, Int::or)
        val pointer = bridge.library(features, inputs)
        return Library(pointer, bridge)
    }

    fun queryRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.query(context.pointer, fonts.pointer, stdlib.pointer, main.serialize(), now.toRawNow(), selector)
            .deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    fun query(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): String {
        return queryRaw(context, fonts, stdlib, main, now, selector).orThrowIgnoringWarnings()
    }

    fun queryWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): Warned<String> {
        return queryRaw(context, fonts, stdlib, main, now, selector).orThrowWithWarnings().sanitize()
    }

    fun parseSyntax(string: String, mode: SyntaxMode): SyntaxTree {
        return bridge.parseSyntax(string, mode.ordinal).toSyntaxTree()
    }

    fun detachedEvalRaw(
        library: Library,
        fonts: FontCollection,
        source: String,
        mode: SyntaxMode,
        context: FSContext? = null,
    ): RResult<String, List<SourceDiagnostic>> {
        return bridge.detachedEval(
            library.pointer,
            fonts.pointer,
            source,
            mode.ordinal,
            context?.pointer,
        ).deserialize<RResult<String, List<SourceDiagnostic>>>().sanitize()
    }

    fun detachedEvalWarnedRaw(
        library: Library,
        fonts: FontCollection,
        source: String,
        mode: SyntaxMode,
        context: FSContext? = null,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.detachedEvalWarned(
            library.pointer,
            fonts.pointer,
            source,
            mode.ordinal,
            context?.pointer,
        ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    fun evalMainWarnedRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.evalMainWarned(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    fun detachedEval(
        library: Library,
        fonts: FontCollection,
        source: String,
        mode: SyntaxMode,
        context: FSContext? = null,
    ): String {
        return detachedEvalRaw(library, fonts, source, mode, context).orThrow()
    }

    fun precompilePagedRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<PagedDocument, List<SourceDiagnostic>>> {
        val x: Warned<RResult<Long, List<SourceDiagnostic>>> = bridge.precompilePaged(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize()
        return x.sanitize().map {
            it.map { ptr ->
                PagedDocument(
                    Pointer(ptr, bridge::freePagedDocument),
                    context,
                    bridge,
                )
            }
        }
    }

    fun precompilePaged(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): PagedDocument {
        return precompilePagedRaw(context, fonts, stdlib, main, now).orThrowIgnoringWarnings()
    }

    fun precompilePagedWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<PagedDocument> {
        return precompilePagedRaw(context, fonts, stdlib, main, now).orThrowWithWarnings()
    }

    fun compileHtmlRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.compileHtml(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    fun compileHtml(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): String {
        return compileHtmlRaw(context, fonts, stdlib, main, now).orThrowIgnoringWarnings()
    }

    fun compileHtmlWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<String> {
        return compileHtmlRaw(context, fonts, stdlib, main, now).orThrowWithWarnings()
    }

    fun compileSvgRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
    ): Warned<RResult<List<String>, List<SourceDiagnostic>>> {
        return bridge.compileSvg(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
            from,
            to,
        ).deserialize<Warned<RResult<List<String>, List<SourceDiagnostic>>>>().sanitize()
    }

    fun compileSvg(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
    ): List<String> {
        return compileSvgRaw(context, fonts, stdlib, main, now, from, to).orThrowIgnoringWarnings()
    }

    fun compileSvgWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
    ): Warned<List<String>> {
        return compileSvgRaw(context, fonts, stdlib, main, now, from, to).orThrowWithWarnings()
    }

    fun compilePngRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        ppi: Double,
    ): Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>> {
        return bridge.compilePng(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
            from,
            to,
            ppi,
        )
            .deserialize<Warned<RResult<List<Base64Bytes>, List<SourceDiagnostic>>>>()
            .sanitize()
            .map { it.map { it.map { it.bytes } } }
    }

    fun compileMergedWithLinksRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        ppi: Double,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return bridge.compilePngMergedWithLinks(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
            ppi,
        )
            .deserialize<Warned<RResult<Base64Bytes, List<SourceDiagnostic>>>>()
            .sanitize()
            .map { it.map { it.bytes } }
    }

    fun compileMergedWithLinks(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        ppi: Double,
    ): ByteArray {
        return compileMergedWithLinksRaw(context, fonts, stdlib, main, now, ppi).orThrowIgnoringWarnings()
    }

    fun compileMergedWithLinksWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        ppi: Double,
    ): Warned<ByteArray> {
        return compileMergedWithLinksRaw(context, fonts, stdlib, main, now, ppi).orThrowWithWarnings()
    }

    fun compilePdfRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        options: PdfOptionsConfig? = null,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return bridge.compilePdf(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
            options?.serialize() ?: "",
        )
            .deserialize<Warned<RResult<Base64Bytes, List<SourceDiagnostic>>>>()
            .sanitize()
            .map { it.map { it.bytes } }
    }

    fun compilePdf(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        options: PdfOptionsConfig? = null,
    ): ByteArray {
        return compilePdfRaw(context, fonts, stdlib, main, now, from, to, options).orThrowIgnoringWarnings()
    }

    fun compilePdfWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        options: PdfOptionsConfig? = null,
    ): Warned<ByteArray> {
        return compilePdfRaw(context, fonts, stdlib, main, now, from, to, options).orThrowWithWarnings()
    }

    fun compilePng(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        ppi: Double,
    ): List<ByteArray> {
        return compilePngRaw(context, fonts, stdlib, main, now, from, to, ppi).orThrowIgnoringWarnings()
    }

    fun compilePngWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        ppi: Double,
    ): Warned<List<ByteArray>> {
        return compilePngRaw(context, fonts, stdlib, main, now, from, to, ppi).orThrowWithWarnings()
    }

    fun fileContext(files: (FileDescriptor) -> RResult<Base64Bytes, FileError>): FSContext {
        return FSContext(bridge.filesCache {
            files(it.deserialize()).serialize()
        }, bridge)
    }

    fun fontCollection(includeSystem: Boolean, includeEmbedded: Boolean, fontPaths: List<String>): FontCollection {
        return FontCollection(bridge.fontCollection(includeSystem, includeEmbedded, fontPaths.serialize()), bridge)
    }

    fun evictCache(maxAge: Long) {
        bridge.evictCache(maxAge)
    }
}
