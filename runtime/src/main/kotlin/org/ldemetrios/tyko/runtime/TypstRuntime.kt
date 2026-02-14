package org.ldemetrios.tyko.runtime

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
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
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
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.repr
import java.io.Closeable

/**
 * Brings together object model of Typst's elements ([IntoValue] hierarchy), and the [TypstCompiler].
 * For methods in TypstCompiler that require specific Typst code, TypstRuntime provides a typed wrapper.
 * Additionally, it handles common defaults for fonts, stdlib, etc.
 *
 * @param bridge An instance of [TypstCore] to wrap. No more than one TypstRuntime can possess a single core
 * @param defaultFeatures features, which default library will be created with
 */
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

    /**
     * Call Typstyle formatter on a code, with given settings.
     *
     * @param content The source code to format
     * @param column How wide the result should be, maximum
     * @param tabWidth The indentation shift
     */
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return compiler.formatSource(content, column, tabWidth)
    }

    /**
     * Construct a Library, carrying all definitions achievable from within a document.
     *
     * @param features Set of optional [Feature]s to enable.
     */
    fun library(features: Set<Feature> = setOf()): Library {
        return compiler.library(features)
    }

    /**
     * Construct a Library, carrying all definitions achievable from within a document.
     *
     * @param features Optional [Feature]s to enable.
     */
    fun library(vararg features: Feature): Library {
        return library(features.toSet())
    }

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param selector Typed selector expression
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
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

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result, or throws an exception if query failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param selector Typed selector expression
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
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

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result with warnings, or throws an exception if query failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param selector Typed selector expression
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
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

    /**
     * Parse Typst source code into an abstract syntax tree.
     *
     * @param string Source code to parse
     * @param mode Parsing mode (markup, code, math)
     */
    fun parseSyntax(string: String, mode: SyntaxMode): SyntaxTree {
        return compiler.parseSyntax(string, mode)
    }

    /**
     * Evaluate the entry file and return result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the evaluation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun evalRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ) = compiler.evalRaw(context, fonts, stdlib, main, now).map { it.map { deserialize(it) } }

    /**
     * Evaluate the entry file and return result, or throw if evaluation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the evaluation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun eval(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ) = compiler.eval(context, fonts, stdlib, main, now).let {
        deserialize(it)
    }

    /**
     * Evaluate the entry file and return result with warnings, or throw if evaluation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the evaluation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun evalWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ) = compiler.evalWarned(context, fonts, stdlib, main, now).map {
        deserialize(it)
    }

    /**
     * Compile a document to HTML.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun compileHtmlRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return compiler.compileHtmlRaw(context, fonts, stdlib, main, now)
    }

    /**
     * Compile a document to HTML.
     * Returns result, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun compileHtml(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): String {
        return compiler.compileHtml(context, fonts, stdlib, main, now)
    }

    /**
     * Compile a document to HTML.
     * Returns result with warnings, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun compileHtmlWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<String> {
        return compiler.compileHtmlWarned(context, fonts, stdlib, main, now)
    }

    /**
     * Compile a document to SVG pages.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     */
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

    /**
     * Compile a document to SVG pages.
     * Returns result, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     */
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

    /**
     * Compile a document to SVG pages.
     * Returns result with warnings, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     */
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

    /**
     * Compile a document to PNG pages.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     * @param ppi Output resolution in pixels per inch
     */
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

    /**
     * Compile a document to PNG pages.
     * Returns result, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     * @param ppi Output resolution in pixels per inch
     */
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

    /**
     * Compile a document to PNG pages.
     * Returns result with warnings, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     * @param ppi Output resolution in pixels per inch
     */
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

    /**
     * Compile a document to a merged bitmap with positional links.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param ppi Output resolution in pixels per inch
     */
    @TyKoInternalApi
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

    /**
     * Compile a document to PDF.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from Kept for API symmetry with paged renderers
     * @param to Kept for API symmetry with paged renderers
     * @param options Optional PDF serialization options
     */
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
        return compiler.compilePdfRaw(context, fonts, stdlib, main, now, options)
    }

    /**
     * Compile a document to PDF.
     * Returns result, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from Kept for API symmetry with paged renderers
     * @param to Kept for API symmetry with paged renderers
     * @param options Optional PDF serialization options
     */
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
        return compiler.compilePdf(context, fonts, stdlib, main, now, options)
    }

    /**
     * Compile a document to PDF.
     * Returns result with warnings, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from Kept for API symmetry with paged renderers
     * @param to Kept for API symmetry with paged renderers
     * @param options Optional PDF serialization options
     */
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
        return compiler.compilePdfWarned(context, fonts, stdlib, main, now, options)
    }

    /**
     * Precompile a paged document, delaying final format rendering.
     * Returns a precompiled document, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun precompilePaged(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): PagedDocument {
        return compiler.precompilePaged(context, fonts, stdlib, main, now)
    }

    /**
     * Precompile a paged document, delaying final format rendering.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun precompilePagedRaw(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<RResult<PagedDocument, List<SourceDiagnostic>>> {
        return compiler.precompilePagedRaw(context, fonts, stdlib, main, now)
    }

    /**
     * Precompile a paged document, delaying final format rendering.
     * Returns result with warnings, or throws if compilation failed.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param stdlib What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun precompilePagedWarned(
        context: FSContext,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollection = defaultFonts,
        stdlib: Library = defaultLibrary,
        now: Now? = Now.System,
    ): Warned<PagedDocument> {
        return compiler.precompilePagedWarned(context, fonts, stdlib, main, now)
    }

    /**
     * Create a virtual file-system context.
     *
     * @param files Callback for retrieving file content. Accepts file location in VFS, returns either content or an error.
     */
    fun fileContext(files: (FileDescriptor) -> RResult<ByteArray, FileError>): FSContext {
        return compiler.fileContext(files)
    }

    /**
     * Use native mechanism for package resolution, for usual package namespaces like "preview" or "local".
     *
     * @param file file descriptor
     */
    fun resolvePackage(file: FileDescriptor): RResult<ByteArray, FileError> {
        return compiler.resolvePackage(file)
    }

    /**
     * Construct file-system context from virtual in-memory files.
     *
     * @param files Mapping from virtual path to file contents
     */
    fun fileContext(files: Map<String, String>): FSContext {
        return compiler.fileContext {
            files[it.virtualPath]?.let {
                RResult.Ok(it.toByteArray())
            } ?: RResult.Err(
                when (val pack = it.packageSpec) {
                    null -> FileError.NotFound(it.virtualPath)
                    else -> FileError.Package(PackageError.NotFound(pack))
                }
            )
        }
    }

    /**
     * Construct a collection of fonts available to compiler operations.
     *
     * @param includeSystem Whether system-installed fonts should be loaded
     * @param includeEmbedded Whether embedded fallback fonts should be loaded
     * @param fontPaths Additional file-system paths to scan for fonts
     */
    fun fontCollection(
        includeSystem: Boolean,
        includeEmbedded: Boolean,
        fontPaths: List<String> = listOf()
    ): FontCollection {
        return compiler.fontCollection(includeSystem, includeEmbedded, fontPaths)
    }

    /**
     * Construct a collection of fonts available to compiler operations.
     *
     * @param includeSystem Whether system-installed fonts should be loaded
     * @param includeEmbedded Whether embedded fallback fonts should be loaded
     * @param fontPaths Additional file-system paths to scan for fonts
     */
    fun fontCollection(
        includeSystem: Boolean = false,
        includeEmbedded: Boolean = true,
        vararg fontPaths: String
    ): FontCollection {
        return fontCollection(includeSystem, includeEmbedded, fontPaths.asList())
    }

    /**
     * Construct a collection of fonts available to compiler operations.
     *
     * @param fontPaths Additional file-system paths to scan for fonts
     */
    fun fontCollection(vararg fontPaths: String): FontCollection {
        return fontCollection(false, true, fontPaths.asList())
    }

    /**
     * https://docs.rs/comemo/latest/comemo/fn.evict.html
     *
     * Evict the global cache.
     *
     * This removes all memoized results from the cache whose age is larger than or equal to `max_age`.
     * The age of a result grows by one during each eviction and is reset to zero when the result produces a cache hit.
     * Set `max_age` to zero to completely clear the cache.
     */
    fun evictCache(maxAge: Long) {
        compiler.evictCache(maxAge)
    }

    /**
     * Execute [body] with a temporary context that contains only `/main.typ`.
     * The created context is always closed afterwards.
     *
     * @param content Source code to place into `/main.typ`
     * @param body Action to execute with created context
     */
    fun <T> withSingleFile(content: String, body: FSContext.() -> T): T {
        val fileCtx = fileContext(mapOf("/main.typ" to content))
        return body(fileCtx).also { fileCtx.close() }
    }

    /**
     * Release all initialized default runtime resources and close the underlying core.
     */
    override fun close() {
        if (emptyFileCtxF.isInitialized()) emptyFileCtx.close()
        if (defaultFontsF.isInitialized()) defaultFonts.close()
        if (defaultLibraryF.isInitialized()) defaultLibrary.close()
        compiler.close()
    }
}


