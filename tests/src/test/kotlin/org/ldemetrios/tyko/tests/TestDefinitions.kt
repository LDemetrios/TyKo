package org.ldemetrios.tyko.tests

import org.ldemetrios.tyko.compiler.NativeCastInfo
import org.ldemetrios.tyko.compiler.NativeFuncDataDescriptor
import org.ldemetrios.tyko.compiler.NativeParamDescriptor
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.orThrow
import org.ldemetrios.tyko.compiler.outputOrThrow
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.model.TArgs
import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TBool
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TLabel
import org.ldemetrios.tyko.model.TMetadata
import org.ldemetrios.tyko.model.TNone
import org.ldemetrios.tyko.model.TRgbColor
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TStyles
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.repr
import org.ldemetrios.tyko.model.t
import org.ldemetrios.tyko.runtime.FontCollectionReference
import org.ldemetrios.tyko.runtime.Library
import org.ldemetrios.tyko.runtime.TypstRuntime
import java.math.BigDecimal
import java.math.RoundingMode


fun Library.extendWithKotlinTestDefinitions(runtime: TypstRuntime, fonts: FontCollectionReference): Library {
    var library = addValue("conifer", TRgbColor(0x9f.t, 0xEB.t, 0x52.t, 0xFF.t), fonts).orThrow()
        .addValue("forest", TRgbColor(0x43.t, 0xA1.t, 0x27.t, 0xFF.t), fonts).orThrow()

    fun addNativeFunction(
        name: String,
        descriptor: NativeFuncDataDescriptor,
        hostCall: (args: TArgs<TValue>, context: TStyles?) -> RResult<TValue, String>,
    ) {
        val data = runtime.nativeFuncData(descriptor, hostCall)
        try {
            library = library.addNativeFunction(name, data)
        } finally {
            data.release()
        }
    }

    addNativeFunction(
        "test",
        NativeFuncDataDescriptor(
            name = "test",
            title = "test",
            docs = "Assert values are equal.",
            keywords = listOf("test", "assert", "equal"),
            contextual = false,
            params = listOf(
                NativeParamDescriptor(
                    name = "values",
                    docs = "Values to compare.",
                    input = NativeCastInfo.any(),
                    positional = true,
                    named = false,
                    variadic = true,
                    required = false,
                ),
            ),
            returns = NativeCastInfo.type("none"),
        ),
    ) { args, _ ->
        val lhs = args.positional.value.getOrNull(0)
        val rhs = args.positional.value.getOrNull(1)
        if (lhs == null || rhs == null) {
            return@addNativeFunction RResult.Err("Assertion failed: expected two arguments")
        }

        val equal = typstEqual(lhs, rhs)

        if (!equal) {
            RResult.Err("Assertion failed: ${normalizeRepr(lhs.repr())} != ${normalizeRepr(rhs.repr())}")
        } else {
            RResult.Ok(TNone)
        }
    }

    addNativeFunction(
        "test-repr",
        NativeFuncDataDescriptor(
            name = "test-repr",
            title = "test-repr",
            docs = "Assert repr strings are equal.",
            keywords = listOf("test", "assert", "repr"),
            contextual = false,
            params = listOf(
                NativeParamDescriptor(
                    name = "values",
                    docs = "Values to compare by repr.",
                    input = NativeCastInfo.any(),
                    positional = true,
                    named = false,
                    variadic = true,
                    required = false,
                ),
            ),
            returns = NativeCastInfo.type("none"),
        ),
    ) { args, _ ->
        val lhs = args.positional.value.getOrNull(0)
        val rhs = args.positional.value.getOrNull(1)
        if (lhs == null || rhs == null) {
            return@addNativeFunction RResult.Err("Assertion failed: expected two arguments")
        }
        val lhsRepr = typstRepr(lhs)
        val rhsRepr = typstRepr(rhs)
        if (lhsRepr != rhsRepr) {
            RResult.Err("Assertion failed: $lhsRepr != $rhsRepr")
        } else {
            RResult.Ok(TNone)
        }
    }

    addNativeFunction(
        "print",
        NativeFuncDataDescriptor(
            name = "print",
            title = "print",
            docs = "Print test values.",
            keywords = listOf("print"),
            contextual = false,
            params = listOf(
                NativeParamDescriptor(
                    name = "values",
                    docs = "Values to print.",
                    input = NativeCastInfo.any(),
                    positional = true,
                    named = true,
                    variadic = true,
                    required = false,
                )
            ),
            returns = NativeCastInfo.type("none"),
        ),
    ) { args, _ ->
        val rendered = args.positional.value.joinToString(", ") { it.repr() }
        println("> $rendered")
        RResult.Ok(TNone)
    }

    addNativeFunction(
        "lines",
        NativeFuncDataDescriptor(
            name = "lines",
            title = "lines",
            docs = "Generates count lines with numbering.",
            keywords = listOf("lines", "numbering"),
            contextual = true,
            params = listOf(
                NativeParamDescriptor(
                    name = "values",
                    docs = "Arguments: count, optional numbering.",
                    input = NativeCastInfo.any(),
                    positional = true,
                    named = false,
                    variadic = true,
                    required = false,
                ),
            ),
            returns = NativeCastInfo.type("str"),
        ),
    ) { args, _ ->
        val count = (args.positional.value.getOrNull(0) as? TInt)?.value
            ?: return@addNativeFunction RResult.Err("lines: first argument must be int")
        if (count < 0) {
            return@addNativeFunction RResult.Err("lines: count must be non-negative")
        }
        val numbering = (args.positional.value.getOrNull(1) as? TStr)?.value ?: "A"
        val content = (1L..count).joinToString("\n") { n ->
            applyNumberingPattern(numbering, n)
        }
        RResult.Ok(TStr(content))
    }

    return library
}


