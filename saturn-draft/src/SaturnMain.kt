package org.ldemetrios.saturnDraft

import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.model.t
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.nio.file.Path
import kotlin.collections.map
import kotlin.io.path.*

val clazz = Class.forName("org.ldemetrios.saturnDraft.ExportKt")


fun main(vararg args: String) {
    val delay = args.getOrNull(0)?.toLong() ?: 2000L
    val world = ResettableFSWorld(Path("."), "/main.typ")
    val compiler = WorldBasedTypstCompiler(sharedLib, world)

    var loaded = clazz
        .declaredMethods
        .filter { it.annotations.any { it is SaturnExport } }
        .filter {
            val static = Modifier.isStatic(it.modifiers)
            val public = Modifier.isPublic(it.modifiers)
            if (!static) println("${it.toString().eliminateFullNames()}, marked as @SaturnExport, is not static and therefore can't be invoked")
            if (!public) println("${it.toString().eliminateFullNames()}, marked as @SaturnExport, is not public and therefore can't be accessed")
            val anyDynamic = it.parameterTypes.any { TDynamic::class.java.isAssignableFrom(it) }
            if (anyDynamic) println("${it.toString().eliminateFullNames()}, marked as @SaturnExport, contains dynamic argument types and therefore can't be invoked")
            static && public
        }
        .groupBy { it.name }
        .filter { (k, v) ->
            if (v.size > 1) println("$k refers to more than one exported function: ${v.map<Method?, String> { it.toString().eliminateFullNames() }}")
            v.size == 1
        }
        .mapValues { it.value[0] }

    while (true) {
        compiler.reset()
        println("=========")

        try {
            val num = ((compiler.query(TLabel("saturn-import-num".t))[0] as TMetadata<*>).value as TInt).intValue
            for (idx in 0 until num) {
                val request = compiler.query(TLabel("saturn-import-$idx".t))[0]
                if (request !is TMetadata<*>) {
                    println("Can't satisfy request $idx  : queried value is not a `metadata`")
                    continue
                }
                val requestValue = request.value
                if (requestValue !is TArray<*> || requestValue.size != 2) {
                    println("Can't satisfy request $idx  : queried value in metadata is not an `array` of size 2")
                    continue
                }
                val func = requestValue[0]
                val args = requestValue[1]
                if (func !is TStr) {
                    println("Can't satisfy request $idx  : func is not a `str`")
                    continue
                }
                val method = loaded[func.strValue]
                if (method == null) {
                    println("Can't satisfy request $idx  : no registered method $func")
                    continue
                }
                if (args !is TArguments<*>) {
                    println("Can't satisfy request $idx  : args is not `arguments`")
                    continue
                }

                val result = try {
                    val params = method.parameterTypes
                    try {
                        val evaled = if (params.size == 1 && TArguments::class.java.isAssignableFrom(params[0])) {
                            // Call with arguments
                            method.invoke(null, args)
                        } else {
                            // Call with spread arguments
                            // TODO will be more Typst-like when full support for NativeFuncData is available for Kotlin functions
                            method.invoke(null, *args.positional.toTypedArray<TValue>())
                        }
                        (evaled as TValue).repr()
                    } catch (e: InvocationTargetException) {
                        throw e.cause!!
                    }
                } catch (e: RuntimeException) {
                    val sb = StringWriter()
                    e.printStackTrace(PrintWriter(sb))
                    "panic(" + TStr(sb.toString()).repr() + ")"
                } catch (e: AssertionError) {
                    e.printStackTrace()
                    continue
                }

                File("saturn-output/$idx.typc").run {
                    parentFile.mkdirs()
                    writeText(result)
                }
            }
        } catch (e: TypstCompilerException) {
            println("File doesn't compile:")
            e.printStackTrace(System.out)
        } catch (e: RuntimeException) {
            println("Invalid run")
            e.printStackTrace(System.out)
        }
        Thread.sleep(delay)
        break
    }
    @Suppress("UNREACHABLE_CODE")
    compiler.close()
}

private fun String.eliminateFullNames(): String = replace("org.ldemetrios.tyko.model.", "")

class ResettableFSWorld(val root: Path, var main: String) : World {
    override fun library(): StdlibProvider = object : StdlibProvider.Standard {
        override val features: List<Feature> get() = listOf(Feature.Html)
        override val inputs: TDictionary<TValue> get() = TDictionary("saturn-run" to true.t)
    }

    override fun file(file: FileDescriptor): RResult<ByteArray, FileError> {
        println(file)
        return if (file.pack == null) {
            val path = root.resolve(file.path.removePrefix("/"))
            if (path.isDirectory()) RResult.Err(FileError.IsDirectory)
            try {
                RResult.Ok(path.readBytes())
            } catch (e: IOException) {
                RResult.Err(FileError.NotFound(file.path))
            }
        } else TODO()
    }

    override fun mainFile(): FileDescriptor {
        return FileDescriptor(null, if (main.startsWith("/")) main else "/$main")
    }

    override fun now(): WorldTime = WorldTime.System

    override val autoManageCentral: Boolean = true
}
