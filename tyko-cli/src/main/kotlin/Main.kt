package org.ldemetrios

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.NativeCastInfo
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.SyntaxMode
import org.ldemetrios.tyko.compiler.TypstCompilerException
import org.ldemetrios.tyko.conversion.java2typst
import org.ldemetrios.tyko.conversion.typst2java
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TJvmObject
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TTiling
import org.ldemetrios.tyko.model.TType
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.runtime.CreateSessionMode
import org.ldemetrios.tyko.runtime.LibrarySpecBuilder
import org.ldemetrios.tyko.runtime.TypstRuntime
import org.ldemetrios.tyko.runtime.buildWorldSpecification
import java.io.FileOutputStream
import java.io.PrintStream
import java.lang.constant.ClassDesc
import java.lang.constant.DirectMethodHandleDesc
import java.lang.constant.MethodHandleDesc
import java.lang.constant.MethodTypeDesc
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.createDirectories
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes
import kotlin.io.path.writeText
import kotlin.reflect.KType


fun main(args: Array<String>) {
    require(args.isNotEmpty() && args.size <= 2) {
        "Usage: tyko-cli <input.(typ|typc|typm)> [output-dir]"
    }

    val input = Path.of(args[0]).toAbsolutePath().normalize()
    require(Files.exists(input) && Files.isRegularFile(input)) {
        "Input file does not exist or is not a regular file: $input"
    }

    val mode = syntaxModeOf(input)
    val projectRoot = input.parent ?: Path.of(".").toAbsolutePath().normalize()
    val mainFile = FileDescriptor(null, "/${input.fileName}")
    val outputDir = projectRoot

    outputDir.createDirectories()

    val runtime = createKvasirLikeRuntime()
    val fs = runtime.fileContextOf { descriptor ->
        resolveFile(runtime, projectRoot, mainFile, mode, descriptor)
    }
    try {
        val result = runtime.compilePdf(
            fs,
            main = mainFile,
            sessionMode = CreateSessionMode.Yes,
        )

        result.warnings.forEach { warning ->
            System.err.println("warning: ${warning.message}")
        }

        when (val output = result.output) {
            is RResult.Ok -> {
                val target = outputDir.resolve("${input.nameWithoutExtension}.pdf")
                target.writeBytes(output.value)
                println(target)
            }

            is RResult.Err -> throw TypstCompilerException.construct(output.error)
        }
    } finally {
        fs.release()
    }
}

private fun createKvasirLikeRuntime(): TypstRuntime = TypstRuntime(
    buildWorldSpecification {
        fonts {
            includeSystem = false
            includeEmbedded = true
        }
        library {
            fresh(Feature.all())
            this.defineUpcall()
        }
    }
) { ChicoryTypstCore() }

private fun resolveFile(
    runtime: TypstRuntime,
    projectRoot: Path,
    mainFile: FileDescriptor,
    mode: SyntaxMode,
    file: FileDescriptor,
): RResult<ByteArray, FileError> {
    return when (file.packageSpec) {
        null -> {
            val absolutePath = projectRoot.resolve(file.virtualPath.removePrefix("/")).normalize()
            when {
                !absolutePath.startsWith(projectRoot) -> RResult.Err(FileError.AccessDenied)
                !Files.exists(absolutePath) -> RResult.Err(FileError.NotFound(file.virtualPath))
                Files.isDirectory(absolutePath) -> RResult.Err(FileError.IsDirectory)
                else -> {
                    val bytes = absolutePath.readBytes()
                    RResult.Ok(if (file == mainFile) wrapMainContent(bytes, mode) else bytes)
                }
            }
        }

        else -> runtime.resolvePackage(file)
    }
}

private fun wrapMainContent(content: ByteArray, mode: SyntaxMode): ByteArray = when (mode) {
    SyntaxMode.Markup -> content
    SyntaxMode.Code -> "#{\n".encodeToByteArray() + content + "\n}".encodeToByteArray()
    SyntaxMode.Math -> "$\n".encodeToByteArray() + content + "\n$".encodeToByteArray()
}

private fun syntaxModeOf(path: Path): SyntaxMode = when (path.extension.lowercase()) {
    "typ" -> SyntaxMode.Markup
    "typc" -> SyntaxMode.Code
    "typm" -> SyntaxMode.Math
    else -> error("Unsupported input extension: .${path.extension}")
}


