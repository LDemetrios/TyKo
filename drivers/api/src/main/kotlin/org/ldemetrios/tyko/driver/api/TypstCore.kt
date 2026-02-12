package org.ldemetrios.tyko.driver.api

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


data class RawNow(val millisOrFlag: Long, val nanos: Int)

data class FlattenedSyntaxTree(
    val marks: LongArray,
    val errors: ByteArray,
    val errorStarts: IntArray,
)

private val newTicket = AtomicLong(0)
private val functions = HashMap<Long, (String) -> String>()

class TypstCore(
    private val provider: ((Long) -> (String) -> String) -> Pair<TypstDriver, MemoryInterface>
) {
    private val pair = provider {
        functions.get(it) ?: throw RuntimeException("Function $it is already cleaned up")
    }
    val driver = pair.first
    val mem = pair.second
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return withRawStringResult { resultPtr ->
            val contentRaw = writeRawString(content)
            driver.format_source(resultPtr, contentRaw.len, contentRaw.ptr, column, tabWidth)
        }
    }

    fun library(features: Int): Pointer {
        return Pointer(
            driver.library(features),
            driver,
            driver::free_lazy_hash_library
        )
    }

    fun query(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
        selector: String,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            val selectorRaw = writeRawString(selector)
            driver.query(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
                mainRaw.len,
                mainRaw.ptr,
                now.millisOrFlag,
                now.nanos,
                selectorRaw.len,
                selectorRaw.ptr,
            )
        }
    }

    fun parseSyntax(string: String, mode: Int): FlattenedSyntaxTree {
        val treePtr = driver.allocate_flattened_tree()
        val raw = writeRawString(string)
        driver.parse_syntax(treePtr, raw.len, raw.ptr, mode)
        val tree = readFlattenedSyntaxTree(treePtr)
        driver.release_flattened_tree(treePtr)
        return tree
    }

    fun evalMain(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
    ): String {
        return withRawStringResult { resultPtr ->
            val raw = writeRawString(main)
            driver.eval_main(
                resultPtr,
                context.ptr,
                stdlib.ptr,
                fonts.ptr,
                raw.len,
                raw.ptr,
                now.millisOrFlag,
                now.nanos,
            )
        }
    }

    @Deprecated("The result of this function contains a Pointer in a serialized form")
    fun precompilePaged(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            driver.precompile_paged(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
                mainRaw.len,
                mainRaw.ptr,
                now.millisOrFlag,
                now.nanos,
            )
        }
    }

    @Deprecated("Freeing lifted out of Core is dangerous", level = DeprecationLevel.WARNING)
    fun freePagedDocument(ptr: Long) = driver.free_paged_document(ptr)

    fun compileHtml(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            driver.compile_html(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
                mainRaw.len,
                mainRaw.ptr,
                now.millisOrFlag,
                now.nanos,
            )
        }
    }

    fun compileSvg(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
        from: Int,
        to: Int,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            driver.compile_svg(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
                mainRaw.len,
                mainRaw.ptr,
                now.millisOrFlag,
                now.nanos,
                from,
                to,
            )
        }
    }

    fun renderSvg(
        document: Pointer,
        from: Int,
        to: Int,
    ): String {
        return withRawStringResult { resultPtr ->
            driver.render_svg(
                resultPtr,
                document.ptr,
                from,
                to,
            )
        }
    }

    fun compilePng(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
        from: Int,
        to: Int,
        ppi: Double,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            driver.compile_png(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
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

    fun renderPng(
        document: Pointer,
        from: Int,
        to: Int,
        ppi: Double,
    ): String {
        return withRawStringResult { resultPtr ->
            driver.render_png(
                resultPtr,
                document.ptr,
                from,
                to,
                ppi,
            )
        }
    }

    fun renderPdf(
        document: Pointer,
        context: Pointer,
        options: String
    ): String {
        return withRawStringResult { resultPtr ->
            val optionsRaw = writeRawString(options)
            driver.render_pdf(
                resultPtr,
                document.ptr,
                context.ptr,
                optionsRaw.len,
                optionsRaw.ptr,
            )
        }
    }

    fun compilePngMergedWithLinks(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
        ppi: Double,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            driver.compile_png_merged_with_links(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
                mainRaw.len,
                mainRaw.ptr,
                now.millisOrFlag,
                now.nanos,
                ppi,
            )
        }
    }

    fun compilePdf(
        context: Pointer,
        fonts: Pointer,
        stdlib: Pointer,
        main: String,
        now: RawNow,
        options: String,
    ): String {
        return withRawStringResult { resultPtr ->
            val mainRaw = writeRawString(main)
            val optionsRaw = writeRawString(options)
            driver.compile_pdf(
                resultPtr,
                context.ptr,
                fonts.ptr,
                stdlib.ptr,
                mainRaw.len,
                mainRaw.ptr,
                now.millisOrFlag,
                now.nanos,
                optionsRaw.len,
                optionsRaw.ptr,
            )
        }
    }

    fun filesCache(reader: (String) -> String): Pointer {
        val readerTicket = newTicket.getAndIncrement()
        functions[readerTicket] = reader
        return Pointer(
            driver.files_cache(readerTicket),
            driver
        ) {
            driver.free_files_cache(it)
            functions.remove(readerTicket)
        }
    }

    fun resetFile(cache: Pointer, id: String) {
        val raw = writeRawString(id)
        driver.reset_file(cache.ptr, raw.len, raw.ptr)
    }

    fun resolvePreviewPackage(id: String): String {
        return withRawStringResult { resultPtr ->
            val raw = writeRawString(id)
            driver.resolve_preview_package(resultPtr, raw.len, raw.ptr)
        }
    }

    fun fontCollection(includeSystem: Boolean, includeEmbedded: Boolean, fontPaths: String): Pointer {
        val raw = writeRawString(fontPaths)
        return Pointer(
            driver.font_collection(
                if (includeSystem) 1 else 0,
                if (includeEmbedded) 1 else 0,
                raw.len, raw.ptr
            ),
            driver,
            driver::free_font_collection
        )
    }

    fun withInputs(library: Pointer, fonts: Pointer, inputs: String, closePrevious: Boolean): String {
        return withRawStringResult { resultPtr ->
            val raw = writeRawString(inputs)
            driver.with_inputs(
                resultPtr, fonts.ptr,
                if (closePrevious) library.take() else library.ptr,
                raw.len, raw.ptr, if (closePrevious) 1 else 0
            )
        }
    }

    fun withStyles(library: Pointer, fonts: Pointer, styles: String, closePrevious: Boolean, append: Boolean): String {
        return withRawStringResult { resultPtr ->
            val raw = writeRawString(styles)
            driver.with_styles(
                resultPtr,
                fonts.ptr,
                if (closePrevious) library.take() else library.ptr,
                raw.len,
                raw.ptr,
                if (closePrevious) 1 else 0,
                if (append) 1 else 0
            )
        }
    }

    fun withTestDefinitions(library: Pointer, closePrevious: Boolean): Pointer {
        return Pointer(
            driver.ext_with_test_definitions(
                if (closePrevious) library.take() else library.ptr,
                if (closePrevious) 1 else 0
            ),
            driver,
            driver::free_lazy_hash_library
        )
    }

    fun evictCache(maxAge: Long) {
        driver.evict_cache(maxAge)
    }

    fun enforceTitle(document: Pointer, title: String) {
        val raw = writeRawString(title)
        driver.enforce_title(document.ptr, raw.len, raw.ptr)
    }

    private data class RawStringHandle(val len: Long, val ptr: Long)

    private fun writeRawString(value: String): RawStringHandle {
        val bytes = value.toByteArray(Charsets.UTF_8)
        if (bytes.isEmpty()) {
            return RawStringHandle(0, 0)
        }
        val ptr = driver.allocate_ptr_for_raw_string(bytes.size.toLong())
        mem.write(ptr, bytes)
        return RawStringHandle(bytes.size.toLong(), ptr)
    }

    private fun withRawStringResult(call: (Long) -> Unit): String {
        val resultPtr = driver.allocate_raw_string()
        call(resultPtr)
        val (resultLen, resultDataPtr) = mem.readLongs(resultPtr, 2)
        val resultBytes = if (resultLen == 0L || resultDataPtr == 0L) {
            ByteArray(0)
        } else {
            mem.readBytes(resultDataPtr, resultLen)
        }
        driver.free_raw_string(resultPtr)
        return String(resultBytes, Charsets.UTF_8)
    }

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
        val errorStarts = if (errorsStartsLen == 0L || errorsStartsPtr == 0L) {
            IntArray(0)
        } else {
            mem.readInts(errorsPtr, errorsStartsLen)
        }
        return FlattenedSyntaxTree(marks, errors, errorStarts)
    }

    fun close() {
        remainingPointers().filter { it.owner == this.driver }.forEach { it.close() }
        driver.dispose()
    }
}
