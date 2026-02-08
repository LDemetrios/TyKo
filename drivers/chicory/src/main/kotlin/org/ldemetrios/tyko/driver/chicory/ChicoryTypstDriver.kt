package org.ldemetrios.tyko.driver.chicory

import com.dylibso.chicory.compiler.MachineFactoryCompiler
import com.dylibso.chicory.runtime.ExportFunction
import com.dylibso.chicory.runtime.HostFunction
import com.dylibso.chicory.runtime.Instance
import com.dylibso.chicory.runtime.Instance.builder
import com.dylibso.chicory.runtime.Store
import com.dylibso.chicory.wasi.WasiOptions
import com.dylibso.chicory.wasi.WasiPreview1
import java.nio.file.Paths
import com.dylibso.chicory.wasm.Parser
import com.dylibso.chicory.wasm.WasmModule
import com.dylibso.chicory.wasm.types.FunctionType
import com.dylibso.chicory.wasm.types.NameCustomSection
import com.dylibso.chicory.wasm.types.ValType
import org.ldemetrios.tyko.driver.api.MemoryInterface
import org.ldemetrios.tyko.driver.api.TypstCore
import org.ldemetrios.tyko.driver.api.TypstDriver
import org.ldemetrios.tyko.driver.chicory.Demangler.demangle
import kotlin.collections.map

fun <T, A: T, B : T> B.tryTransforming(func: (B) -> A) = try {
    func(this)
} catch (e: VirtualMachineError) {
    throw e
} catch (e: Throwable) {
    this
}

fun sanitizeStackTrace(stackTrace: Array<StackTraceElement>, instance: Instance): Array<StackTraceElement> =
    stackTrace.tryTransforming {
        it.map {
            val idx = it.methodName.split("_").last().toIntOrNull()
            if (idx != null && it.className.startsWith(PREFIX)) {
                StackTraceElement(
                    "Chicory",
                    it.moduleName,
                    it.moduleVersion,
                    it.className.drop(PREFIX.length),
                    instance.functionName(idx)?.tryTransforming(::demangle) ?: idx.toString(),
                    it.fileName,
                    it.lineNumber,
                )
            } else it
        }
            .drop(1)
            .toTypedArray()
    }

class NativePanicException(val instance: Instance) : RuntimeException()

internal fun Instance.functionName(idx: Int) = module().run {
    if (idx < importSection().importCount()) {
        importSection().getImport(idx).name()
    } else {
        customSection("name")
            .let { it as? NameCustomSection }
            ?.nameOfFunction(idx)
    }
}

internal const val PREFIX = "com.dylibso.chicory.\$gen.CompiledMachine"

fun TypstChicoryInstance(
    module: WasmModule,
    options: WasiOptions,
    compiledMachine: Boolean = true,
    readFileByReaderTicket: (ticket: Long, coordinates: String) -> String,
): Instance {
    val readByTicket = HostFunction(
        "env", "read_file_by_reader_ticket",
        FunctionType.of(
            listOf(ValType.I32, ValType.I64, ValType.I64, ValType.I32),
            listOf()
        ),
    ) { instance, args ->
        val (result_ptr, ticket, coordinates_len, coordinates_ptr) = args;
        val coordinates = instance.memory().readString(coordinates_ptr.toInt(), coordinates_len.toInt())
        val result_v = readFileByReaderTicket(ticket, coordinates)
        val resultBytes = result_v.toByteArray()
        val ptr = instance.export("allocate_ptr_for_raw_string").apply(resultBytes.size.toLong())[0]
        instance.memory().write(ptr.toInt(), resultBytes)
        instance.memory().writeLong(result_ptr.toInt(), resultBytes.size.toLong())
        instance.memory().writeLong(result_ptr.toInt() + 8, ptr)
        longArrayOf()
    }

    val printBacktrace = HostFunction(
        "env", "throw_exception",
        FunctionType.of(listOf(), listOf())
    ) { instance, args ->
        throw NativePanicException(instance)
    }
    val store = Store()
    store.addFunction(readByTicket)
    store.addFunction(printBacktrace)
    val wasi = WasiPreview1.builder().withOptions(options).build();
    store.addFunction(*wasi.toHostFunctions())

    val builder = builder(module)
    builder.withImportValues(store.toImportValues())
    if (compiledMachine) {
        builder.withMachineFactory(MachineFactoryCompiler::compile)
    }
    val instance = builder.build()
    store.register("typst_shared", instance)
    return instance
}

