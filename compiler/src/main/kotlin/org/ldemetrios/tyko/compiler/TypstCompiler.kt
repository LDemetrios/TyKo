package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.NoopCore
import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.TyKoCorrectnessAlert
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.TypstCore

/**
 * Represents a set of definitions, which the compilation of documents will consider to be stdlib.
 *
 * Instances manage a native pointer and must be used with the respect to [Pointer] handling rules (most notably, closed in time).
 */
data class Library(internal val pointer: Pointer, internal val bridge: TypstCore) : AutoCloseable {
    /**
     * Release the underlying native library pointer, freeing the memory.
     */
    override fun close() = pointer.close()

    /**
     * Construct a new [Library] with the same definitions, but new `sys.inputs`.
     * Returns either a Library, or an error if `inputs` fail to compile.
     *
     * @param inputs Typst _code_, representing the new inputs dictionary.
     *       This code will be evaluated with the current instance of Library.
     * @param fonts Fonts accessible while compiling `inputs`.
     * @param closePrevious Whether the current instance should be [close]d.
     * Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`.
     * However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
     */
    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withInputs(
        inputs: String,
        fonts: FontCollection,
        closePrevious: Boolean
    ): RResult<Library, List<SourceDiagnostic>> {
        val list: RResult<Long, List<SourceDiagnostic>> =
            bridge.withInputs(pointer, fonts.pointer, inputs, closePrevious).deserialize()
        return list.sanitize().map { Library(pointer.another(it), bridge) }
    }

    /**
     * Construct a new [Library] with the same definitions, but new `sys.inputs`.
     * Returns a Library, or throws an [TypstCompilerException] if `inputs` fail to compile.
     *
     * @param inputs Typst _code_, representing the new inputs dictionary.
     *       This code will be evaluated with the current instance of Library.
     * @param fonts Fonts accessible while compiling `inputs`.
     * @param closePrevious Whether the current instance should be [close]d.
     *  Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`
     * However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
     */
    fun withInputsOrThrow(inputs: String, fonts: FontCollection, closePrevious: Boolean): Library {
        return withInputs(inputs, fonts, closePrevious).orThrow()
    }

    /**
     * Construct a new [Library] with built-in test definitions attached.
     *
     * @param closePrevious Whether to release the previous native library allocation.
     */
    @TyKoInternalApi
    fun withTestDefinitions(closePrevious: Boolean): Library {
        return Library(bridge.withTestDefinitions(pointer, closePrevious), bridge)
    }

    /**
     * Construct a new [Library] with the same definitions, but with new default styles applied.
     * Returns either a new Library, or an error if `styled` failed to compile.
     *
     * @param styled The Typst _code_, representing a "styled" element, which the style will be taken from.
     * @param fonts Fonts accessible while compiling `styled`.
     * @param closePrevious Whether to release the previous native library allocation.
     *  Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`
     *  However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
     * @param append Whether styles should be appended instead of replacing the existing style set.
     */
    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
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

    /**
     * Construct a new [Library] with the same definitions, but with new default styles applied.
     * Returns a Library, or throws an [TypstCompilerException] if `styled` fail to compile.
     *
     * @param styled The Typst _code_, representing a "styled" element, which the style will be taken from.
     * @param fonts Fonts accessible while compiling `styled`.
     * @param closePrevious Whether to release the previous native library allocation.
     *  Note that if `closePrevious = true`, the current instance will be closed regardless of success in compilation of `inputs`
     *  However, it is more efficient than creating new one by `closePrevious = false`, and then closing this.
     * @param append Whether styles should be appended instead of replacing the existing style set.
     */
    fun withStylesOrThrow(
        styled: String,
        fonts: FontCollection,
        closePrevious: Boolean,
        append: Boolean,
    ): Library {
        return withStyles(styled, fonts, closePrevious, append).orThrow()
    }
}

/**
 * A virtual file-system context used by compiler operations.
 *
 * Instances manage a native pointer and must be used with the respect to [Pointer] handling rules (most notably, closed in time).
 */
