package org.ldemetrios.tyko.driver.chicory

import com.dylibso.chicory.runtime.ExportFunction
import com.dylibso.chicory.runtime.HostFunction
import com.dylibso.chicory.runtime.Instance
import com.dylibso.chicory.runtime.Instance.builder
import com.dylibso.chicory.runtime.Store
import com.dylibso.chicory.wasi.WasiOptions
import com.dylibso.chicory.wasi.WasiPreview1
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.Base64
import com.dylibso.chicory.wasm.types.FunctionType
import com.dylibso.chicory.wasm.types.ValType
import org.ldemetrios.tyko.driver.api.MemoryInterface
import org.ldemetrios.tyko.driver.api.TypstCore
import org.ldemetrios.tyko.driver.api.TypstDriver
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.collections.map

fun <T, A : T, B : T> B.tryTransforming(func: (B) -> A) = try {
    func(this)
} catch (e: VirtualMachineError) {
    throw e
} catch (e: Throwable) {
    this
}

fun sanitizeStackTrace(stackTrace: Array<StackTraceElement>, instance: Instance): Array<StackTraceElement> =
    stackTrace.tryTransforming {
        it.map {
            val idx = it.methodName.split("_", ".").last().toIntOrNull()
            if (idx != null && it.className.startsWith(PREFIX)) {
                StackTraceElement(
                    "Chicory",
                    it.moduleName,
                    it.moduleVersion,
                    it.className.drop(PREFIX.length),
                    instance.functionName(idx) ?: idx.toString(),
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
        FunctionNames.nameOf(idx)
    }
}

internal const val PREFIX = "org.ldemetrios.tyko.driver.chicory.TypstSharedMachine"

fun TypstChicoryInstance(
    options: WasiOptions,
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

    val downloadUrl = HostFunction(
        "env", "download_url",
        FunctionType.of(
            listOf(ValType.I32, ValType.I64, ValType.I32),
            listOf()
        ),
    ) { instance, args ->
        val (resultPtr, reqLen, reqPtr) = args
        val reqJson = instance.memory().readString(reqPtr.toInt(), reqLen.toInt())
        val responseJson = try {
            buildDownloadResponse(reqJson)
        } catch (e: Throwable) {
            buildJsonObject { put("Err", JsonPrimitive(e.message ?: "download_url failed")) }.toString()
        }
        val responseBytes = responseJson.toByteArray()
        val ptr = instance.export("allocate_ptr_for_raw_string").apply(responseBytes.size.toLong())[0]
        instance.memory().write(ptr.toInt(), responseBytes)
        instance.memory().writeLong(resultPtr.toInt(), responseBytes.size.toLong())
        instance.memory().writeLong(resultPtr.toInt() + 8, ptr)
        longArrayOf()
    }
    val store = Store()
    store.addFunction(readByTicket)
    store.addFunction(printBacktrace)
    store.addFunction(downloadUrl)
    val wasi = WasiPreview1.builder().withOptions(options).build();
    store.addFunction(*wasi.toHostFunctions())

    val builder = builder(COMPILED_MODULE.wasmModule())
    builder.withImportValues(store.toImportValues())
    builder.withMachineFactory(COMPILED_MODULE.machineFactory())
    val instance = builder.build()
    store.register("typst_shared", instance)
    return instance
}

private val downloadJson = Json { ignoreUnknownKeys = true }

private fun buildDownloadResponse(reqJson: String): String {
    val parsed = downloadJson.parseToJsonElement(reqJson).jsonObject
    val url = parsed["url"]?.jsonPrimitive?.content ?: error("download_url: missing url")
    val headersObj = parsed["headers"]?.jsonObject
    val headers = headersObj?.mapValues { it.value.jsonPrimitive.content } ?: emptyMap()

    val client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(30))
        .build()
    val requestBuilder = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
    headers.forEach { (key, value) ->
        requestBuilder.header(key, value)
    }
    val response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray())
    val status = response.statusCode()
    val responseHeaders = response.headers().map().mapValues { it.value.joinToString(",") }
    val bodyBase64 = Base64.getEncoder().encodeToString(response.body())

    val ok = buildJsonObject {
        put("status", JsonPrimitive(status))
        put("headers", buildJsonObject {
            responseHeaders.forEach { (key, value) -> put(key, JsonPrimitive(value)) }
        })
        put("body", JsonPrimitive(bodyBase64))
    }
    return buildJsonObject { put("Ok", ok) }.toString()
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

    override fun eval_main(
        resultPtr: Long,
        contextPtr: Long,
        stdlibPtr: Long,
        fontsPtr: Long,
        mainLen: Long,
        mainPtr: Long,
        nowMillisOrFlag: Long,
        nowNanos: Int,
    ) {
        instance.export("eval_main").call(
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

    override fun resolve_preview_package(resultPtr: Long, idLen: Long, idPtr: Long) {
        instance.export("resolve_preview_package").call(resultPtr, idLen, idPtr)
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

    override fun library(features: Int): Long {
        return instance.export("library").call(
            features.toLong(),
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
    wasiOptions: WasiOptions = defaultWasiOptions(),
) = TypstCore { reader ->
    val instance = TypstChicoryInstance(
        wasiOptions,
    ) { ticket, coordinates -> reader(ticket)(coordinates) }
    val memory = ChicoryMemoryInterface(instance)
    val engine = ChicoryTypstDriver(instance)
    engine.boot(longArrayOf())
    Pair(engine, memory)
}

private val COMPILED_MODULE = TypstShared()
val MODULE = COMPILED_MODULE.wasmModule()

fun defaultPackagesHostPath(): Path {
    val os = System.getProperty("os.name").lowercase()
    val home = System.getProperty("user.home")
    val path = when {
        os.contains("mac") -> Paths.get(home, "Library", "Caches", "typst", "packages")
        os.contains("win") -> {
            val local = System.getenv("LOCALAPPDATA")
            if (local != null) Paths.get(local, "typst", "packages")
            else Paths.get(home, "AppData", "Local", "typst", "packages")
        }

        else -> {
            val xdg = System.getenv("XDG_CACHE_HOME")
            if (xdg != null) Paths.get(xdg, "typst", "packages")
            else Paths.get(home, ".cache", "typst", "packages")
        }
    }
    Files.createDirectories(path)
    return path
}

fun defaultWasiOptions(): WasiOptions = WasiOptions.builder()
    .inheritSystem()
    .withEnvironment("RUST_BACKTRACE", "full")
    .withEnvironment("RUST_LIB_BACKTRACE", "1")
    .withDirectory("/packages", defaultPackagesHostPath())
    .build()

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