class ChicoryMemoryInterface(private val instance: Instance) : MemoryInterface {
    override fun write(ptr: Long, data: ByteArray) {
        if (data.isEmpty()) {
            return
        }
        instance.memory().write(ptr.toInt(), data)
    }

    override fun write(ptr: Long, data: ShortArray) {
        var offset = ptr.toInt()
        for (value in data) {
            instance.memory().writeShort(offset, value)
            offset += Short.SIZE_BYTES
        }
    }

    override fun write(ptr: Long, data: IntArray) {
        var offset = ptr.toInt()
        for (value in data) {
            instance.memory().writeI32(offset, value)
            offset += Int.SIZE_BYTES
        }
    }

    override fun write(ptr: Long, data: LongArray) {
        var offset = ptr.toInt()
        for (value in data) {
            instance.memory().writeLong(offset, value)
            offset += Long.SIZE_BYTES
        }
    }

    override fun readBytes(ptr: Long, size: Long): ByteArray {
        if (size == 0L) {
            return ByteArray(0)
        }
        return instance.memory().readBytes(ptr.toInt(), size.toInt())
    }

    override fun readShorts(ptr: Long, size: Long): ShortArray {
        if (size == 0L) {
            return ShortArray(0)
        }
        val result = ShortArray(size.toInt())
        var offset = ptr.toInt()
        for (i in result.indices) {
            result[i] = instance.memory().readShort(offset)
            offset += Short.SIZE_BYTES
        }
        return result
    }

    override fun readInts(ptr: Long, size: Long): IntArray {
        if (size == 0L) {
            return IntArray(0)
        }
        val result = IntArray(size.toInt())
        var offset = ptr.toInt()
        for (i in result.indices) {
            result[i] = instance.memory().readInt(offset)
            offset += Int.SIZE_BYTES
        }
        return result
    }

    override fun readLongs(ptr: Long, size: Long): LongArray {
        if (size == 0L) {
            return LongArray(0)
        }
        val result = LongArray(size.toInt())
        var offset = ptr.toInt()
        for (i in result.indices) {
            result[i] = instance.memory().readLong(offset)
            offset += Long.SIZE_BYTES
        }
        return result
    }
}

@Suppress("removal")
context(driver: ChicoryTypstDriver) fun ExportFunction.call(vararg arguments: Long): LongArray {
    try {
        return apply(*arguments) ?: longArrayOf()
    } catch (e: ThreadDeath) {
        throw e
    } catch (e: VirtualMachineError) {
        throw e
    } catch (e: Throwable) {
        e.stackTrace = sanitizeStackTrace(e.stackTrace, driver.instance)
        throw e
    }
}

class ChicoryTypstDriver(val instance: Instance) : TypstDriver {
    override fun boot(longs: LongArray) {
        instance.export("boot_driver").call(*longs)
    }

    override fun format_source(
        resultPtr: Long,
        contentLen: Long,
        contentPtr: Long,
        column: Int,
        tabWidth: Int
    ) {
        instance.export("format_source").call(
            resultPtr, contentLen, contentPtr, column.toLong(), tabWidth.toLong()
        )
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
        instance.export("query").call(
            resultPtr,
            contextPtr,
            fontsPtr,
            stdlibPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
            selectorLen,
            selectorPtr
        )
    }

    override fun parse_syntax(
        resultPtr: Long,
        stringLen: Long,
        stringPtr: Long,
        mode: Int
    ) {
        instance.export("parse_syntax").call(
            resultPtr,
            stringLen,
            stringPtr,
            mode.toLong()
        )
    }

