package org.ldemetrios.tyko.driver.api

interface TypstDriver {
    fun format_source(resultPtr: Long, contentLen: Long, contentPtr: Long, column: Int, tabWidth: Int)
    fun query(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        selectorLen: Long,
        selectorPtr: Long,
    )

    fun parse_syntax(resultPtr: Long, stringLen: Long, stringPtr: Long, mode: Int)
    fun allocate_flattened_tree(): Long
    fun release_flattened_tree(treePtr: Long)

    fun detached_eval(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        sourceLen: Long,
        sourcePtr: Long,
        mode: Int
    )

    fun detached_eval_warned(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        sourceLen: Long,
        sourcePtr: Long,
        mode: Int
    )

    fun eval_main_warned(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
    )

    fun compile_html(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
    )

    fun precompile_paged(
        result: Long,
        context: Long,
        fonts: Long,
        stdlib: Long,
        main_len: Long,
        main_ptr: Long,
        now_millis_or_flag: Long,
        now_nanos: Int,
    )

    fun compile_svg(
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
    )

    fun render_svg(
        result: Long,
        document: Long,
        from: Int,
        to: Int,
    )

    fun compile_png(
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
        ppi: Double,
    )

    fun render_png(
        result: Long,
        document: Long,
        from: Int,
        to: Int,
        ppi: Double,
    )

    fun compile_png_merged_with_links(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        ppi: Double,
    )

    fun compile_pdf(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
        optionsLen: Long,
        optionsPtr: Long,
    )

    fun render_pdf(
        result: Long,
        document: Long,
        context: Long,
        options_len: Long,
        options_ptr: Long,
    )

    fun free_paged_document(ptr: Long)

    fun files_cache(readerTicket: Long): Long
    fun free_files_cache(cachePtr: Long)
    fun reset_file(cachePtr: Long, idLen: Long, idPtr: Long)

    fun font_collection(includeSystem: Int, includeEmbedded: Int, fontPathsLen: Long, fontPathsPtr: Long): Long
    fun free_font_collection(collectionPtr: Long)

    fun library(features: Int, inputsLen: Long, inputsPtr: Long): Long
    fun free_library(libraryPtr: Long)
    fun with_inputs(
        resultPtr: Long,
        fonts: Long,
        libraryPtr: Long,
        inputsLen: Long,
        inputsPtr: Long,
        closePrevious: Int
    )

    fun with_styles(
        resultPtr: Long,
        fonts: Long,
        libraryPtr: Long,
        stylesLen: Long,
        stylesPtr: Long,
        closePrevious: Int,
        append: Int
    )

    fun ext_with_test_definitions(libraryPtr: Long, close_previous: Int): Long

    fun free_lazy_hash_library(lazyHashPtr: Long)

    fun clone(libraryPtr: Long): Long

    fun allocate_raw_string(): Long
    fun free_raw_string(rawStringPtr: Long)
    fun allocate_ptr_for_raw_string(size: Long): Long

    fun evict_cache(maxAge: Long)
    fun boot(longs: LongArray)

    fun enforce_title(documentPtr: Long, titleLen: Long, titlePtr: Long)
    fun dispose() = Unit
}
