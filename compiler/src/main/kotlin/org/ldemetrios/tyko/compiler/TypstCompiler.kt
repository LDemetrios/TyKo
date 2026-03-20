package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrar
import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.ResourceReference
import org.ldemetrios.tyko.driver.api.Session
import org.ldemetrios.tyko.driver.api.TyKoCorrectnessAlert
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.TypstCore
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * The main class of this level of abstraction.
 * Encapsulates the remains of memory handling, unfortunately spilled by [TypstCore],
 * and provides more user-friendly API for Typst compiler access.
 */
class TypstCompiler internal constructor(internal val bridge: TypstCore, internal val parent: TypstCompiler?) {
    constructor(bridge: () -> TypstCore) : this(bridge(), null)

    private val children = ConcurrentHashMap<TypstCompiler, Unit>()

    fun childScope() = TypstCompiler(bridge, this).also { children[it] = Unit }

    private val closeLock = ReentrantReadWriteLock()
    private val closed = AtomicBoolean(false)

    @OptIn(TyKoInternalApi::class)
    fun close() {
        val values = closeLock.write {
            if (closed.getAndSet(true)) return
            val wrappers = registry.keys.toList()
            registry.clear()
            wrappers
        }

        children.forEach { it.key.close() }
        values.forEach { it.consume { } }
        if (parent == null) bridge.close()
    }

    private val registry = ConcurrentHashMap<PointerWrapper, Unit>()

    internal fun register(wrapper: PointerWrapper) {
        closeLock.read {
            if (closed.get()) {
                wrapper.consume { } // Immediately destroy
            } else {
                registry[wrapper] = Unit
            }
        }
    }

    internal fun unregister(wrapper: PointerWrapper) {
        registry.remove(wrapper)
    }

