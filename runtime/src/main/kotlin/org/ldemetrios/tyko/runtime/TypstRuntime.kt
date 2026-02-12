package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.Base64Bytes
import org.ldemetrios.tyko.compiler.FSContext
import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.FontCollection
import org.ldemetrios.tyko.compiler.Library
import org.ldemetrios.tyko.compiler.Now
import org.ldemetrios.tyko.compiler.PackageError
import org.ldemetrios.tyko.compiler.PagedDocument
import org.ldemetrios.tyko.compiler.PdfOptionsConfig
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.SourceDiagnostic
import org.ldemetrios.tyko.compiler.SyntaxMode
import org.ldemetrios.tyko.compiler.SyntaxTree
import org.ldemetrios.tyko.compiler.TypstCompiler
import org.ldemetrios.tyko.compiler.Warned
import org.ldemetrios.tyko.compiler.map
import org.ldemetrios.tyko.driver.api.TypstCore
import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TSelector
import org.ldemetrios.tyko.model.TSpace
import org.ldemetrios.tyko.model.TStyle
import org.ldemetrios.tyko.model.TStyled
import org.ldemetrios.tyko.model.TStyles
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.deserialize
import org.ldemetrios.tyko.model.repr
import java.io.Closeable


class TypstRuntime(val bridge: TypstCore, val defaultFeatures: Set<Feature> = setOf()) : Closeable {
    private val compiler = TypstCompiler(bridge)

    private val emptyFileCtxF = lazy {
        compiler.fileContext {
            RResult.Err(FileError.AccessDenied)
        }
    }

    private val defaultLibraryF = lazy {
        library(defaultFeatures)
    }

    private val defaultFontsF = lazy {
        fontCollection(false, true, listOf())
    }