    override fun allocate_flattened_tree(): Long {
        return instance.export("allocate_flattened_tree").call()[0]
    }

    override fun release_flattened_tree(treePtr: Long) {
        instance.export("release_flattened_tree").call(treePtr)
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
        instance.export("detached_eval").call(
            resultPtr,
            contextPtr,
            stdlibPtr,
            fontsPtr,
            sourceLen,
            sourcePtr,
            mode.toLong()
        )
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
        instance.export("detached_eval_warned").call(
            resultPtr,
            contextPtr,
            stdlibPtr,
            fontsPtr,
            sourceLen,
            sourcePtr,
            mode.toLong()
        )
    }

    override fun compile_html(
        resultPtr: Long,
        contextPtr: Long,
        fontsPtr: Long,
        stdlibPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
    ) {
        instance.export("compile_html").call(
            resultPtr,
            contextPtr,
            fontsPtr,
            stdlibPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
        )
    }

    override fun eval_main_warned(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
    ) {
        instance.export("eval_main_warned").call(
            resultPtr,
            contextPtr,
            stdlibPtr,
            fontsPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
        )
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
        to: Int,
    ) {
        instance.export("compile_svg").call(
            resultPtr,
            contextPtr,
            fontsPtr,
            stdlibPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
            from.toLong(),
            to.toLong(),
        )
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
        ppi: Double,
    ) {
        instance.export("compile_png").call(
            resultPtr,
            contextPtr,
            fontsPtr,
            stdlibPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
            from.toLong(),
            to.toLong(),
            java.lang.Double.doubleToRawLongBits(ppi)
        )
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
        ppi: Double,
    ) {
        instance.export("compile_png_merged_with_links").call(
            resultPtr,
            contextPtr,
            fontsPtr,
            stdlibPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
            java.lang.Double.doubleToRawLongBits(ppi)
        )
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
        optionsPtr: Long,
    ) {
        instance.export("compile_pdf").call(
            resultPtr,
            contextPtr,
            fontsPtr,
            stdlibPtr,
            mainLen,
            mainPtr,
            nowMillisOrFlag,
            nowNanos.toLong(),
            optionsLen,
            optionsPtr,
        )
    }

    override fun files_cache(readerTicket: Long): Long {
        return instance.export("files_cache").call(readerTicket)[0]
    }

    override fun free_files_cache(cachePtr: Long) {
        instance.export("free_files_cache").call(cachePtr)
    }

    override fun reset_file(cachePtr: Long, idLen: Long, idPtr: Long) {
        instance.export("reset_file").call(cachePtr, idLen, idPtr)
    }

    override fun font_collection(
        includeSystem: Int,
        includeEmbedded: Int,
        fontPathsLen: Long,
        fontPathsPtr: Long
    ): Long {
        return instance.export("font_collection").call(
            includeSystem.toLong(),
            includeEmbedded.toLong(),
            fontPathsLen,
            fontPathsPtr
        )[0]
    }

    override fun free_font_collection(collectionPtr: Long) {
        instance.export("free_font_collection").call(collectionPtr)
    }

    override fun library(features: Int, inputsLen: Long, inputsPtr: Long): Long {
        return instance.export("library").call(
            features.toLong(),
            inputsLen,
            inputsPtr
        )[0]
    }

    override fun free_library(libraryPtr: Long) {
        instance.export("free_library").call(libraryPtr)
    }

    override fun with_inputs(
        resultPtr: Long,
        fontsPtr: Long,
        libraryPtr: Long,
        inputsLen: Long,
        inputsPtr: Long,
        closePrevious: Int,
    ) {
        instance.export("with_inputs").call(
            resultPtr,
            fontsPtr,
            libraryPtr,
            inputsLen,
            inputsPtr,
            closePrevious.toLong()
        )
    }


