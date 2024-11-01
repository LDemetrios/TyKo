package org.ldemetrios.typst4k.dsl

import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.rt.deserializeTypstValue
import org.ldemetrios.typst4k.rt.repr
import org.ldemetrios.typst4k.withTmp
import java.io.Reader
import java.io.StringReader
import java.nio.file.Path
import kotlin.io.path.pathString

@PublishedApi
internal fun CommonConfig.composeQueryArgs() =
    Command("query")
        .optionalEntry("--root", queryConfig.root ?: root)
        .multiple("--input", (inputs + queryConfig.inputs).map { it.key + "=" + it.value })
        .optionalEntry("--font-path", queryConfig.fontPath ?: fontPath)
        .optionalEntry("--format", queryConfig.format)


@Suppress("RedundantUnitExpression")
@PublishedApi
internal inline fun <reified A : TValue> Typst.query(
    stdin: Reader?,
    input: String,
    selector: String,
    noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    val config = if (configer == null) this.config else {
        val comp = QueryConfig()
        comp.configer()
        this.config.copy(queryConfig = comp)
    }


    val res: TypstCompilerResult<String> = execute(
        forQueries,
        stdin,
        config.composeQueryArgs()
            .optionalArg(input)
            .optionalArg(selector)
    )

    return res.map {
        deserializeTypstValue<TArray<A>>(it)
    }
}

@Deprecated("Using raw selectors may produce unexpected behaviour")
inline fun <reified A: TValue> Typst.query(
    input: Path, selector: String, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(null, input.pathString, selector, configer)
}

inline fun <reified A: TValue> Typst.query(
    input: Path, selector: TSelector, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(null,  input.pathString, selector.repr(), configer)
}

inline fun <reified A: TValue> Typst.query(
    input: Path, selector: TLabel, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(null,  input.pathString, TLabelSelector(selector).repr(), configer)
}


@Deprecated("Using raw selectors may produce unexpected behaviour")
inline fun <reified A: TValue> Typst.query(
    input: String, selector: String, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(StringReader(input), "-", selector, configer)
}

inline fun <reified A: TValue> Typst.query(
    input: String, selector: TSelector, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(StringReader(input), "-", selector.repr(), configer)
}

inline fun <reified A: TValue> Typst.query(
    input: String, selector: TLabel, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(StringReader(input), "-", TLabelSelector(selector).repr(), configer)
}


@Deprecated("Using raw selectors may produce unexpected behaviour")
inline fun <reified A: TValue> Typst.query(
    input: Reader, selector: String, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(input, "-", selector, configer)
}

inline fun <reified A: TValue> Typst.query(
    input: Reader, selector: TSelector, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(input, "-", selector.repr(), configer)
}

inline fun <reified A: TValue> Typst.query(
    input: Reader, selector: TLabel, noinline configer: (QueryConfig.() -> Unit)? = null,
): TypstCompilerResult<TArray<A>> {
    return query(input, "-", TLabelSelector(selector).repr(), configer)
}