    /**
     * Call Typstyle formatter on a code, with given settings. Unfortunately only Markup mode is supported at the moment.
     *
     * @param content The source code to format
     * @param column How wide the result should be, maximum
     * @param tabWidth The indentation shift
     */
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return bridge.formatSource(content, column, tabWidth)
    }

    /**
     * Construct a Library, carrying all the definitions, achievable from within the document.
     * @param features Set of optional [Feature]s to enable.
     */
    @OptIn(TyKoInternalApi::class)
    fun library(features: Set<Feature>): LibraryPointer {
        val features = features.map { 1 shl it.ordinal }.fold(0, Int::or)
        val pointer = bridge.library(features)
        return LibraryPointer(pointer, this)
    }

    @TyKoInternalApi
    fun nativeFuncData(
        descriptor: NativeFuncDataDescriptor,
        hostCall: (session: Long, argsJson: String, contextJson: String) -> RResult<String, String>,
    ): NativeFuncDataPointer {
        return NativeFuncDataPointer(
            bridge.createNativeFuncData(descriptor.serialize()) { session, args, context ->
                hostCall(session, args, context).serialize()
            },
            this
        )
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.query(session, contextPtr, fontsPtr, libraryPtr, main.serialize(), now.toRawNow(), selector)
                        .deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
                }
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): String {
        return queryRaw(session, context, fonts, library, main, now, selector).outputOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        selector: String,
    ): Warned<String> {
        return queryRaw(session, context, fonts, library, main, now, selector).warnedOrThrow().sanitize()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.evalMain(
                        session,
                        contextPtr,
                        fontsPtr,
                        libraryPtr,
                        main.serialize(),
                        now.toRawNow(),
                    ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
                }
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): Warned<String> {
        return evalRaw(session, context, fonts, library, main, now).warnedOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): String {
        return evalRaw(session, context, fonts, library, main, now).outputOrThrow()
    }

    /**
     * Compile a document into an intermediate representation of [PagedDocumentPointer].
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): Warned<RResult<PagedDocumentPointer, List<SourceDiagnostic>>> {
        val carry = context.clone()
        return try {
            val sessionCarry = session?.reference()
            try {
                context.withPtr { contextPtr ->
                    fonts.withPtr { fontsPtr ->
                        library.withPtr { libraryPtr ->
                            bridge.precompilePaged(
                                session,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                { str, ptrConstr ->
                                    str.deserialize<Warned<RResult<Long, List<SourceDiagnostic>>>>()
                                        .sanitize()
                                        .map {
                                            it.map { ptr ->
                                                PagedDocumentPointer(
                                                    ptrConstr(ptr),
                                                    this,
                                                    carry,
                                                    sessionCarry,
                                                )
                                            }.mapError {
                                                carry.release()
                                                sessionCarry?.release()
                                                it
                                            }
                                        }
                                },
                                main.serialize(),
                                now.toRawNow(),
                            )
                        }
                    }
                }
            } catch (e: ThreadDeath) {
                throw e
            } catch (e: VirtualMachineError) {
                throw e
            } catch (e: Throwable) {
                sessionCarry?.release()
                throw e
            }
        } catch (e: ThreadDeath) {
            throw e
        } catch (e: VirtualMachineError) {
            throw e
        } catch (e: Throwable) {
            carry.release()
            throw e
        }
    }


    /**
     * Compile a document into an intermediate representation of [PagedDocumentPointer].
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): PagedDocumentPointer {
        return precompilePagedRaw(session, context, fonts, library, main, now).outputOrThrow()
    }

    /**
     * Compile a document into an intermediate representation of [PagedDocumentPointer].
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): Warned<PagedDocumentPointer> {
        return precompilePagedRaw(session, context, fonts, library, main, now).warnedOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.compileHtml(
                        session,
                        contextPtr,
                        fontsPtr,
                        libraryPtr,
                        main.serialize(),
                        now.toRawNow(),
                    ).deserialize<Warned<RResult<String, List<SourceDiagnostic>>>>().sanitize()
                }
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): String {
        return compileHtmlRaw(session, context, fonts, library, main, now).outputOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
    ): Warned<String> {
        return compileHtmlRaw(session, context, fonts, library, main, now).warnedOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        from: Int,
        to: Int,
    ): Warned<RResult<List<String>, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.compileSvg(
                        session,
                        contextPtr,
                        fontsPtr,
                        libraryPtr,
                        main.serialize(),
                        now.toRawNow(),
                        from,
                        to,
                    ).deserialize<Warned<RResult<List<String>, List<SourceDiagnostic>>>>().sanitize()
                }
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        from: Int,
        to: Int,
    ): List<String> {
        return compileSvgRaw(session, context, fonts, library, main, now, from, to).outputOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        from: Int,
        to: Int,
    ): Warned<List<String>> {
        return compileSvgRaw(session, context, fonts, library, main, now, from, to).warnedOrThrow()
    }

    @TyKoInternalApi
    fun compileMergedWithLinksRaw(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        ppi: Double,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.compilePngMergedWithLinks(
                        session,
                        contextPtr,
                        fontsPtr,
                        libraryPtr,
                        main.serialize(),
                        now.toRawNow(),
                        ppi,
                    )
                        .deserialize<Warned<RResult<Base64Bytes, List<SourceDiagnostic>>>>()
                        .sanitize()
                        .map { it.map { it.bytes } }
                }
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        options: PdfOptionsConfig? = null,
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.compilePdf(
                        session,
                        contextPtr,
                        fontsPtr,
                        libraryPtr,
                        main.serialize(),
                        now.toRawNow(),
                        options?.serialize() ?: "",
                    )
                        .deserialize<Warned<RResult<Base64Bytes, List<SourceDiagnostic>>>>()
                        .sanitize()
                        .map { it.map { it.bytes } }
                }
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        options: PdfOptionsConfig? = null,
    ): ByteArray {
        return compilePdfRaw(session, context, fonts, library, main, now, options).outputOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        options: PdfOptionsConfig? = null,
    ): Warned<ByteArray> {
        return compilePdfRaw(session, context, fonts, library, main, now, options).warnedOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        from: Int,
        to: Int,
        ppi: Double,
    ): Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>> {
        return context.withPtr { contextPtr ->
            fonts.withPtr { fontsPtr ->
                library.withPtr { libraryPtr ->
                    bridge.compilePng(
                        session,
                        contextPtr,
                        fontsPtr,
                        libraryPtr,
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
            }
        }
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        from: Int,
        to: Int,
        ppi: Double,
    ): List<ByteArray> {
        return compilePngRaw(session, context, fonts, library, main, now, from, to, ppi).outputOrThrow()
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
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: FSContextPointerReference,
        fonts: FontCollectionPointerReference,
        library: LibraryPointerReference,
        main: FileDescriptor,
        now: Now,
        from: Int,
        to: Int,
        ppi: Double,
    ): Warned<List<ByteArray>> {
        return compilePngRaw(session, context, fonts, library, main, now, from, to, ppi).warnedOrThrow()
    }

    /**
     * Create a virtual file-system context.
     *
     * @param files Callback for retrieving file content. Accepts file location in VFS, returns either content or an error.
     */
    @OptIn(TyKoInternalApi::class)
    fun fileContext(
        files: (FileDescriptor, Session?) -> RResult<ByteArray, FileError>,
        session: Session?
    ): FSContextPointer {
        return FSContextPointer(bridge.filesCache {
            files(it.deserialize(), session).map {
                Base64Bytes(it)
            }.serialize()
        }, this, files)
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
    fun fontCollection(
        includeSystem: Boolean,
        includeEmbedded: Boolean,
        fontPaths: List<String>
    ): FontCollectionPointer {
        return FontCollectionPointer(bridge.fontCollection(includeSystem, includeEmbedded, fontPaths.serialize()), this)
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
}

@TyKoInternalApi
internal typealias ExtendedFileResult = RResult<Base64Bytes, FileError>
