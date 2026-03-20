package org.ldemetrios.tyko.tests

import io.kotest.core.spec.style.FreeSpec
import org.intellij.lang.annotations.Language
import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.conversion.java2typst
import org.ldemetrios.tyko.conversion.typst2java
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.runtime.CreateSessionMode
import org.ldemetrios.tyko.runtime.TypstRuntime
import java.lang.constant.ClassDesc
import java.lang.constant.DirectMethodHandleDesc
import java.lang.constant.MethodHandleDesc
import java.lang.constant.MethodTypeDesc
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

object ReflectionFixtures {
    @JvmField
    var shared: Int = 7

    @JvmStatic
    fun ints(values: IntArray): IntArray = values
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

private fun reflectiveCall(
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

@OptIn(TyKoInternalApi::class)
class JvmObjectsTest : FreeSpec({
    "Basic test" {
        val runtime = TypstRuntime { ChicoryTypstCore() }
        val fonts = runtime.fontCollection()
        val library = runtime.library(Feature.all())
            .withInputs(TDict(mapOf("stdout" to TJvmObject(System.out))), fonts)
            .orThrow()
        val fs = runtime.fileContext(
            mapOf(
                "/main.typ" to """
                    #let stdout = sys.inputs.stdout
                    
                    #stdout.to-string()
                    
                    #stdout.get-class()
                """.trimIndent()
            )
        )
        println(runtime.compileHtml(fs, library = library, fonts = fonts))
    }

    "Reflection test" {
        val runtime = TypstRuntime { ChicoryTypstCore() }
        val fonts = runtime.fontCollection()
        val func = runtime.nativeFuncData(
            NativeFuncDataDescriptor(
                name = "invoke-java",
                title = "invoke-java",
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
                """.trimIndent(),
                keywords = listOf("java", "jvm", "ffi", "reflection", "method-handle"),
                contextual = false,
                params = listOf(
                    NativeParamDescriptor(
                        name = "signature",
                        docs = "The JVM member signature in `owner#member:descriptor` format.",
                        input = NativeCastInfo.type("str"),
                    ),
                    NativeParamDescriptor(
                        name = "refkind",
                        docs = "The JVM reference kind number.",
                        input = NativeCastInfo.type("int"),
                    ),
                    NativeParamDescriptor(
                        name = "args",
                        docs = "Arguments for the receiver/member invocation.",
                        input = NativeCastInfo.any(),
                        variadic = true,
                        required = false,
                    ),
                    NativeParamDescriptor(
                        name = "lookup",
                        docs = "Optional `MethodHandles.Lookup` to resolve the handle with.",
                        input = NativeCastInfo.type("jvm-object"),
                        positional = false,
                        named = true,
                        required = false,
                    ),
                ),
                returns = NativeCastInfo.any(),
            )
        ) { args, _ ->
            val lookup = args.named["lookup"] as TJvmObject?
            val signature = args.positional[0] as TStr
            val refkind = args.positional[1] as TInt
            val params = args.positional.drop(2)
            try {
                RResult.Ok(
                    reflectiveCall(
                        lookup?.value?.let { it as MethodHandles.Lookup } ?: MethodHandles.lookup(),
                        signature.value,
                        DirectMethodHandleDesc.Kind.valueOf(refkind.value.toInt()),
                        params
                    )
                )
            } catch (e: Throwable) {
                val reversed = e.stackTrace.reversed()
                val compare = Thread.currentThread().stackTrace.reversed()
                val overlap = minOf(reversed.size, compare.size)
                val common = (0 until overlap).takeWhile { reversed[it] == compare[it] }.size
                val errorString = e.toString() + "\n" + reversed.drop(common).reversed().joinToString("\n")
                RResult.Err(errorString)
            }
        }
        val library = try {
            runtime.library(Feature.all())
                .extendWithKotlinTestDefinitions(runtime, fonts)
                .withNativeFunction("invoke-java", func)
        } finally {
            func.release()
        }

        @Language("typ")
        val source = """
            #let getfield = 1
            #let getstatic = 2
            #let putfield = 3
            #let putstatic = 4
            #let invokevirtual = 5
            #let invokestatic = 6
            #let constructor = 8
            
            #let point = invoke-java("java.awt.Point#<init>:(II)V", constructor, 3, 4)
            #test(invoke-java("java.lang.Integer#sum:(II)I", invokestatic, 20, 22), 42)
            #test(invoke-java("java.lang.Math#max:(DD)D", invokestatic, 1.5, 2.25), 2.25)
            #test(invoke-java("java.awt.Point#x:I", getfield, point), 3)
            #test(invoke-java("java.awt.Point#x:I", putfield, point, 9), none)
            #test(invoke-java("java.awt.Point#x:I", getfield, point), 9)
            #test(invoke-java("java.awt.Point#move:(II)V", invokevirtual, point, 12, 13), none)
            #test(invoke-java("java.awt.Point#y:I", getfield, point), 13)
            #test(invoke-java("org.ldemetrios.tyko.tests.ReflectionFixtures#shared:I", getstatic), 7)
            #test(invoke-java("org.ldemetrios.tyko.tests.ReflectionFixtures#shared:I", putstatic, 11), none)
            #test(invoke-java("org.ldemetrios.tyko.tests.ReflectionFixtures#shared:I", getstatic), 11)
            
            #let stdout = invoke-java("java.lang.System#out:Ljava/io/PrintStream;", getstatic)
            #let println(x) = {
                invoke-java("java.io.PrintStream#println:(Ljava/lang/String;)V", invokevirtual, stdout, repr(x))
            }
            #println("Hello, world")
            #println("Hello, world")
        """.trimIndent()

        val fs = runtime.fileContext(
            mapOf(
                "/main.typ" to source
            )
        )
        println(runtime.compileHtml(fs, library = library, fonts = fonts, sessionMode = CreateSessionMode.Yes))
    }
})