    private val emptyFileCtx by emptyFileCtxF
    private val defaultLibrary by defaultLibraryF
    private val defaultFonts by defaultFontsF

    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return compiler.formatSource(content, column, tabWidth)
    }

    fun library(features: Set<Feature> = setOf()): Library {
        return compiler.libraryProvider(features)
    }

    fun library(vararg features: Feature): Library {
        return library(features.toSet())
    }

    fun <S : TValue> queryRaw(
        context: FSContext,
        selector: TSelector<S>,
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        now: Now = Now.System,
    ): Warned<RResult<TArray<S>, List<SourceDiagnostic>>> {
        return compiler.queryRaw(context, fonts, stdlib, main, now, selector.repr())
            .map { it.map { deserialize(it) as TArray<S> } }
    }

    fun <S : TValue> query(
        context: FSContext,
        selector: TSelector<S>,
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        now: Now = Now.System,
    ): TArray<S> {
        return deserialize(compiler.query(context, fonts, stdlib, main, now, selector.repr())) as TArray<S>
    }

    fun <S : TValue> queryWarned(
        context: FSContext,
        selector: TSelector<S>,
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        now: Now = Now.System,
    ): Warned<TArray<S>> {
        return compiler.queryWarned(context, fonts, stdlib, main, now, selector.repr())
            .map { deserialize(it) as TArray<S> }
    }

    fun parseSyntax(string: String, mode: SyntaxMode): SyntaxTree {
        return compiler.parseSyntax(string, mode)
    }

    fun evalMainRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ) = compiler.evalMainRaw(context, fonts, stdlib, main, now).map { it.map { deserialize(it) } }

    fun evalMain(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ) = compiler.evalMain(context, fonts, stdlib, main, now).let {
        deserialize(it)
    }

    fun evalMainWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ) = compiler.evalMainWarned(context, fonts, stdlib, main, now).map {
        deserialize(it)
    }

    fun compileHtmlRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return compiler.compileHtmlRaw(context, fonts, stdlib, main, now)
    }

    fun compileHtml(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): String {
        return compiler.compileHtml(context, fonts, stdlib, main, now)
    }

    fun compileHtmlWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<String> {
        return compiler.compileHtmlWarned(context, fonts, stdlib, main, now)
    }

    fun compileSvgRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
    ): Warned<RResult<List<String>, List<SourceDiagnostic>>> {
        return compiler.compileSvgRaw(context, fonts, stdlib, main, now, from, to)
    }

    fun compileSvg(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
    ): List<String> {
        return compiler.compileSvg(context, fonts, stdlib, main, now, from, to)
    }

    fun compileSvgWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
    ): Warned<List<String>> {
        return compiler.compileSvgWarned(context, fonts, stdlib, main, now, from, to)
    }

    fun compilePngRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        ppi: Double = 144.0,
    ): Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>> {
        return compiler.compilePngRaw(context, fonts, stdlib, main, now, from, to, ppi)
    }

    fun compilePng(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        ppi: Double = 144.0,
    ): List<ByteArray> {
        return compiler.compilePng(context, fonts, stdlib, main, now, from, to, ppi)
    }

    fun compilePngWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        ppi: Double = 144.0,
    ): Warned<List<ByteArray>> {
        return compiler.compilePngWarned(context, fonts, stdlib, main, now, from, to, ppi)
    }

    fun compileMergedWithLinksRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        ppi: Double = 72.0,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return compiler.compileMergedWithLinksRaw(context, fonts, stdlib, main, now, ppi)
    }

    fun compileMergedWithLinks(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        ppi: Double = 72.0,
    ): ByteArray {
        return compiler.compileMergedWithLinks(context, fonts, stdlib, main, now, ppi)
    }

    fun compilePdfRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        options: PdfOptionsConfig? = null,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return compiler.compilePdfRaw(context, fonts, stdlib, main, now, from, to, options)
    }

    fun compilePdf(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        options: PdfOptionsConfig? = null,
    ): ByteArray {
        return compiler.compilePdf(context, fonts, stdlib, main, now, from, to, options)
    }

    fun compilePdfWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        options: PdfOptionsConfig? = null,
    ): Warned<ByteArray> {
        return compiler.compilePdfWarned(context, fonts, stdlib, main, now, from, to, options)
    }

    fun precompilePaged(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): PagedDocument {
        return compiler.precompilePaged(context, fonts, stdlib, main, now)
    }

    fun precompilePagedRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<RResult<PagedDocument, List<SourceDiagnostic>>> {
        return compiler.precompilePagedRaw(context, fonts, stdlib, main, now)
    }

    fun precompilePagedWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<PagedDocument> {
        return compiler.precompilePagedWarned(context, fonts, stdlib, main, now)
    }

    fun fileContext(files: (FileDescriptor) -> RResult<Base64Bytes, FileError>): FSContext {
        return compiler.fileContext(files)
    }

    fun resolvePreviewPackage(file: FileDescriptor): RResult<Base64Bytes, FileError> {
        return compiler.resolvePreviewPackage(file)
    }

    fun fileContext(files: Map<String, String>): FSContext {
        return compiler.fileContext {
            files[it.virtualPath]?.let {
                RResult.Ok(Base64Bytes(it.toByteArray()))
            } ?: RResult.Err(
                when (val pack = it.packageSpec) {
                    null -> FileError.NotFound(it.virtualPath)
                    else -> FileError.Package(PackageError.NotFound(pack))
                }
            )
        }
    }

    fun fontCollection(
        includeSystem: Boolean,
        includeEmbedded: Boolean,
        fontPaths: List<String> = listOf()
    ): FontCollection {
        return compiler.fontCollection(includeSystem, includeEmbedded, fontPaths)
    }

    fun fontCollection(
        includeSystem: Boolean = false,
        includeEmbedded: Boolean = true,
        vararg fontPaths: String
    ): FontCollection {
        return fontCollection(includeSystem, includeEmbedded, fontPaths.asList())
    }

    fun fontCollection(vararg fontPaths: String): FontCollection {
        return fontCollection(false, true, fontPaths.asList())
    }

    fun evictCache(maxAge: Long) {
        compiler.evictCache(maxAge)
    }

    fun <T> withSingleFile(content: String, body: FSContext.() -> T): T {
        val fileCtx = fileContext(mapOf("/main.typ" to content))
        return body(fileCtx).also { fileCtx.close() }
    }

    override fun close() {
        if (emptyFileCtxF.isInitialized()) emptyFileCtx.close()
        if (defaultFontsF.isInitialized()) defaultFonts.close()
        if (defaultLibraryF.isInitialized()) defaultLibrary.close()
        compiler.close()
    }
}


fun Library.withInputs(
    inputs: TDict<TValue>,
    fonts: FontCollection,
    closePrevious: Boolean
): RResult<Library, List<SourceDiagnostic>> {
    return withInputs(inputs.repr(), fonts, closePrevious)
}

fun Library.withInputsOrThrow(inputs: TDict<TValue>, fonts: FontCollection, closePrevious: Boolean): Library {
    return withInputsOrThrow(inputs.repr(), fonts, closePrevious)
}

fun Library.withStyles(
    vararg styles: TStyle,
    fonts: FontCollection,
    closePrevious: Boolean,
    append: Boolean,
): RResult<Library, List<SourceDiagnostic>> {
    return withStyles(
        TStyled(
            TStyles(
                TArray(
                    styles.asList()
                )
            ),
            TSpace(),
        ).repr(),
        fonts, closePrevious, append
    )
}

fun Library.withStylesOrThrow(
    vararg styles: TStyle,
    fonts: FontCollection,
    closePrevious: Boolean,
    append: Boolean,
): Library {
    return withStylesOrThrow(
        TStyled(
            TStyles(
                TArray(
                    styles.asList()
                )
            ),
            TSpace(),
        ).repr(),
        fonts, closePrevious, append
    )
}