data class FSContext(
    val pointer: Pointer,
    val bridge: TypstCore,
    val original: (FileDescriptor) -> RResult<ByteArray, FileError>
) : AutoCloseable {
    /**
     * Release the underlying native library pointer, freeing the memory.
     */
    override fun close() = pointer.close()

    /**
     * Invalidate a specific file in the underlying cache.
     */
    @OptIn(TyKoInternalApi::class)
    fun resetFile(file: FileDescriptor) {
        bridge.resetFile(pointer, file.serialize())
    }
}

/**
 * A set of fonts available to the compiler.
 *
 * Instances manage a native pointer and must be used with the respect to [Pointer] handling rules (most notably, closed in time).
 */
data class FontCollection(val pointer: Pointer, val bridge: TypstCore) : AutoCloseable {
    /**
     * Release the underlying native library pointer, freeing the memory.
     */
    override fun close() = pointer.close()

    companion object {
        @TyKoInternalApi
        fun nullptr() = FontCollection(Pointer.nullptr(), NoopCore)
    }
}

/**
 * An intermediate representation of a document, being compiled to a paged format (pdf, svg or png).
 * Can be used multiple times to compile to several format simultaneously, not repeating compilation process from the beginning.
 *
 * Instances manage a native pointer and must be used with the respect to [Pointer] handling rules (most notably, closed in time).
 * `PagedDocument` also carries the original [FSContext], which it was compiled with. That context should not be closed before document is.
 */
data class PagedDocument(val pointer: Pointer, val carriedContext: FSContext, val bridge: TypstCore) : AutoCloseable {
    /**
     * Release the underlying native library pointer, freeing the memory.
     */
    override fun close() = pointer.close()

    /**
     * Finish compilation to PNG format.
     *
     * @param from First page index (inclusive).
     * @param to Last page index (exclusive).
     * @param ppi Output resolution in pixels per inch.
     */
    @OptIn(TyKoInternalApi::class)
    fun renderPng(from: Int = 0, to: Int = Int.MAX_VALUE, ppi: Double = 144.0) =
        bridge
            .renderPng(pointer, from, to, ppi)
            .deserialize<List<Base64Bytes>>()
            .map { it.bytes }

    /**
     * Finish compilation to SVG format.
     *
     * @param from First page index (inclusive).
     * @param to Last page index (exclusive).
     */
    @OptIn(TyKoInternalApi::class)
    fun renderSvg(from: Int = 0, to: Int = Int.MAX_VALUE) =
        bridge
            .renderSvg(pointer, from, to)
            .deserialize<List<String>>()

    /**
     * Finish compilation to PDF format.
     * This, specifically, can create new errors while enforcing a particular pdf standard.
     * Either returns a pdf, or an error.
     *
     * @param options PDF configuration
     */
    @OptIn(TyKoInternalApi::class)
    fun renderPdfRaw(options: PdfOptionsConfig? = null) =
        bridge
            .renderPdf(pointer, carriedContext.pointer, options?.serialize() ?: "")
            .deserialize<RResult<Base64Bytes, List<SourceDiagnostic>>>()
            .sanitize()
            .map { it.bytes }

    /**
     * Finish compilation to PDF format.
     * This, specifically, can create new errors while enforcing a particular pdf standard.
     * Either returns a pdf, or throws a [TypstCompilerException].
     *
     * @param options Optional PDF serialization options.
     */
    fun renderPdf(options: PdfOptionsConfig? = null) = renderPdfRaw(options).orThrow()

    /**
     * Explicitly set document metadata to have a particular title.
     */
    @OptIn(TyKoInternalApi::class)
    fun enforceTitle(title: String) = bridge.enforceTitle(pointer, title)
}

/**
 * The main class of this level of abstraction.
 * Encapsulates the remains of memory handling, unfortunately spilled by [TypstCore],
 * and provides more user-friendly API for Typst compiler access.
 */
