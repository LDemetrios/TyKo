package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.NoopCore
import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.TyKoCorrectnessAlert
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.TypstCore
import java.io.Closeable

data class Library(internal val pointer: Pointer, internal val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()

    @TyKoCorrectnessAlert
    fun withInputs(
        inputs: String,
        fonts: FontCollection,
        closePrevious: Boolean
    ): RResult<Library, List<SourceDiagnostic>> {
        val list: RResult<Long, List<SourceDiagnostic>> =
            bridge.withInputs(pointer, fonts.pointer, inputs, closePrevious).deserialize()
        return list.sanitize().map { Library(pointer.another(it), bridge) }
    }

    @TyKoCorrectnessAlert
    fun withInputsOrThrow(inputs: String, fonts: FontCollection, closePrevious: Boolean): Library {
        return withInputs(inputs, fonts, closePrevious).orThrow()
    }

    fun withTestDefinitions(closePrevious: Boolean): Library {
        return Library(bridge.withTestDefinitions(pointer, closePrevious), bridge)
    }

    @TyKoCorrectnessAlert
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

    @TyKoCorrectnessAlert
    fun withStylesOrThrow(
        styled: String,
        fonts: FontCollection,
        closePrevious: Boolean,
        append: Boolean,
    ): Library {
        return withStyles(styled, fonts, closePrevious, append).orThrow()
    }
}

data class FSContext(
    val pointer: Pointer,
    val bridge: TypstCore,
    val original: (FileDescriptor) -> RResult<ByteArray, FileError>
) : Closeable {
    override fun close() = pointer.close()

    fun resetFile(file: FileDescriptor) {
        bridge.resetFile(pointer, file.serialize())
    }
}

data class FontCollection(val pointer: Pointer, val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()

    companion object {
        @TyKoInternalApi
        fun nullptr() = FontCollection(Pointer.nullptr(), NoopCore)
    }
}

data class PagedDocument(val pointer: Pointer, val carriedContext: FSContext, val bridge: TypstCore) : Closeable {
    override fun close() = pointer.close()

    @OptIn(TyKoInternalApi::class)
    fun renderPng(from: Int = 0, to: Int = Int.MAX_VALUE, ppi: Double = 144.0) =
        bridge
            .renderPng(pointer, from, to, ppi)
            .deserialize<List<Base64Bytes>>()
            .map { it.bytes }

    fun renderSvg(from: Int = 0, to: Int = Int.MAX_VALUE) =
        bridge
            .renderSvg(pointer, from, to)
            .deserialize<List<String>>()

    @OptIn(TyKoInternalApi::class)
    fun renderPdfRaw(options: PdfOptionsConfig? = null) =
        bridge
            .renderPdf(pointer, carriedContext.pointer, options?.serialize() ?: "")
            .deserialize<RResult<Base64Bytes, List<SourceDiagnostic>>>()
            .sanitize()
            .map { it.bytes }

    fun renderPdf(options: PdfOptionsConfig? = null) = renderPdfRaw(options).orThrow()

    fun enforceTitle(title: String) = bridge.enforceTitle(pointer, title)
}

class TypstCompiler(val bridge: TypstCore) {
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return bridge.formatSource(content, column, tabWidth)
    }

    fun libraryProvider(features: Set<Feature>): Library {
        val features = features.map { 1 shl it.ordinal }.fold(0, Int::or)
        val pointer = bridge.library(features)
        return Library(pointer, bridge)
    }

    @TyKoCorrectnessAlert
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

    @TyKoCorrectnessAlert
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

    @TyKoCorrectnessAlert
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
        return bridge.parseSyntax(string, mode.ordinal).toSyntaxTree(string)
    }

    fun evalRaw(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return bridge.evalMain(
            context.pointer,
            fonts.pointer,
            stdlib.pointer,
            main.serialize(),
            now.toRawNow(),
        ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
    }

    fun evalWarned(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): Warned<String> {
        return evalRaw(context, fonts, stdlib, main, now).orThrowWithWarnings()
    }

    fun eval(
        context: FSContext,
        fonts: FontCollection,
        stdlib: Library,
        main: FileDescriptor,
        now: Now?,
    ): String {
        return evalRaw(context, fonts, stdlib, main, now).orThrowIgnoringWarnings()
    }

    @OptIn(TyKoFFIEntity::class)
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
                    Pointer(ptr, bridge.driver, bridge::freePagedDocument),
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

    @OptIn(TyKoInternalApi::class)
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

    @OptIn(TyKoInternalApi::class)
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

    @OptIn(TyKoInternalApi::class)
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

    @OptIn(TyKoInternalApi::class)
    fun fileContext(files: (FileDescriptor) -> RResult<ByteArray, FileError>): FSContext {
        return FSContext(bridge.filesCache {
            files(it.deserialize()).map {
                Base64Bytes(it)
            }.serialize()
        }, bridge, files)
    }

    @OptIn(TyKoInternalApi::class)
    fun resolvePreviewPackage(file: FileDescriptor): RResult<ByteArray, FileError> {
        return bridge.resolvePreviewPackage(file.serialize())
            .deserialize<ExtendedFileResult>()
            .map { it.bytes }
    }

    fun fontCollection(includeSystem: Boolean, includeEmbedded: Boolean, fontPaths: List<String>): FontCollection {
        return FontCollection(bridge.fontCollection(includeSystem, includeEmbedded, fontPaths.serialize()), bridge)
    }

    fun evictCache(maxAge: Long) {
        bridge.evictCache(maxAge)
    }

    fun close() {
        bridge.close()
    }
}

@TyKoInternalApi
private typealias ExtendedFileResult = RResult<Base64Bytes, FileError>
