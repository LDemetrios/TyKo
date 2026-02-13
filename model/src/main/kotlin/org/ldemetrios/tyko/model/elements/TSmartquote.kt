package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


sealed interface TSmartquoteSpec : IntoValue {
    companion object {
        fun fromValue(value: TValue): TSmartquoteSpec = when (value) {
            is TSmartquoteLevel -> value
            is TDict<*> -> TSmartquoteSymbols.fromValue(value)
            is TArray<*> -> TOpenCloseQuotes.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@UnionType(["str", "auto"])
sealed interface TSmartquoteLevel : TSmartquoteSpec, TValue


@SerialName("array")
data class TOpenCloseQuotes(val open: TStr, val close: TStr) : TSmartquoteSpec, IntoArr<TStr> {
    override fun intoValue(): TArray<TStr> = TArray(listOf(open, close))

    companion object {
        fun fromValue(value: TValue): TOpenCloseQuotes = when (value) {
            is TArray<*> -> if (value.size == 2) TOpenCloseQuotes(
                value[0].intoValue() as TStr,
                value[1].intoValue() as TStr
            ) else {
                throw AssertionError("Can't convert from $value")
            }

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}


@SerialName("dict")
data class TSmartquoteSymbols(
    val single: TSmartquoteSpec?,
    val double: TSmartquoteSpec?,
) : IntoDict<TSmartquoteSpec>, TSmartquoteSpec {
    override fun intoValue(): TDict<TSmartquoteSpec> = TDict(
        mapOfNotNullValues(
             "single" to single,
             "double" to double,
        )
    )

    companion object {
        fun fromValue(value: TValue): TSmartquoteSymbols = when (value) {
            is TDict<*> -> TSmartquoteSymbols(
                value["single"]?.intoValue()?.let { TSmartquoteSpec.fromValue(it) },
                value["double"]?.intoValue()?.let { TSmartquoteSpec.fromValue(it) },
            )

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

//!https://typst.app/docs/reference/text/smartquote/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A language-aware quote that reacts to its context.
 * 
 * Automatically turns into an appropriate opening or closing quote based on the active [text language](https://typst.app/docs/reference/text/text/#parameters-lang).
 * 
 * **_Example_**
 * 
 * ```typ
 * "This is in quotes."
 * 
 * #set text(lang: "de")
 * "Das ist in Anführungszeichen."
 * 
 * #set text(lang: "fr")
 * "C'est entre guillemets."
 * ```
 * <img src="https://typst.app/assets/docs/dhrUjSwC3cH8VIWvplWrzwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: The normal quote characters (`'` and `"`). Typst automatically makes your quotes smart.
 */
@SerialName("smartquote")
data class TSmartquote(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether this should be a double quote.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val double: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether smart quotes are enabled.
     * 
     * To disable smartness for a single quote, you can also escape it with a backslash.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val enabled: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether to use alternative quotes.
     * 
     * Does nothing for languages that don't have alternative quotes, or if explicit quotes were set.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val alternative: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The quotes to use.
     * 
     * - When set to `auto`, the appropriate single quotes for the [text language](https://typst.app/docs/reference/text/text/#parameters-lang) will be used. This is the default.
     * - Custom quotes can be passed as a string, array, or dictionary of either
     *   - [string](https://typst.app/docs/reference/foundations/str/): a string consisting of two characters containing the opening and closing double quotes (characters here refer to Unicode grapheme clusters)
     *   - [array](https://typst.app/docs/reference/foundations/array/): an array containing the opening and closing double quotes
     *   - [dictionary](https://typst.app/docs/reference/foundations/dictionary/): a dictionary containing the double and single quotes, each specified as either `auto`, string, or array
     * 
     * Settable; Typst type: auto|str|array|dictionary
     */
    @all:Settable val quotes: TSmartquoteSpec? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("smartquote")
    }
}


@SerialName("set-smartquote")
data class TSetSmartquote(
    override val internals: SetRuleInternals? = null,
    val double: TBool? = null,
    val enabled: TBool? = null,
    val alternative: TBool? = null,
    val quotes: TSmartquoteSpec? = null
) : TSetRule()
