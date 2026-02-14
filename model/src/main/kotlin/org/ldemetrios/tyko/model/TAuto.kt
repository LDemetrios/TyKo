package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/auto/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/auto/](https://typst.app/docs/reference/foundations/auto/)
 * 
 * A value that indicates a smart default.
 * 
 * The auto type has exactly one value: `auto`.
 * 
 * Parameters that support the `auto` value have some smart default or contextual behaviour. A good example is the [text direction](https://typst.app/docs/reference/text/text/#parameters-dir) parameter. Setting it to `auto` lets Typst automatically determine the direction from the [text language](https://typst.app/docs/reference/text/text/#parameters-lang).
 */
@SerialName("auto")
object TAuto : TValue, Smart<Nothing>, TSmartquoteLevel, ArrayOrSingle<Smart<Nothing>>, TImageFormat,
MarginSplat<TAuto> {
    override fun type(): TType = TYPE

    val TYPE = TType.AUTO

    override fun repr(sb: StringBuilder) {
        sb.append("auto")
    }
}
