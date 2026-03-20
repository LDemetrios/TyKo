package org.ldemetrios.tyko.driver.api

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

interface MemoryInterface {
    fun write(ptr: Long, data: ByteArray)
    fun write(ptr: Long, data: ShortArray)
    fun write(ptr: Long, data: IntArray)
    fun write(ptr: Long, data: LongArray)
    fun readBytes(ptr: Long, size: Long): ByteArray
    fun readShorts(ptr: Long, size: Long): ShortArray
    fun readInts(ptr: Long, size: Long): IntArray
    fun readLongs(ptr: Long, size: Long): LongArray
}

@TyKoInternalApi
data class FlattenedSyntaxTree(
    val marks: LongArray,
    val errors: ByteArray,
    val errorStarts: IntArray,
)

@TyKoInternalApi
data class RawNow(val millisOrFlag: Long, val nanos: Int)

private val newTicket = AtomicLong(0)
private val readersByTicket = ConcurrentHashMap<Long, (String) -> String>()
private val nativeCallsByTicket = ConcurrentHashMap<Long, (Long, String, String) -> String>()

@OptIn(TyKoFFIEntity::class)
class TypstCore @TyKoInternalApi constructor(
    val pointersDebugMode: ResourceDebugMode = ResourceDebugMode.None,
    private val provider: (
        readByTicket: (Long) -> (String) -> String,
        nativeCallByTicket: (Long) -> (Long, String, String) -> String,
    ) -> Pair<TypstDriver, MemoryInterface>,
) {
    private val ticketer = AtomicLong(0)
    private val registry = ConcurrentHashMap<Long, Pointer>()

    private fun pointer(value: Long, type: PointerType, freer: (Long) -> Unit): Pointer {
        if (value == 0L) {
            throw IllegalStateException("Native call returned, or deserializer found, a nullptr. This violates a driver contract.")
        }
        val ticket = ticketer.getAndIncrement()
        val ptr = Pointer(
            value, this, type, {
                freer(it)
            }, pointersDebugMode
        )
        ptr.addShutdownHook { registry.remove(ticket) }
        registry[ticket] = ptr
        return ptr
    }


    private val pair = provider(
        { ticket ->
            readersByTicket.get(ticket) ?: throw RuntimeException("Reader function $ticket is already cleaned up")
        },
        { ticket ->
            nativeCallsByTicket.get(ticket)
                ?: throw RuntimeException("Native callback $ticket is already cleaned up")
        },
    )

    @TyKoInternalApi
    val driver = pair.first

    @TyKoInternalApi
    val mem = pair.second

    @OptIn(TyKoInternalApi::class)
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return withRawStringResult { resultPtr ->
            val contentRaw = writeRawString(content)
            driver.format_source(resultPtr, contentRaw.len, contentRaw.ptr, column, tabWidth)
        }
    }

    @TyKoInternalApi
    fun library(features: Int): Pointer {
        return pointer(
            driver.library(features),
            PointerType.Library,
            driver::free_lazy_hash_library,
        )
    }

    @TyKoInternalApi
    fun query(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
        selector: String,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val mainRaw = writeRawString(main)
                            val selectorRaw = writeRawString(selector)

                            driver.query(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                mainRaw.len,
                                mainRaw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                                selectorRaw.len,
                                selectorRaw.ptr,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun parseSyntax(string: String, mode: Int): FlattenedSyntaxTree {
        val treePtr = driver.allocate_flattened_tree()
        try {
            val raw = writeRawString(string)
            driver.parse_syntax(treePtr, raw.len, raw.ptr, mode)
            return readFlattenedSyntaxTree(treePtr)
        } finally {
            driver.release_flattened_tree(treePtr)
        }
    }

    @TyKoInternalApi
    fun evalMain(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val raw = writeRawString(main)
                            driver.eval_main(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                libraryPtr,
                                fontsPtr,
                                raw.len,
                                raw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    @TyKoFFIEntity
    fun <R> precompilePaged(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        deserializer: (String, (Long) -> Pointer) -> R,
        main: String,
        now: RawNow,
    ): R {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        deserializer(
                            withRawStringResult { resultPtr ->
                                val mainRaw = writeRawString(main)
                                driver.precompile_paged(
                                    sessionTicket,
                                    resultPtr,
                                    contextPtr,
                                    fontsPtr,
                                    libraryPtr,
                                    mainRaw.len,
                                    mainRaw.ptr,
                                    now.millisOrFlag,
                                    now.nanos,
                                )
                            }
                        ) {
                            pointer(it, PointerType.PagedDocument, driver::free_paged_document)
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun compileHtml(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val mainRaw = writeRawString(main)
                            driver.compile_html(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                mainRaw.len,
                                mainRaw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun compileSvg(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
        from: Int,
        to: Int,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val mainRaw = writeRawString(main)
                            driver.compile_svg(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                mainRaw.len,
                                mainRaw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                                from,
                                to,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun renderSvg(
        document: PointerAccess,
        from: Int,
        to: Int,
    ): String {
        return document.withPointer(this, PointerType.PagedDocument) { documentPtr ->
            withRawStringResult { resultPtr ->
                driver.render_svg(
                    resultPtr,
                    documentPtr,
                    from,
                    to,
                )
            }
        }
    }

    @TyKoInternalApi
    fun compilePng(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
        from: Int,
        to: Int,
        ppi: Double,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val mainRaw = writeRawString(main)
                            driver.compile_png(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                mainRaw.len,
                                mainRaw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                                from,
                                to,
                                ppi,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun renderPng(
        document: PointerAccess,
        from: Int,
        to: Int,
        ppi: Double,
    ): String {
        return document.withPointer(this, PointerType.PagedDocument) { documentPtr ->
            withRawStringResult { resultPtr ->
                driver.render_png(
                    resultPtr,
                    documentPtr,
                    from,
                    to,
                    ppi,
                )
            }
        }
    }

    @TyKoInternalApi
    fun renderPdf(
        session: ResourceReference<JavaObjectsRegistrar>?,
        document: PointerAccess,
        context: PointerAccess,
        options: String
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                document.withPointer(this, PointerType.PagedDocument) { documentPtr ->
                    withRawStringResult { resultPtr ->
                        val optionsRaw = writeRawString(options)
                        driver.render_pdf(
                            sessionTicket,
                            resultPtr,
                            documentPtr,
                            contextPtr,
                            optionsRaw.len,
                            optionsRaw.ptr,
                        )
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun compilePngMergedWithLinks(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
        ppi: Double,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val mainRaw = writeRawString(main)
                            driver.compile_png_merged_with_links(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                mainRaw.len,
                                mainRaw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                                ppi,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun compilePdf(
        session: ResourceReference<JavaObjectsRegistrar>?,
        context: PointerAccess,
        fonts: PointerAccess,
        library: PointerAccess,
        main: String,
        now: RawNow,
        options: String,
    ): String {
        return withSession(session) { sessionTicket ->
            context.withPointer(this, PointerType.FSContext) { contextPtr ->
                fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                    library.withPointer(this, PointerType.Library) { libraryPtr ->
                        withRawStringResult { resultPtr ->
                            val mainRaw = writeRawString(main)
                            val optionsRaw = writeRawString(options)
                            driver.compile_pdf(
                                sessionTicket,
                                resultPtr,
                                contextPtr,
                                fontsPtr,
                                libraryPtr,
                                mainRaw.len,
                                mainRaw.ptr,
                                now.millisOrFlag,
                                now.nanos,
                                optionsRaw.len,
                                optionsRaw.ptr,
                            )
                        }
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun filesCache(reader: (String) -> String): Pointer {
        val readerTicket = newTicket.getAndIncrement()
        readersByTicket[readerTicket] = reader
        return try {
            pointer(
                driver.files_cache(readerTicket),
                PointerType.FSContext,
                {
                    driver.free_files_cache(it)
                    readersByTicket.remove(readerTicket)
                },
            )
        } catch (e: ThreadDeath) {
            throw e
        } catch (e: VirtualMachineError) { // Fall through, nothing we can do at that point
            throw e
        } catch (e: Throwable) {
            readersByTicket.remove(readerTicket)
            throw e
        }
    }

    @TyKoInternalApi
    fun resetFile(fsContext: PointerAccess, id: String) {
        fsContext.withPointer(this, PointerType.FSContext) { fsContextPtr ->
            val raw = writeRawString(id)
            driver.reset_file(fsContextPtr, raw.len, raw.ptr)
        }
    }

    @TyKoInternalApi
    fun resolvePreviewPackage(id: String): String {
        return withRawStringResult { resultPtr ->
            val raw = writeRawString(id)
            driver.resolve_preview_package(resultPtr, raw.len, raw.ptr)
        }
    }

    @TyKoInternalApi
    fun fontCollection(
        includeSystem: Boolean,
        includeEmbedded: Boolean,
        fontPaths: String
    ): Pointer {
        val raw = writeRawString(fontPaths)
        return pointer(
            driver.font_collection(
                if (includeSystem) 1 else 0,
                if (includeEmbedded) 1 else 0,
                raw.len, raw.ptr
            ),
            PointerType.FontCollection,
            driver::free_font_collection
        )
    }

    @TyKoInternalApi
    fun <R> withInputs(
        session: ResourceReference<JavaObjectsRegistrar>?,
        library: PointerAccess,
        fonts: PointerAccess,
        inputs: String,
        deserializer: (String, (Long) -> Pointer) -> R
    ): R {
        return withSession(session) { sessionTicket ->
            fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                library.withPointer(this, PointerType.Library) { libraryPtr ->
                    deserializer(
                        withRawStringResult { resultPtr ->
                            val raw = writeRawString(inputs)
                            driver.with_inputs(
                                sessionTicket,
                                resultPtr, fontsPtr,
                                libraryPtr,
                                raw.len, raw.ptr, 0
                            )
                        }) {
                        pointer(it, PointerType.Library, driver::free_lazy_hash_library)
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun <R> alterInputs(
        session: ResourceReference<JavaObjectsRegistrar>?,
        library: Pointer, fonts: PointerAccess, inputs: String,
        deserializer: (String, (Long) -> Pointer) -> R
    ): R {
        return withSession(session) { sessionTicket ->
            fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                library.consume(this, PointerType.Library) { libraryPtr ->
                    deserializer(
                        withRawStringResult { resultPtr ->
                            val raw = writeRawString(inputs)
                            driver.with_inputs(
                                sessionTicket,
                                resultPtr, fontsPtr,
                                libraryPtr,
                                raw.len, raw.ptr, 1
                            )
                        }) {
                        pointer(it, PointerType.Library, driver::free_lazy_hash_library)
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun <R> withStyles(
        session: ResourceReference<JavaObjectsRegistrar>?,
        library: PointerAccess,
        fonts: PointerAccess,
        styles: String,
        append: Boolean,
        deserializer: (String, (Long) -> Pointer) -> R
    ): R {
        return withSession(session) { sessionTicket ->
            fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                library.withPointer(this, PointerType.Library) { libraryPtr ->
                    deserializer(withRawStringResult { resultPtr ->
                        val raw = writeRawString(styles)
                        driver.with_styles(
                            sessionTicket,
                            resultPtr,
                            fontsPtr,
                            libraryPtr,
                            raw.len,
                            raw.ptr,
                            0,
                            if (append) 1 else 0
                        )
                    }) {
                        pointer(
                            it,
                            PointerType.Library,
                            driver::free_lazy_hash_library
                        )

                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun <R> alterStyles(
        session: ResourceReference<JavaObjectsRegistrar>?,
        library: Pointer,
        fonts: PointerAccess,
        styles: String,
        append: Boolean,
        deserializer: (String, (Long) -> Pointer) -> R
    ): R {
        return withSession(session) { sessionTicket ->
            fonts.withPointer(this, PointerType.FontCollection) { fontsPtr ->
                library.consume(this, PointerType.Library) { libraryPtr ->
                    deserializer(
                        withRawStringResult { resultPtr ->
                            val raw = writeRawString(styles)
                            driver.with_styles(
                                sessionTicket,
                                resultPtr,
                                fontsPtr,
                                libraryPtr,
                                raw.len,
                                raw.ptr,
                                1,
                                if (append) 1 else 0
                            )
                        }) {
                        pointer(
                            it,
                            PointerType.Library,
                            driver::free_lazy_hash_library
                        )
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun withTestDefinitions(library: PointerAccess): Pointer {
        return library.withPointer(
            this,
            PointerType.Library
        ) { libraryPtr ->
            pointer(
                driver.ext_with_test_definitions(
                    libraryPtr,
                    0
                ),
                PointerType.Library,
                driver::free_lazy_hash_library
            )
        }
    }

    @TyKoInternalApi
    fun addTestDefinitions(library: Pointer): Pointer {
        return library.consume(this, PointerType.Library) { libraryPtr ->
            pointer(
                driver.ext_with_test_definitions(
                    libraryPtr,
                    0
                ),
                PointerType.Library,
                driver::free_lazy_hash_library
            )
        }
    }

    @TyKoInternalApi
    fun createNativeFuncData(
        descriptor: String,
        hostCall: (Long, String, String) -> String
    ): Pointer {
        val ticket = newTicket.getAndIncrement()
        nativeCallsByTicket[ticket] = hostCall
        return try {
            val raw = writeRawString(descriptor)
            pointer(
                driver.create_native_func_data(
                    raw.len,
                    raw.ptr,
                    ticket
                ), PointerType.NativeFuncData
            ) { ptr ->
                driver.free_native_func_data(ptr)
                nativeCallsByTicket.remove(ticket)
            }
        } catch (e: ThreadDeath) {
            throw e
        } catch (e: VirtualMachineError) { // Fall through, nothing we can do at that point
            throw e
        } catch (e: Throwable) {
            nativeCallsByTicket.remove(ticket)
            throw e
        }
    }

    @TyKoInternalApi
    fun withNativeFunction(
        library: PointerAccess,
        nativeFuncData: PointerAccess,
        name: String,
    ): Pointer {
        return nativeFuncData.withPointer(
            this,
            PointerType.NativeFuncData
        ) { nativeFuncPtr ->
            library.withPointer(this, PointerType.Library) { libraryPtr ->
                val raw = writeRawString(name)
                pointer(
                    driver.register_native_func(
                        libraryPtr,
                        nativeFuncPtr,
                        raw.len,
                        raw.ptr,
                        0,
                    ),
                    PointerType.Library,
                    driver::free_lazy_hash_library
                )
            }
        }
    }

    @TyKoInternalApi
    fun addNativeFunction(
        library: Pointer,
        nativeFuncData: PointerAccess,
        name: String,
    ): Pointer {
        return nativeFuncData.withPointer(
            this,
            PointerType.NativeFuncData
        ) { nativeFuncPtr ->
            library.consume(this, PointerType.Library) { libraryPtr ->
                val raw = writeRawString(name)
                pointer(
                    driver.register_native_func(
                        libraryPtr,
                        nativeFuncPtr,
                        raw.len,
                        raw.ptr,
                        1,
                    ),
                    PointerType.Library,
                    driver::free_lazy_hash_library
                )
            }
        }
    }

    @TyKoInternalApi
    fun <R> withValue(
        session: ResourceReference<JavaObjectsRegistrar>?,
        library: PointerAccess,
        fonts: PointerAccess,
        name: String,
        value: String,
        deserializer: (String, (Long) -> Pointer) -> R,
    ): R {
        return withSession(session) { sessionTicket ->
            fonts.withPointer(
                this,
                PointerType.FontCollection
            ) { fontsPtr ->
                library.withPointer(
                    this,
                    PointerType.Library
                ) { libraryPtr ->
                    deserializer(
                        withRawStringResult { resultPtr ->
                            val nameRaw = writeRawString(name)
                            val valueRaw = writeRawString(value)
                            driver.register_value(
                                sessionTicket,
                                resultPtr,
                                fontsPtr,
                                libraryPtr,
                                nameRaw.len,
                                nameRaw.ptr,
                                valueRaw.len,
                                valueRaw.ptr,
                                0,
                            )
                        }
                    ) {
                        pointer(
                            it,
                            PointerType.Library,
                            driver::free_lazy_hash_library
                        )
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun <R> addValue(
        session: ResourceReference<JavaObjectsRegistrar>?,
        library: Pointer,
        fonts: PointerAccess,
        name: String,
        value: String,
        deserializer: (String, (Long) -> Pointer) -> R,
    ): R {
        return withSession(session) { sessionTicket ->
            fonts.withPointer(
                this,
                PointerType.FontCollection
            ) { fontsPtr ->
                library.consume(
                    this,
                    PointerType.Library
                ) { libraryPtr ->
                    deserializer(
                        withRawStringResult { resultPtr ->
                            val nameRaw = writeRawString(name)
                            val valueRaw = writeRawString(value)
                            driver.register_value(
                                sessionTicket,
                                resultPtr,
                                fontsPtr,
                                libraryPtr,
                                nameRaw.len,
                                nameRaw.ptr,
                                valueRaw.len,
                                valueRaw.ptr,
                                1,
                            )
                        }
                    ) {
                        pointer(
                            it,
                            PointerType.Library,
                            driver::free_lazy_hash_library
                        )
                    }
                }
            }
        }
    }

    @OptIn(TyKoInternalApi::class)
    fun evictCache(maxAge: Long) {
        driver.evict_cache(maxAge)
    }

    @TyKoInternalApi
    fun enforceTitle(document: PointerAccess, title: String) {
        document.withPointer(
            this,
            PointerType.PagedDocument
        ) { documentPtr ->
            val raw = writeRawString(title)
            driver.enforce_title(documentPtr, raw.len, raw.ptr)
        }
    }

    private data class RawStringHandle(val len: Long, val ptr: Long)

    @TyKoInternalApi
    private fun writeRawString(value: String): RawStringHandle {
        val bytes = value.toByteArray(Charsets.UTF_8)
        if (bytes.isEmpty()) {
            return RawStringHandle(0, 0)
        }
        val ptr =
            driver.allocate_ptr_for_raw_string(bytes.size.toLong())
        mem.write(ptr, bytes)
        return RawStringHandle(bytes.size.toLong(), ptr)
    }

    @TyKoInternalApi
    private fun withRawStringResult(call: (Long) -> Unit): String {
        val resultPtr = driver.allocate_raw_string()
        try {
            call(resultPtr)
            val (resultLen, resultDataPtr) = mem.readLongs(
                resultPtr,
                2
            )
            val resultBytes =
                if (resultLen == 0L || resultDataPtr == 0L) {
                    ByteArray(0)
                } else {
                    mem.readBytes(resultDataPtr, resultLen)
                }
            return String(resultBytes, Charsets.UTF_8)
        } finally {
            driver.free_raw_string(resultPtr)
        }
    }

    @TyKoInternalApi
    private fun readFlattenedSyntaxTree(treePtr: Long): FlattenedSyntaxTree {
        val fields = mem.readLongs(treePtr, 9)
        val marksPtr = fields[0]
        val marksLen = fields[1]
        val errorsPtr = fields[3]
        val errorsLen = fields[4]
        val errorsStartsPtr = fields[6]
        val errorsStartsLen = fields[7]

        val marks = if (marksLen == 0L || marksPtr == 0L) {
            LongArray(0)
        } else {
            mem.readLongs(marksPtr, marksLen)
        }
        val errors = if (errorsLen == 0L || errorsPtr == 0L) {
            ByteArray(0)
        } else {
            mem.readBytes(errorsPtr, errorsLen)
        }
        val errorStarts =
            if (errorsStartsLen == 0L || errorsStartsPtr == 0L) {
                IntArray(0)
            } else {
                mem.readInts(errorsStartsPtr, errorsStartsLen)
            }
        return FlattenedSyntaxTree(marks, errors, errorStarts)
    }

    @TyKoInternalApi
    fun close() {
        val iterator = registry.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            next.value.consume(this, next.value.type) {}
        }
        driver.dispose()
    }
}
