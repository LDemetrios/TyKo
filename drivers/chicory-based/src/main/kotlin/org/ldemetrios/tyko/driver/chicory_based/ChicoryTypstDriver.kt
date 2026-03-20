package org.ldemetrios.tyko.driver.chicory_based

import com.dylibso.chicory.runtime.ExportFunction
import com.dylibso.chicory.runtime.HostFunction
import com.dylibso.chicory.runtime.Instance
import com.dylibso.chicory.runtime.Instance.builder
import com.dylibso.chicory.runtime.Machine
import com.dylibso.chicory.runtime.Store
import com.dylibso.chicory.wasi.WasiOptions
import com.dylibso.chicory.wasi.WasiPreview1
import com.dylibso.chicory.wasm.Parser
import com.dylibso.chicory.wasm.WasmModule
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.Base64
import java.util.function.Function
import com.dylibso.chicory.wasm.types.FunctionType
import com.dylibso.chicory.wasm.types.ValType
import org.ldemetrios.tyko.driver.api.MemoryInterface
import org.ldemetrios.tyko.driver.api.TypstCore
import org.ldemetrios.tyko.driver.api.TypstDriver
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.resolveJavaObject
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

internal const val PREFIX = "org.ldemetrios.tyko.driver.chicory_based.TypstSharedMachine"
private const val GENERATED_META_RESOURCE = "org/ldemetrios/tyko/driver/chicory_based/TypstShared.meta"

