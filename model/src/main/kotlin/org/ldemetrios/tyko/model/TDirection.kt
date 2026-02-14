package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


//!https://typst.app/docs/reference/layout/direction/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/direction/](https://typst.app/docs/reference/layout/direction/)
 * 
 * The four directions into which content can be laid out.
 * 
 * Possible values are:
 * 
 * - `ltr`: Left to right.
 * - `rtl`: Right to left.
 * - `ttb`: Top to bottom.
 * - `btt`: Bottom to top.
 * 
 * These values are available globally and also in the direction type's scope, so you can write either of the following two:
 * 
 * ```typ
 * #stack(dir: rtl)[A][B][C]
 * #stack(dir: direction.rtl)[A][B][C]
 * ```
 * <img src="https://typst.app/assets/docs/43rZPR36KLZcf8RLRLjX0wAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("direction")
enum class TDirection : TValue, Smart<TDirection>, Option<TDirection> {
    @SerialName("ltr")
    LTR,
    @SerialName("rtl")
    RTL,
    @SerialName("btt")
    BTT,
    @SerialName("ttb")
    TTB;

    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DIRECTION
    }

    override fun repr(sb: StringBuilder)  {
        sb.append(name.lowercase())
    }
}
