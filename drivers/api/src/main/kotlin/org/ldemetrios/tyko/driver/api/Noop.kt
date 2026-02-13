package org.ldemetrios.tyko.driver.api


@OptIn(TyKoFFIEntity::class)
internal object NoopDriver : TypstDriver {
    override fun format_source(
        resultPtr: Long,
        contentLen: Long,
        contentPtr: Long,
        column: Int,
        tabWidth: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun query(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        selectorLen: Long,
        selectorPtr: Long
    ) {
        throw UnsupportedOperationException()
    }

    override fun parse_syntax(
        resultPtr: Long,
        stringLen: Long,
        stringPtr: Long,
        mode: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun allocate_flattened_tree(): Long {
        throw UnsupportedOperationException()
    }

    override fun release_flattened_tree(treePtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun detached_eval(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        sourceLen: Long,
        sourcePtr: Long,
        mode: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun detached_eval_warned(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        sourceLen: Long,
        sourcePtr: Long,
        mode: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun eval_main(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun compile_html(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun precompile_paged(
        result: Long,
        context: Long,
        fonts: Long,
        stdlib: Long,
        main_len: Long,
        main_ptr: Long,
        now_millis_or_flag: Long,
        now_nanos: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun compile_svg(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        from: Int,
        to: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun render_svg(result: Long, document: Long, from: Int, to: Int) {
        throw UnsupportedOperationException()
    }

    override fun compile_png(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        from: Int,
        to: Int,
        ppi: Double
    ) {
        throw UnsupportedOperationException()
    }

    override fun render_png(
        result: Long,
        document: Long,
        from: Int,
        to: Int,
        ppi: Double
    ) {
        throw UnsupportedOperationException()
    }

    override fun compile_png_merged_with_links(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        ppi: Double
    ) {
        throw UnsupportedOperationException()
    }

    override fun compile_pdf(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        optionsLen: Long,
        optionsPtr: Long
    ) {
        throw UnsupportedOperationException()
    }

    override fun render_pdf(
        result: Long,
        document: Long,
        context: Long,
        options_len: Long,
        options_ptr: Long
    ) {
        throw UnsupportedOperationException()
    }

    override fun free_paged_document(ptr: Long) {
        throw UnsupportedOperationException()
    }

    override fun files_cache(readerTicket: Long): Long {
        throw UnsupportedOperationException()
    }

    override fun free_files_cache(cachePtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun reset_file(cachePtr: Long, idLen: Long, idPtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun resolve_preview_package(resultPtr: Long, idLen: Long, idPtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun font_collection(
        includeSystem: Int,
        includeEmbedded: Int,
        fontPathsLen: Long,
        fontPathsPtr: Long
    ): Long {
        throw UnsupportedOperationException()
    }

    override fun free_font_collection(collectionPtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun library(features: Int): Long {
        throw UnsupportedOperationException()
    }

    override fun free_library(libraryPtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun with_inputs(
        resultPtr: Long,
        fonts: Long,
        libraryPtr: Long,
        inputsLen: Long,
        inputsPtr: Long,
        closePrevious: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun with_styles(
        resultPtr: Long,
        fonts: Long,
        libraryPtr: Long,
        stylesLen: Long,
        stylesPtr: Long,
        closePrevious: Int,
        append: Int
    ) {
        throw UnsupportedOperationException()
    }

    override fun ext_with_test_definitions(libraryPtr: Long, close_previous: Int): Long {
        throw UnsupportedOperationException()
    }

    override fun free_lazy_hash_library(lazyHashPtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun clone(libraryPtr: Long): Long {
        throw UnsupportedOperationException()
    }

    override fun allocate_raw_string(): Long {
        throw UnsupportedOperationException()
    }

    override fun free_raw_string(rawStringPtr: Long) {
        throw UnsupportedOperationException()
    }

    override fun allocate_ptr_for_raw_string(size: Long): Long {
        throw UnsupportedOperationException()
    }

    override fun evict_cache(maxAge: Long) {
        throw UnsupportedOperationException()
    }

    override fun boot(longs: LongArray) {
        throw UnsupportedOperationException()
    }

    override fun enforce_title(documentPtr: Long, titleLen: Long, titlePtr: Long) {
        throw UnsupportedOperationException()
    }
}

internal object NoopMemory : MemoryInterface {
    override fun write(ptr: Long, data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun write(ptr: Long, data: ShortArray) {
        throw UnsupportedOperationException()
    }

    override fun write(ptr: Long, data: IntArray) {
        throw UnsupportedOperationException()
    }

    override fun write(ptr: Long, data: LongArray) {
        throw UnsupportedOperationException()
    }

    override fun readBytes(ptr: Long, size: Long): ByteArray {
        throw UnsupportedOperationException()
    }

    override fun readShorts(ptr: Long, size: Long): ShortArray {
        throw UnsupportedOperationException()
    }

    override fun readInts(ptr: Long, size: Long): IntArray {
        throw UnsupportedOperationException()
    }

    override fun readLongs(ptr: Long, size: Long): LongArray {
        throw UnsupportedOperationException()
    }
}

@OptIn(TyKoFFIEntity::class)
@TyKoInternalApi
val NoopCore = TypstCore { _ -> NoopDriver to NoopMemory }
