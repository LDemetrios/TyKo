package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/stretch/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/stretch/](https://typst.app/docs/reference/math/stretch/)
 * 
 * Stretches a glyph.
 * 
 * This function can also be used to automatically stretch the base of an attachment, so that it fits the top and bottom attachments.
 * 
 * Note that only some glyphs can be stretched, and which ones can depend on the math font being used. However, most math fonts are the same in this regard.
 * 
 * ```typ
 * $ H stretch(=)^"define" U + p V $
 * $ f : X stretch(->>, size: #150%)_"surjective" Y $
 * $ x stretch(harpoons.ltrb, size: #3em) y
 *     stretch(\[, size: #150%) z $
 * ```
 * <img src="https://typst.app/assets/docs/s6743QhH3-etZ1y_QW-bLAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.stretch")
data class TMathStretch(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/stretch/](https://typst.app/docs/reference/math/stretch/)
     * 
     * The glyph to stretch.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/stretch/](https://typst.app/docs/reference/math/stretch/)
     * 
     * The size to stretch to, relative to the maximum size of the glyph and its attachments.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val size: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.stretch")
    }
}


@SerialName("set-math.stretch")
data class TSetMathStretch(
    override val internals: SetRuleInternals? = null,
    val size: TRelative? = null
) : TSetRule()