internal fun TypstChicoryInstance(
    options: WasiOptions,
    readFileByReaderTicket: (ticket: Long, coordinates: String) -> String,
    invokeNativeFunctionByTicket: (ticket: Long, session: Long, args: String, context: String) -> String,
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
    val invokeNativeFunction = HostFunction(
        "env", "invoke_native_function_by_ticket",
        FunctionType.of(
            listOf(ValType.I32, ValType.I64, ValType.I64, ValType.I32, ValType.I64, ValType.I32, ValType.I64),
            listOf()
        ),
    ) { instance, args ->
        val resultPtr = args[0]
        val ticket = args[1]
        val valuesLen = args[2]
        val valuesPtr = args[3]
        val contextLen = args[4]
        val contextPtr = args[5]
        val session = args[6]
        val values = instance.memory().readString(valuesPtr.toInt(), valuesLen.toInt())
        val context = instance.memory().readString(contextPtr.toInt(), contextLen.toInt())
        val result = invokeNativeFunctionByTicket(ticket, session, values, context)
        val resultBytes = result.toByteArray()
        val ptr = instance.export("allocate_ptr_for_raw_string").apply(resultBytes.size.toLong())[0]
        instance.memory().write(ptr.toInt(), resultBytes)
        instance.memory().writeLong(resultPtr.toInt(), resultBytes.size.toLong())
        instance.memory().writeLong(resultPtr.toInt() + 8, ptr)
        longArrayOf()
    }

    val jvmObjectToString = HostFunction(
        "env", "jvm_object_to_string",
        FunctionType.of(
            listOf(ValType.I32, ValType.I64, ValType.I64),
            listOf()
        ),
    ) { instance, args ->
        val (result_ptr, session, id) = args;
        val result_v = resolveJavaObject(session, id).toString()
        val resultBytes = result_v.toByteArray()
        val ptr = instance.export("allocate_ptr_for_raw_string").apply(resultBytes.size.toLong())[0]
        instance.memory().write(ptr.toInt(), resultBytes)
        instance.memory().writeLong(result_ptr.toInt(), resultBytes.size.toLong())
        instance.memory().writeLong(result_ptr.toInt() + 8, ptr)
        longArrayOf()
    }

    val jvmObjectGetClass = HostFunction(
        "env", "jvm_object_get_class",
        FunctionType.of(
            listOf(ValType.I32, ValType.I64, ValType.I64),
            listOf()
        ),
    ) { instance, args ->
        val (result_ptr, session, id) = args;
        val result_v = resolveJavaObject(session, id)?.javaClass?.descriptorString() ?: "null"
        val resultBytes = result_v.toByteArray()
        val ptr = instance.export("allocate_ptr_for_raw_string").apply(resultBytes.size.toLong())[0]
        instance.memory().write(ptr.toInt(), resultBytes)
        instance.memory().writeLong(result_ptr.toInt(), resultBytes.size.toLong())
        instance.memory().writeLong(result_ptr.toInt() + 8, ptr)
        longArrayOf()
    }

    val jvmObjectHashCode = HostFunction(
        "env", "jvm_object_hash_code",
        FunctionType.of(
            listOf(ValType.I64, ValType.I64),
            listOf(ValType.I32)
        )
    ) { instance, args ->
        val (session, id) = args;
        val result_v = resolveJavaObject(session, id).hashCode()
        longArrayOf(result_v.toLong())
    }

    val jvmObjectEquals = HostFunction(
        "env", "jvm_object_equals",
        FunctionType.of(
            listOf(ValType.I64, ValType.I64, ValType.I64, ValType.I64),
            listOf(ValType.I32)
        )
    ) { instance, args ->
        val (lSession, lId, rSession, rId) = args;
        val result_v = resolveJavaObject(lSession, lId) == (resolveJavaObject(rSession, rId))
        longArrayOf(if (result_v) 1L else 0L)
    }

    val store = Store()
    store.addFunction(readByTicket)
    store.addFunction(printBacktrace)
    store.addFunction(downloadUrl)
    store.addFunction(invokeNativeFunction)
    store.addFunction(jvmObjectToString)
    store.addFunction(jvmObjectGetClass)
    store.addFunction(jvmObjectHashCode)
    store.addFunction(jvmObjectEquals)
    val wasi = WasiPreview1.builder().withOptions(options).build();
    store.addFunction(*wasi.toHostFunctions())

    val builder = builder(COMPILED_MODULE)
    builder.withImportValues(store.toImportValues())
    builder.withMachineFactory(COMPILED_MACHINE_FACTORY)
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

internal class ChicoryMemoryInterface(private val instance: Instance) : MemoryInterface {
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

@TyKoFFIEntity
@Suppress("removal", "RedundantSuppression") // ThreadDeath is deprecated for removal, but not on 17
context(driver: ChicoryTypstDriver) internal fun ExportFunction.call(vararg arguments: Long): LongArray {
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

@TyKoFFIEntity
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
        session: Long,
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
            session,
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

    override fun compile_html(
        session: Long,
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
            session,
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
        session: Long,
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
            session,
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
        session: Long,
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
            session,
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
        session: Long,
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
            session,
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
        session: Long,
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
            session,
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
        session: Long,
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
            session,
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
        session: Long,
        resultPtr: Long,
        fontsPtr: Long,
        libraryPtr: Long,
        inputsLen: Long,
        inputsPtr: Long,
        closePrevious: Int,
    ) {
        instance.export("with_inputs").call(
            session,
            resultPtr,
            fontsPtr,
            libraryPtr,
            inputsLen,
            inputsPtr,
            closePrevious.toLong()
        )
    }


    override fun with_styles(
        session: Long,
        resultPtr: Long,
        fontsPtr: Long,
        libraryPtr: Long,
        stylesLen: Long,
        stylesPtr: Long,
        closePrevious: Int,
        append: Int,
    ) {
        instance.export("with_styles").call(
            session,
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

    override fun create_native_func_data(nameLen: Long, namePtr: Long, ticket: Long): Long {
        return instance.export("create_native_func_data").call(nameLen, namePtr, ticket)[0]
    }

    override fun free_native_func_data(nativeFuncDataPtr: Long) {
        instance.export("free_native_func_data").call(nativeFuncDataPtr)
    }

    override fun register_native_func(
        libraryPtr: Long,
        nativeFuncDataPtr: Long,
        nameLen: Long,
        namePtr: Long,
        closePrevious: Int,
    ): Long {
        return instance.export("register_native_func")
            .call(
                libraryPtr,
                nativeFuncDataPtr,
                nameLen,
                namePtr,
                closePrevious.toLong(),
            )[0]
    }

    override fun register_value(
        session: Long,
        resultPtr: Long,
        fonts: Long,
        libraryPtr: Long,
        nameLen: Long,
        namePtr: Long,
        valueLen: Long,
        valuePtr: Long,
        closePrevious: Int,
    ) {
        instance.export("register_value").call(
            session,
            resultPtr,
            fonts,
            libraryPtr,
            nameLen,
            namePtr,
            valueLen,
            valuePtr,
            closePrevious.toLong(),
        )
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
        session: Long,
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
            session,
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
        session: Long,
        result: Long,
        document: Long,
        context: Long,
        options_len: Long,
        options_ptr: Long
    ) {
        instance.export("render_pdf").call(
            session,
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

/**
 * Create a Chicory-based (pure-JVM, 32-bit) core for `TypstCompiler` or `TypstRuntime`.
 *
 * Basic differences of Chicory-based core:
 *  - Completely pure-JVM and cross-platform.
 *  - Runs in completely isolated environment. Paths should be wired explicitly via WasiOptions.
 *  - Can be in multiple, independent instances.
 *  - Apparently not thread-safe.
 *  - Limited to 32 bits of memory.
 *
 * @param wasiOptions Options of Chicory Instance VM. By default, set to [defaultWasiOptions].
 */
@OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
fun ChicoryTypstCore(
    wasiOptions: WasiOptions = defaultWasiOptions(),
) = TypstCore { readByTicket, nativeCallByTicket ->
    val instance = TypstChicoryInstance(
        wasiOptions,
        { ticket, coordinates -> readByTicket(ticket)(coordinates) },
        { ticket, session, args, context -> nativeCallByTicket(ticket)(session, args, context) },
    )
    val memory = ChicoryMemoryInterface(instance)
    val engine = ChicoryTypstDriver(instance)
    engine.boot(longArrayOf())
    Pair(engine, memory)
}

private val COMPILED_MODULE by lazy { compiledTypstModule() }

@OptIn(TyKoFFIEntity::class)
private fun compiledTypstModule(): WasmModule {
    val stream = ChicoryTypstDriver::class.java.classLoader.getResourceAsStream(GENERATED_META_RESOURCE)
        ?: error("Missing generated resource: $GENERATED_META_RESOURCE")
    return stream.use(Parser::parse)
}

@OptIn(TyKoFFIEntity::class)
private val COMPILED_MACHINE_FACTORY: Function<Instance, Machine> = Function { instance ->
    val machineClass = Class.forName(PREFIX, true, ChicoryTypstDriver::class.java.classLoader)
    val constructor = machineClass.getDeclaredConstructor(Instance::class.java)
    constructor.newInstance(instance) as Machine
}

/**
 * Makes an attempt to guess typical location for Typst's package cache.
 */
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

/**
 * Default WASI options:
 *
 * - binds System's `in`, `out` and `err` to ones of the WASI VM.
 * - binds VM's `/packages` path to your system's regular package location
 */
fun defaultWasiOptions(): WasiOptions = WasiOptions.builder()
    .inheritSystem()
    .withEnvironment("RUST_BACKTRACE", "full")
    .withEnvironment("RUST_LIB_BACKTRACE", "1")
    .withDirectory("/packages", defaultPackagesHostPath())
    .build()