private fun applyNumberingPattern(pattern: String, n: Long): String {
    return when (pattern) {
        "1" -> n.toString()
        "A" -> toAlpha(n, uppercase = true)
        "a" -> toAlpha(n, uppercase = false)
        "I" -> toRoman(n, uppercase = true)
        "i" -> toRoman(n, uppercase = false)
        else -> n.toString()
    }
}

private fun toAlpha(value: Long, uppercase: Boolean): String {
    if (value <= 0) return "0"
    var n = value
    val base = if (uppercase) 'A'.code else 'a'.code
    val out = StringBuilder()
    while (n > 0) {
        n -= 1
        val letter = (base + (n % 26).toInt()).toChar()
        out.append(letter)
        n /= 26
    }
    return out.reverse().toString()
}

private fun toRoman(value: Long, uppercase: Boolean): String {
    if (value <= 0) return "0"
    var n = value
    val numerals = listOf(
        1000L to "M",
        900L to "CM",
        500L to "D",
        400L to "CD",
        100L to "C",
        90L to "XC",
        50L to "L",
        40L to "XL",
        10L to "X",
        9L to "IX",
        5L to "V",
        4L to "IV",
        1L to "I",
    )
    val out = StringBuilder()
    for ((number, token) in numerals) {
        while (n >= number) {
            out.append(token)
            n -= number
        }
    }
    val result = out.toString()
    return if (uppercase) result else result.lowercase()
}

private val decimal = Regex("""-?\d+\.\d+(?:[eE][+-]?\d+)?""")

private fun normalizeRepr(repr: String): String {
    return decimal.replace(repr) { m ->
        runCatching {
            BigDecimal(m.value).setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
        }.getOrElse { m.value }
    }
}

private fun typstEqual(lhs: TValue, rhs: TValue): Boolean {
    val localRuntime = TypstRuntime { ChicoryTypstCore() }
    val source = "#metadata(${normalizeRepr(lhs.repr())} == ${normalizeRepr(rhs.repr())})<here>"
    val repr = localRuntime.query(TLabel("here".t)) {
        files {
            singleFile("/main.typ", source)
        }
    }.outputOrThrow().singleOrNull() as TMetadata<TBool>
    return repr.value.value
}

private fun typstRepr(value: TValue): String {
    val localRuntime = TypstRuntime { ChicoryTypstCore() }
    val source = "#metadata(repr(${normalizeRepr(value.repr())}))<here>"
    val repr = localRuntime.query(TLabel("here".t)) {
        files {
            singleFile("/main.typ", source)
        }
    }.outputOrThrow().singleOrNull() as TMetadata<TStr>
    return repr.value.value
}