class TypstCompiler(val bridge: TypstCore) {
    /**
     * Call Typstyle formatter on a code, with given settings. Unfortunately only Markup mode is supported at the moment.
     *
     * @param content The source code to format
     * @param column How wide the result should be, maximum
     * @param tabWidth The indentation shift
     */
    @OptIn(TyKoInternalApi::class)
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return bridge.formatSource(content, column, tabWidth)
    }

    /**
     * Construct a Library, carrying all the definitions, achievable from within the document.
     * @param features Set of optional [Feature]s to enable.
     */
    @OptIn(TyKoInternalApi::class)
    fun library(features: Set<Feature>): Library {
        val features = features.map { 1 shl it.ordinal }.fold(0, Int::or)
        val pointer = bridge.library(features)
        return Library(pointer, bridge)
    }

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     * @param selector Correct Typst _code_, representing a selector
     */
    @OptIn(TyKoInternalApi::class)
    @TyKoCorrectnessAlert
    fun queryRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.query(context.pointer, fonts.pointer, library.pointer, main.serialize(), now.toRawNow(), selector)
            .deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result if successful, throws exception otherwise. Ignores warnings.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     * @param selector Correct Typst _code_, representing a selector
     */
    @TyKoCorrectnessAlert
    fun query(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): String {
        return queryRaw(context, fonts, library, main, now, selector).orThrowIgnoringWarnings()
    }

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result with warnings if successful, throws exception otherwise.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     * @param selector Correct Typst _code_, representing a selector
     */
    @TyKoCorrectnessAlert
    fun queryWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): Warned<String> {
        return queryRaw(context, fonts, library, main, now, selector).orThrowWithWarnings().sanitize()
    }

    /**
     * Parse Typst source into a [SyntaxTree].
     *
     * @param string The code to parse
     * @param mode How to treat this code (see [SyntaxMode])
     */
    @OptIn(TyKoInternalApi::class)
    fun parseSyntax(string: String, mode: SyntaxMode): SyntaxTree {
        return bridge.parseSyntax(string, mode.ordinal).toSyntaxTree(string)
    }

    /**
     * Evaluate the main file.
     * Returns result as provided, with warnings and errors.
     * The value is serialized into JSON, however, using mechanisms different from Typst's own serialization mechanisms.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
    @OptIn(TyKoInternalApi::class)
    fun evalRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.evalMain(
            context.pointer,
            fonts.pointer,
            library.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    /**
     * Evaluate the main file.
     * Returns result with warnings if successful, throws exception otherwise.
     * The value is serialized into JSON, however, using mechanisms different from Typst's own serialization mechanisms.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
    fun evalWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<String> {
        return evalRaw(context, fonts, library, main, now).orThrowWithWarnings()
    }

    /**
     * Evaluate the main file.
     * Returns result with warnings if successful, throws exception otherwise.
     * The value is serialized into JSON, however, using mechanisms different from Typst's own serialization mechanisms.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
    fun eval(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): String {
        return evalRaw(context, fonts, library, main, now).orThrowIgnoringWarnings()
    }

    /**
     * Compile a document into an intermediate representation of [PagedDocument].
     * Can be then finished into any of paged formats (SVG, PDF, PNG), but not HTML.
     * Returns result as provided, with warnings and errors.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     */
    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun precompilePagedRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<PagedDocument, List<SourceDiagnostic>>> {
        val x: Warned<RResult<Long, List<SourceDiagnostic>>> = bridge.precompilePaged(
            context.pointer,
            fonts.pointer,
            library.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize()
        return x.sanitize().map {
            it.map { ptr ->
                PagedDocument(
                    Pointer(ptr, bridge.driver, bridge::freePagedDocument),
                    context,
                    bridge,
                )
            }
        }
    }


    /**
     * Compile a document into an intermediate representation of [PagedDocument].
     * Can be then finished into any of paged formats (SVG, PDF, PNG), but not HTML.
     * Returns result if successful, throws exception otherwise. Ignores warnings.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     */
    fun precompilePaged(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): PagedDocument {
        return precompilePagedRaw(context, fonts, library, main, now).orThrowIgnoringWarnings()
    }

    /**
     * Compile a document into an intermediate representation of [PagedDocument].
     * Can be then finished into any of paged formats (SVG, PDF, PNG), but not HTML.
     * Returns result with warnings if successful, throws exception otherwise.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     */
    fun precompilePagedWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<PagedDocument> {
        return precompilePagedRaw(context, fonts, library, main, now).orThrowWithWarnings()
    }

    /**
     * Compile a document into HTML.
     * Returns result as provided, with warnings and errors.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     */
    @OptIn(TyKoInternalApi::class)
    fun compileHtmlRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.compileHtml(
            context.pointer,
            fonts.pointer,
            library.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    /**
     * Compile a document into HTML.
     * Returns result if successful, throws exception otherwise. Ignores warnings.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     */
    fun compileHtml(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): String {
        return compileHtmlRaw(context, fonts, library, main, now).orThrowIgnoringWarnings()
    }

    /**
     * Compile a document into HTML.
     * Returns result with warnings if successful, throws exception otherwise.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     */
    fun compileHtmlWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<String> {
        return compileHtmlRaw(context, fonts, library, main, now).orThrowWithWarnings()
    }

    /**
     * Compile a document into SVG.
     * Returns result as provided, with warnings and errors.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param from Start page, inclusive (clipped to document size)
     *  @param to End page, exclusive (clipped to document size)
     */
    @OptIn(TyKoInternalApi::class)
    fun compileSvgRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
    ): Warned<RResult<List<String>, List<SourceDiagnostic>>> {
        return bridge.compileSvg(
            context.pointer,
            fonts.pointer,
            library.pointer,
            main.serialize(),
            now.toRawNow(),
            from,
            to,
        ).deserialize<Warned<RResult<List<String>, List<SourceDiagnostic>>>>().sanitize()
    }

    /**
     * Compile a document into SVG.
     * Returns result if successful, throws exception otherwise. Ignores warnings.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param from Start page, inclusive (clipped to document size)
     *  @param to End page, exclusive (clipped to document size)
     */
    fun compileSvg(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
    ): List<String> {
        return compileSvgRaw(context, fonts, library, main, now, from, to).orThrowIgnoringWarnings()
    }

    /**
     * Compile a document into SVG.
     * Returns result with warnings if successful, throws exception otherwise.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param from Start page, inclusive (clipped to document size)
     *  @param to End page, exclusive (clipped to document size)
     */
    fun compileSvgWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
    ): Warned<List<String>> {
        return compileSvgRaw(context, fonts, library, main, now, from, to).orThrowWithWarnings()
    }

    @TyKoInternalApi
    fun compileMergedWithLinksRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        ppi: Double,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return bridge.compilePngMergedWithLinks(
            context.pointer,
            fonts.pointer,
            library.pointer,
            main.serialize(),
            now.toRawNow(),
            ppi,
        )
            .deserialize<Warned<RResult<Base64Bytes, List<SourceDiagnostic>>>>()
            .sanitize()
            .map { it.map { it.bytes } }
    }

    /**
     * Compile a document into PDF.
     * Returns result as provided, with warnings and errors.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param options Pdf configuration
     */
    @OptIn(TyKoInternalApi::class)
    fun compilePdfRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        options: PdfOptionsConfig? = null,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return bridge.compilePdf(
            context.pointer,
            fonts.pointer,
            library.pointer,
            main.serialize(),
            now.toRawNow(),
            options?.serialize() ?: "",
        )
            .deserialize<Warned<RResult<Base64Bytes, List<SourceDiagnostic>>>>()
            .sanitize()
            .map { it.map { it.bytes } }
    }

    /**
     * Compile a document into PDF.
     * Returns result if successful, throws exception otherwise. Ignores warnings.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param options Pdf configuration
     */
    fun compilePdf(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        options: PdfOptionsConfig? = null,
    ): ByteArray {
        return compilePdfRaw(context, fonts, library, main, now, options).orThrowIgnoringWarnings()
    }

    /**
     * Compile a document into PDF.
     * Returns result with warnings if successful, throws exception otherwise.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param options Pdf configuration
     */
    fun compilePdfWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        options: PdfOptionsConfig? = null,
    ): Warned<ByteArray> {
        return compilePdfRaw(context, fonts, library, main, now, options).orThrowWithWarnings()
    }

    /**
     * Compile a document into PNG.
     * Returns result as provided, with warnings and errors.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param from Start page, inclusive (clipped to document size)
     *  @param to End page, exclusive (clipped to document size)
     *  @param ppi Output resolution in pixels per inch.
     */
    @OptIn(TyKoInternalApi::class)
    fun compilePngRaw(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        ppi: Double,
    ): Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>> {
        return bridge.compilePng(
            context.pointer,
            fonts.pointer,
            library.pointer,
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

    /**
     * Compile a document into PNG.
     * Returns result if successful, throws exception otherwise. Ignores warnings.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param from Start page, inclusive (clipped to document size)
     *  @param to End page, exclusive (clipped to document size)
     *  @param ppi Output resolution in pixels per inch.
     */
    fun compilePng(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        ppi: Double,
    ): List<ByteArray> {
        return compilePngRaw(context, fonts, library, main, now, from, to, ppi).orThrowIgnoringWarnings()
    }

    /**
     * Compile a document into PNG.
     * Returns result with warnings if successful, throws exception otherwise.
     *
     *  @param context Virtual File System, which the file contents will be searched in
     *  @param fonts Fonts, available to compiler
     *  @param library What the compilation will consider to be stdlib
     *  @param main The entry point of the compilation
     *  @param now Time, which the compilation will consider to be "now"
     *  @param from Start page, inclusive (clipped to document size)
     *  @param to End page, exclusive (clipped to document size)
     *  @param ppi Output resolution in pixels per inch.
     */
    fun compilePngWarned(
        context: FSContext,
        fonts: FontCollection,
        library: Library,
        main: FileDescriptor,
        now: Now?,
        from: Int,
        to: Int,
        ppi: Double,
    ): Warned<List<ByteArray>> {
        return compilePngRaw(context, fonts, library, main, now, from, to, ppi).orThrowWithWarnings()
    }

    /**
     * Create a virtual file-system context.
     *
     * @param files Callback for retrieving file content. Accepts file location in VFS, returns either content or an error.
     */
    @OptIn(TyKoInternalApi::class)
    fun fileContext(files: (FileDescriptor) -> RResult<ByteArray, FileError>): FSContext {
        return FSContext(bridge.filesCache {
            files(it.deserialize()).map {
                Base64Bytes(it)
            }.serialize()
        }, bridge, files)
    }

    /**
     * Use native mechanism for package resolution, for usual package namespaces like "preview" or "local".
     */
    @OptIn(TyKoInternalApi::class)
    fun resolvePackage(file: FileDescriptor): RResult<ByteArray, FileError> {
        return bridge.resolvePreviewPackage(file.serialize())
            .deserialize<ExtendedFileResult>()
            .map { it.bytes }
    }

    /**
     * Create a font book, encapsulating fonts available to compilation.
     *
     * @param includeSystem Whether system fonts should be included (doesn't work on each driver)
     * @param includeEmbedded Whether fonts bundled with Typst should be included
     * @param fontPaths Where else to search for fonts
     */
    @OptIn(TyKoInternalApi::class)
    fun fontCollection(includeSystem: Boolean, includeEmbedded: Boolean, fontPaths: List<String>): FontCollection {
        return FontCollection(bridge.fontCollection(includeSystem, includeEmbedded, fontPaths.serialize()), bridge)
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
        bridge.evictCache(maxAge)
    }

    /**
     * Close the underlying TypstCore bridge and release all owned resources.
     */
    fun close() {
        bridge.close()
    }
}

@TyKoInternalApi
private typealias ExtendedFileResult = RResult<Base64Bytes, FileError>
