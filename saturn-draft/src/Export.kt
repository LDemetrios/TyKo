package org.ldemetrios.saturnDraft

import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.model.TDictionary
import org.ldemetrios.tyko.model.deg
import org.ldemetrios.tyko.model.t
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.valueOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.util.renderError
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SaturnExport

@SaturnExport
fun eval_kt(code: TStr): TDictionary<*> {
    val out = System.out
    val err = System.err
    val imports = code.strValue.lines().takeWhile { it.startsWith("import") }

    val script = """
        import java.io.*
        import org.ldemetrios.tyko.compiler.*
        import org.ldemetrios.tyko.ffi.TypstSharedLibrary
        import org.ldemetrios.tyko.model.*
        import org.ldemetrios.tyko.operations.*
        ${imports.joinToString("\n")}

        val __OUT_INTERCEPTER = ByteArrayOutputStream()
        val __ERR_INTERCEPTER = ByteArrayOutputStream()
        System.setOut(PrintStream(__OUT_INTERCEPTER))
        System.setErr(PrintStream(__ERR_INTERCEPTER))

        try {
            val result = run {

                ${code.strValue.lines().drop(imports.size).joinToString("\n")}

            }
            1 to Triple(result, __OUT_INTERCEPTER.toByteArray(), __ERR_INTERCEPTER.toByteArray())
        } catch (e: Exception) {
            0 to Triple(e, __OUT_INTERCEPTER.toByteArray(), __ERR_INTERCEPTER.toByteArray())
        }
    """

    val scriptingHost = BasicJvmScriptingHost()

    val compilationConfig = ScriptCompilationConfiguration {
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
    }

    val evaluationConfig = ScriptEvaluationConfiguration {}

    val result = scriptingHost.eval(script.toScriptSource(), compilationConfig, evaluationConfig)

    System.setOut(out)
    System.setErr(err)

    return when (val it = result.valueOrNull()?.returnValue) {
        is ResultValue.Value -> {
            val (exCode, res) = it.value as Pair<Int, Triple<*, ByteArray, ByteArray>>
            when (exCode) {
                0 -> TDictionary("error" to String(res.third).t, "output" to String(res.second).t, "throwable" to run {
                    val sw = ByteArrayOutputStream()
                    (res.first as Throwable).printStackTrace(PrintStream(sw))
                    String(sw.toByteArray()).t
                })

                1 -> TDictionary(
                    "error" to String(res.third).t,
                    "output" to String(res.second).t,
                    "value" to when (val v = res.first) {
                        null, Unit -> TNone
                        is TValue -> v
                        else -> throw IllegalStateException("Incorrect return type ${v.javaClass}")
                    }
                )

                else -> TODO(exCode.toString())
            }
        }

        is ResultValue.Error -> TDictionary("failure" to it.renderError().t)
        ResultValue.NotEvaluated -> TODO("not-eval??")
        is ResultValue.Unit -> TODO("unit??")
        null -> TDictionary("CE" to TArray(result.reports.map { it.render().t }))
    }
}

@SaturnExport
fun add(x: TInt, y: TInt): TInt {
    return TInt(x.intValue + y.intValue)
}

@SaturnExport
fun sub(x: TInt, y: TInt): TInt {
    return TInt(x.intValue - y.intValue)
}

val colorMap = mapOf(
    "bool" to "ffedc1",
    "relative" to "ffedc1",
    "length" to "ffedc1",

    "none" to "ffcbc4",
    "auto" to "ffcbc4",

    "alignment" to "a6eaff",

    "bytes" to "f9dfff",
    "array" to "f9dfff",
    "dictionary" to "f9dfff",

    "content" to "a6ebe6",
).mapValues { TRgb("#${it.value}".t) } +
        listOf(
            "color", "gradient", "stroke"
        ).associateWith {
            TLinearGradient(
                TArray(listOf("#7cd5ff", "#a6fbca", "#fff37c", "#ffa49d").map { TRgb(it.t) }),
                angle = (-7).deg,
            )
        }

@SaturnExport
fun highlightTypdoc(content: TRaw): TContent {
    val text = content.text.strValue.lines()

    val first = text.first()
    val last = text.last()
    val mid = text.subList(1, text.size - 1)

    val result = mutableListOf<TContent>()
    result.add(TRaw(first.t))
    result.add(TLinebreak())
    for (line in mid) {
        val ty = if (":" in line) {
            val (name, type) = line.split(":").map { it.trim() }
            result.add(TRaw("    $name: ".t))
            type
        } else {
            result.add(TRaw("    ".t))
            line
        }
        val vars = ty
            .removeSuffix(",")
            .split("|")
            .map { it.trim() }
            .map { THighlight(TRaw(it.t), fill = colorMap[it] ?: TNone) }
        for (vari in vars.subList(0, vars.size - 1)) {
            result.add(vari)
            result.add(TRaw("|".t))
        }
        result.add(vars.last())
        result.add(TRaw(",".t))
        result.add(TLinebreak())
    }
    result.add(TRaw(last.t))
    return TSequence(TArray(result))
}
