package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.NativeCastInfo
import org.ldemetrios.tyko.compiler.NativeFuncDataDescriptor
import org.ldemetrios.tyko.compiler.NativeParamDescriptor
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.TArgs
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TStyle
import org.ldemetrios.tyko.model.TStyles
import org.ldemetrios.tyko.model.TType
import org.ldemetrios.tyko.model.TValue
import kotlin.String

class LibrarySpecBuilder {
    private var proto: LibraryPrototypeSpecification? = null
        set(value) {
            if (field != null) throw IllegalStateException("Prototype is already set")
            field = value
        }

    private var appendStylesV: Boolean? = null
    var appendStyles: Boolean
        get() = appendStylesV ?: throw IllegalStateException("`appendStyles` is not set")
        set(value) {
            if (appendStylesV != null) throw IllegalStateException("`appendStyles` is already set")
            appendStylesV = value
        }

    fun fromDefault() {
        proto = LibraryPrototypeSpecification.UseDefault
    }

    fun prototype(prototype: LibraryReference) {
        proto = LibraryPrototypeSpecification.UseReference(prototype)
    }

    fun fresh(features: Set<Feature>) {
        proto = LibraryPrototypeSpecification.Fresh(features)
    }

    private var styles = mutableListOf<TStyle>()

    fun styles(vararg style: TStyle) = styles(style.asList())
    fun styles(styles: List<TStyle>) {
        this.styles.addAll(styles)
    }

    fun styles(styles: TStyles) = styles(styles.value)
    fun styles(builder: MutableList<TStyle>.() -> Unit) = styles(buildList(builder))

    private var clearInputs = false
    fun clearInputs() {
        clearInputs = true
    }

    private var inputs = mutableMapOf<String, TValue>()
    fun inputs(vararg pairs: Pair<String, IntoValue>) = inputs(pairs.toMap())
    fun inputs(builder: MapScope.() -> Unit) = inputs(MapScope(mutableMapOf()).apply(builder))
    fun inputs(map: Map<String, IntoValue>) {
        inputs.putAll(map.mapValues { it.value.intoValue() })
    }

    private var values = mutableMapOf<String, TValue>()
    fun values(vararg pairs: Pair<String, IntoValue>) = values(pairs.toMap())
    fun values(builder: MapScope.() -> Unit) = values(MapScope(mutableMapOf()).apply(builder))
    fun values(map: Map<String, IntoValue>) {
        values.putAll(map.mapValues { it.value.intoValue() })
    }

    private var funcs = mutableMapOf<String, FuncSpecification>()
    fun funcs(vararg pairs: Pair<String, NativeFuncDataReference>) = funcs(pairs.toMap())
    fun funcs(map: Map<String, NativeFuncDataReference>) {
        funcs.putAll(map.mapValues { FuncSpecification.BorrowedFunction(it.value) })
    }

    fun func(name: String, funcBuilder: NewFunctionSpecBuilder.() -> Unit) {
        funcs[name] = NewFunctionSpecBuilder(name).apply(funcBuilder).build()
    }

    fun build(): LibrarySpecification = LibrarySpecification(
        proto ?: LibraryPrototypeSpecification.Fresh(setOf()),
        styles,
        appendStylesV ?: true,
        if (!clearInputs && inputs.isEmpty()) null else TDict(inputs),
        values,
        funcs,
    )
}

class MapScope(private val delegate: MutableMap<String, IntoValue>) : MutableMap<String, IntoValue> by delegate {
    operator fun String.invoke(value: IntoValue) {
        put(this, value)
    }
}

class NewFunctionSpecBuilder(val name: String) {
    private var titleV: String? = null
    var title: String
        get() = titleV ?: throw IllegalStateException("Title is not set")
        set(value) {
            if (titleV != null) throw IllegalStateException("Title is already set")
            titleV = value
        }


    private var docsV: String? = null
    var docs: String
        get() = docsV ?: throw IllegalStateException("Documentation is not set")
        set(value) {
            if (docsV != null) throw IllegalStateException("Documentation is already set")
            docsV = value
        }


    private var keywords = mutableListOf<String>()
    fun keywords(vararg keyword: String) = keywords(keyword.asList())
    fun keywords(keywords: List<String>) {
        this.keywords.addAll(keywords)
    }

    fun keywords(builder: MutableList<String>.() -> Unit) = keywords(buildList(builder))


    private var contextual: Boolean? = null

    private var returnsV: NativeCastInfo? = null
    var returns: NativeCastInfo
        get() = returnsV ?: throw IllegalStateException("Return type info is not set")
        set(value) {
            if (returnsV != null) throw IllegalStateException("Return type info is already set")
            returnsV = value
        }

    fun returns(info: NativeCastInfo) {
        returns = info
    }

    fun returnsAny() = returns(NativeCastInfo.any())
    fun returns(type: TType) = returns(NativeCastInfo.union())
    fun returnsUnion(vararg types: TType) {
        if (types.size == 1) returns(types[0])
        else returns(NativeCastInfo.union(*types.map { NativeCastInfo.type(it.name.value) }.toTypedArray()))
    }