private fun LibrarySpecBuilder.defineUpcall() {
    func("define-class") {
        title = "Define Class"

        param("breakglass") {
            docs = "Break-glass ticket"
            input = NativeCastInfo.type("str")
            positional = false
            named = true
            required = false
        }

        param("name") {
            docs = "Class name"
            input = NativeCastInfo.type("str")
            positional = true
            named = false
            required = true
        }

        param("content") {
            docs = "Classfile bytes"
            input = NativeCastInfo.type("bytes")
            positional = true
            named = false
            required = true
        }

        returns(TType("jvm-object"))

        pureCatching {
            val name = it.positional[0] as TStr
            val content = it.positional[1] as TBytes
            val breakglass = it.named["breakglass"] as TStr?
            val clazz = BytesClassLoader.define(name.value, content.value)
            TJvmObject(clazz)
        }
    }

    func("lookup-in") {
        title = "Lookup in particular Class"

        param("breakglass") {
            docs = "Break-glass ticket"
            input = NativeCastInfo.type("str")
            positional = false
            named = true
            required = false
        }

        param("class") {
            docs = "The class"
            input = NativeCastInfo.type("jvm-object")
            positional = true
            named = false
            required = true
        }

        returns(TType("jvm-object"))

        pureCatching {
            val clazz = it.positional[0] as TJvmObject
            val breakglass = it.named["breakglass"] as TStr?
            val lookup = MethodHandles.privateLookupIn(clazz.value as Class<*>, MethodHandles.lookup())
            TJvmObject(lookup)
        }
    }

    func("upcall") {
        title = "Upcall"
        docs = """
                    Resolve and invoke a JVM member through method handles.
                    
                    Signature format: `owner#member:descriptor`
                    Examples:
                    - `java.lang.Integer#sum:(II)I`
                    - `java.awt.Point#x:I`
                    - `java.awt.Point#<init>:(II)V`
                    
                    Refkind values:
                    - `1` getter
                    - `2` static getter
                    - `3` setter
                    - `4` static setter
                    - `5` virtual
                    - `6` static
                    - `7` special
                    - `8` constructor
                    - `9` interface virtual
                """.trimIndent()
        keywords("java", "jvm", "ffi", "reflection", "method-handle")

        param("lookup") {
            docs = "Optional `MethodHandles.Lookup` to resolve the handle with."
            input = NativeCastInfo.type("jvm-object")
            positional = false
            named = true
            required = false
        }

        param("breakglass") {
            docs = "Break-glass ticket"
            input = NativeCastInfo.type("str")
            positional = false
            named = true
            required = false
        }
        param("refkind") {
            docs = "The JVM reference kind number."
            input = NativeCastInfo.type("int")
        }
        param("signature") {
            docs = "The JVM member signature in `owner#member:descriptor` format."
            input = NativeCastInfo.type("str")
        }

        param("args") {
            docs = "Arguments for the receiver/member invocation."
            input = NativeCastInfo.any()
            variadic = true
            required = false
        }

        returnsAny()

        pureCatching {
            val lookup = it.named["lookup"] as TJvmObject?
            val breakglass = it.named["breakglass"] as TStr?
            val refkind = it.positional[0] as TInt
            val signature = it.positional[1] as TStr
            val params = it.positional.drop(2)
            reflectiveCall(
                lookup?.value?.let { it as MethodHandles.Lookup } ?: MethodHandles.publicLookup(),
                signature.value,
                DirectMethodHandleDesc.Kind.valueOf(refkind.value.toInt()),
                params
            )
        }
    }
}

private data class ByteArrayW(val b: ByteArray) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is ByteArrayW -> false
        else -> b.contentEquals(other.b)
    }

    override fun hashCode(): Int = b.contentHashCode()
}

object BytesClassLoader : ClassLoader() {
    private val map = ConcurrentHashMap<ByteArrayW, Class<*>>()
    fun define(name: String, b: ByteArray): Class<*>? {
        return map.getOrPut(ByteArrayW(b)) { defineClass(name, b, 0, b.size) }
    }
}

private data class ParsedSignature(
    val owner: ClassDesc,
    val name: String,
    val descriptor: String,
)

private fun parseClassDesc(raw: String): ClassDesc {
    return if (raw.startsWith("[") || (raw.startsWith("L") && raw.endsWith(";")) || raw.length == 1) {
        ClassDesc.ofDescriptor(raw)
    } else {
        val split = raw.lastIndexOf('.')
        if (split == -1) {
            ClassDesc.of(raw)
        } else {
            ClassDesc.of(raw.substring(0, split), raw.substring(split + 1))
        }
    }
}

private fun parseSignature(signature: String): ParsedSignature {
    val ownerSplit = signature.indexOf('#')
    require(ownerSplit > 0 && ownerSplit < signature.length - 1) {
        "Expected signature `owner#member:descriptor`, got `$signature`"
    }
    val memberSplit = signature.indexOf(':', ownerSplit + 1)
    require(memberSplit > ownerSplit + 1 && memberSplit < signature.length - 1) {
        "Expected signature `owner#member:descriptor`, got `$signature`"
    }
    return ParsedSignature(
        owner = parseClassDesc(signature.substring(0, ownerSplit)),
        name = signature.substring(ownerSplit + 1, memberSplit),
        descriptor = signature.substring(memberSplit + 1),
    )
}

private fun buildHandleDesc(signature: String, kind: DirectMethodHandleDesc.Kind): MethodHandleDesc {
    val parsed = parseSignature(signature)
    return when (kind) {
        DirectMethodHandleDesc.Kind.CONSTRUCTOR -> {
            val descriptor = MethodTypeDesc.ofDescriptor(parsed.descriptor)
            MethodHandleDesc.ofConstructor(parsed.owner, *descriptor.parameterArray())
        }

        DirectMethodHandleDesc.Kind.GETTER,
        DirectMethodHandleDesc.Kind.SETTER,
        DirectMethodHandleDesc.Kind.STATIC_GETTER,
        DirectMethodHandleDesc.Kind.STATIC_SETTER -> {
            MethodHandleDesc.ofField(
                kind,
                parsed.owner,
                parsed.name,
                ClassDesc.ofDescriptor(parsed.descriptor),
            )
        }

        else -> {
            MethodHandleDesc.ofMethod(
                kind,
                parsed.owner,
                parsed.name,
                MethodTypeDesc.ofDescriptor(parsed.descriptor),
            )
        }
    }
}

fun reflectiveCall(
    lookup: MethodHandles.Lookup,
    value: String,
    valueOf: DirectMethodHandleDesc.Kind,
    params: List<TValue>
): TValue {
    val descriptor = buildHandleDesc(value, valueOf)
    val handle = descriptor.resolveConstantDesc(lookup) as MethodHandle
    val parameterTypes = handle.type().parameterArray()
    require(parameterTypes.size == params.size) {
        "Expected ${parameterTypes.size} arguments for `$value`, got ${params.size}"
    }
    val converted = params.mapIndexed { idx, param ->
        typst2java(param, parameterTypes[idx])
    }
    return java2typst(handle.invokeWithArguments(converted))
}