/**
 * Construct a new [Library] with the same definitions, but new `sys.inputs`.
 *
 * @param inputs Values for `sys.inputs`
 * @param fonts Fonts accessible while compiling `inputs`
 * @param closePrevious Whether the current instance should be closed.
 * Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`.
 * However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
 */
fun Library.withInputs(
    inputs: TDict<TValue>,
    fonts: FontCollection,
    closePrevious: Boolean
): RResult<Library, List<SourceDiagnostic>> {
    return withInputs(inputs.repr(), fonts, closePrevious)
}

/**
 * Construct a new [Library] with the same definitions, but new `sys.inputs`.
 * Returns a Library, or throws if `inputs` fail to compile.
 *
 * @param inputs Values for `sys.inputs`
 * @param fonts Fonts accessible while compiling `inputs`
 * @param closePrevious Whether the current instance should be closed.
 * Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`.
 * However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
 */
fun Library.withInputsOrThrow(inputs: TDict<TValue>, fonts: FontCollection, closePrevious: Boolean): Library {
    return withInputsOrThrow(inputs.repr(), fonts, closePrevious)
}

/**
 * Construct a new [Library] with the same definitions, but with new default styles applied.
 *
 * @param styles Styles to apply
 * @param fonts Fonts accessible while compiling styles
 * @param closePrevious Whether the current instance should be closed.
 * Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`.
 * However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
 * @param append Whether styles should be appended instead of replacing existing style set
 */
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

/**
 * Construct a new [Library] with the same definitions, but with new default styles applied.
 * Returns a Library, or throws if styles fail to compile.
 *
 * @param styles Styles to apply
 * @param fonts Fonts accessible while compiling styles
 * @param closePrevious Whether the current instance should be closed.
 * Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`.
 * However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
 * @param append Whether styles should be appended instead of replacing existing style set
 */
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
