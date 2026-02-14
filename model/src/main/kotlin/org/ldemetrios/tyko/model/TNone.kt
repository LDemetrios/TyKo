package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/none/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/none/](https://typst.app/docs/reference/foundations/none/)
 * 
 * A value that indicates the absence of any other value.
 * 
 * The none type has exactly one value: `none`.
 * 
 * When inserted into the document, it is not visible. This is also the value that is produced by empty code blocks. It can be [joined](https://typst.app/docs/reference/scripting/#blocks) with any value, yielding the other value.
 * 
 * **_Example_**
 * 
 * ```typ
 * Not visible: #none
 * ```
 * <img src="https://typst.app/assets/docs/bWChCwjCUgpluIjZfBh2dgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("none")
data object TNone : TValue, Dash, Smart<TNone>, TCurveControl<Nothing>, ArrayOrSingle<TNone>,
    Option<Nothing>, SidesSplat<TNone> {
    override fun type(): TType = TYPE

    val TYPE = TType.NONE

    override fun repr(sb: StringBuilder) {
        sb.append("none")
    }
}