    override fun with_styles(
        resultPtr: Long,
        fontsPtr: Long,
        libraryPtr: Long,
        stylesLen: Long,
        stylesPtr: Long,
        closePrevious: Int,
        append: Int,
    ) {
        instance.export("with_styles").call(
            resultPtr,
            fontsPtr,
            libraryPtr,
            stylesLen,
            stylesPtr,
            closePrevious.toLong(),
            append.toLong()
        )
    }

    override fun ext_with_test_definitions(libraryPtr: Long, close_previous: Int): Long {
        return instance.export("ext_with_test_definitions").call(libraryPtr, close_previous.toLong())[0]
    }

    override fun free_lazy_hash_library(lazyHashPtr: Long) {
        instance.export("free_lazy_hash_library").call(lazyHashPtr)
    }

    override fun clone(libraryPtr: Long): Long {
        return instance.export("clone").call(libraryPtr)[0]
    }

    override fun allocate_raw_string(): Long {
        return instance.export("allocate_raw_string").call()[0]
    }

    override fun free_raw_string(rawStringPtr: Long) {
        instance.export("free_raw_string").call(rawStringPtr)
    }

    override fun allocate_ptr_for_raw_string(size: Long): Long {
        return instance.export("allocate_ptr_for_raw_string").call(size)[0]
    }

    override fun evict_cache(maxAge: Long) {
        instance.export("evict_cache").call(maxAge)
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
        instance.export("precompile_paged").call(
            result,
            context,
            fonts,
            stdlib,
            main_len,
            main_ptr,
            now_millis_or_flag,
            now_nanos.toLong()
        )
    }

    override fun render_svg(result: Long, document: Long, from: Int, to: Int) {
        instance.export("render_svg").call(result, document, from.toLong(), to.toLong())
    }

    override fun render_png(
        result: Long,
        document: Long,
        from: Int,
        to: Int,
        ppi: Double
    ) {
        instance.export("render_png").call(
            result,
            document,
            from.toLong(),
            to.toLong(),
            java.lang.Double.doubleToRawLongBits(ppi)
        )
    }

    override fun render_pdf(
        result: Long,
        document: Long,
        context: Long,
        options_len: Long,
        options_ptr: Long
    ) {
        instance.export("render_pdf").call(
            result,
            document,
            context,
            options_len,
            options_ptr
        )
    }

    override fun free_paged_document(ptr: Long) {
        instance.export("free_paged_document").call(ptr)
    }

    override fun enforce_title(documentPtr: Long, titleLen: Long, titlePtr: Long) {
        instance.export("enforce_title").call(documentPtr, titleLen, titlePtr)
    }
}


fun ChicoryTypstCore(
    bytes: WasmModule,
    wasiOptions: WasiOptions = WasiOptions.builder()
        .inheritSystem()
        .withEnvironment("RUST_BACKTRACE", "full")
        .withEnvironment("RUST_LIB_BACKTRACE", "1")
        .withDirectory("/", Paths.get("/"))
        .build(),
    compiledMachine: Boolean = true
) = TypstCore { reader ->
    val instance = TypstChicoryInstance(
        bytes,
        wasiOptions,
        compiledMachine = compiledMachine
    ) { ticket, coordinates -> reader(ticket)(coordinates) }
    val memory = ChicoryMemoryInterface(instance)
    val engine = ChicoryTypstDriver(instance)
    engine.boot(longArrayOf())
    Pair(engine, memory)
}

val MODULE = Parser.parse(
    ChicoryTypstDriver::class.java.classLoader.getResourceAsStream("typst-shared.wasm")!!
)

fun ChicoryTypstCore(
    wasiOptions: WasiOptions = WasiOptions.builder().inheritSystem().withEnvironment("RUST_BACKTRACE", "full").build(),
) = ChicoryTypstCore(MODULE, wasiOptions)

//fun main() {
//    val runtime = ChicoryTypstCore()
//    println(
//        runtime.formatSource(
//            """
//            #figure(
//              image("glacier.jpg", width: 70%),
//              caption: [
//                _Glaciers_ form an important part
//                of the earth's climate system.
//              ],
//            )
//        """.trimIndent(), 20, 4
//        )
//    )
//}