    private var params = mutableListOf<NativeParamDescriptor>()

    fun param(name: String, body: ParamInfoBuilder.() -> Unit) {
        params.add(ParamInfoBuilder(name).apply(body).build())
    }

    private lateinit var executor: (TArgs<TValue>, TStyles) -> RResult<TValue, String>

    fun contextual(body: (TArgs<TValue>, TStyles) -> RResult<TValue, String>) {
        contextual = true
        executor = body
    }

    fun contextualCatching(body: (TArgs<TValue>, TStyles) -> TValue) {
        contextual { args, ctx ->
            try {
                RResult.Ok(body(args, ctx))
            } catch (e: ThreadDeath) {
                throw e
            } catch (e: VirtualMachineError) {
                throw e
            } catch (e: Throwable) {
                val here = Thread.currentThread().stackTrace.reversed()
                val there = e.stackTrace.reversed()

                val overlap = minOf(there.size, here.size)
                val common = (0 until overlap).takeWhile { there[it] == here[it] }.size
                val errorString = e.toString() + "\n" + there.drop(common).reversed().joinToString("\n")
                RResult.Err(errorString)
            }
        }
    }

    fun pure(body: (TArgs<TValue>) -> RResult<TValue, String>) {
        contextual = false
        executor = { it, _ -> body(it) }
    }

    fun pureCatching(body: (TArgs<TValue>) -> TValue) {
        pure { args ->
            try {
                RResult.Ok(body(args))
            } catch (e: ThreadDeath) {
                throw e
            } catch (e: VirtualMachineError) {
                throw e
            } catch (e: Throwable) {
                val here = Thread.currentThread().stackTrace.reversed()
                val there = e.stackTrace.reversed()

                val overlap = minOf(there.size, here.size)
                val common = (0 until overlap).takeWhile { there[it] == here[it] }.size
                val errorString = e.toString() + "\n" + there.drop(common).reversed().joinToString("\n") { "\tat $it"}
                RResult.Err(errorString)
            }
        }
    }


    fun build() = FuncSpecification.NewFunction(
        NativeFuncDataDescriptor(
            name,
            titleV ?: name,
            docsV ?: name,
            keywords,
            contextual ?: throw IllegalStateException("Function body isn't specified"),
            params,
            returnsV ?: NativeCastInfo.any(),
        ),
        executor
    )
}

class ParamInfoBuilder(val name: String) {
    private var docsV: String? = null
    var docs: String
        get() = docsV ?: throw IllegalStateException("Documentation is not set")
        set(value) {
            if (docsV != null) throw IllegalStateException("Documentation is already set")
            docsV = value
        }


    private var inputV: NativeCastInfo? = null
    var input: NativeCastInfo
        get() = inputV ?: throw IllegalStateException("Param type info is not set")
        set(value) {
            if (inputV != null) throw IllegalStateException("Param type info is already set")
            inputV = value
        }

    fun input(info: NativeCastInfo) {
        input = info
    }

    fun inputAny() = input(NativeCastInfo.any())
    fun input(type: TType) = input(NativeCastInfo.union())
    fun inputUnion(vararg types: TType) {
        if (types.size == 1) input(types[0])
        else input(NativeCastInfo.union(*types.map { NativeCastInfo.type(it.name.value) }.toTypedArray()))
    }

    private var positionalV: Boolean? = null
    var positional: Boolean
        get() = positionalV ?: throw IllegalStateException("`positional` is not set")
        set(value) {
            if (positionalV != null) throw IllegalStateException("`positional` is already set")
            positionalV = value
        }


    private var namedV: Boolean? = null
    var named: Boolean
        get() = namedV ?: throw IllegalStateException("`named` is not set")
        set(value) {
            if (namedV != null) throw IllegalStateException("`named` is already set")
            namedV = value
        }


    private var variadicV: Boolean? = null
    var variadic: Boolean
        get() = variadicV ?: throw IllegalStateException("`variadic` is not set")
        set(value) {
            if (variadicV != null) throw IllegalStateException("`variadic` is already set")
            variadicV = value
        }


    private var requiredV: Boolean? = null
    var required: Boolean
        get() = requiredV ?: throw IllegalStateException("`required` is not set")
        set(value) {
            if (requiredV != null) throw IllegalStateException("`required` is already set")
            requiredV = value
        }


    private var settableV: Boolean? = null
    var settable: Boolean
        get() = settableV ?: throw IllegalStateException("`settable` is not set")
        set(value) {
            if (settableV != null) throw IllegalStateException("`settable` is already set")
            settableV = value
        }

    fun build() = NativeParamDescriptor(
        name,
        docsV ?: name,
        inputV ?: NativeCastInfo.any(),
        positional = positionalV ?: true,
        named = namedV ?: false,
        variadic = variadicV ?: false,
        required = requiredV ?: true,
        settable = settableV ?: false,
    )
}
