package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/pad/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Adds spacing around content.
 * 
 * The spacing can be specified for each side individually, or for all sides at once by specifying a positional argument.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set align(center)
 * 
 * #pad(x: 16pt, image("typing.jpg"))
 * _Typing speeds can be
 *  measured in words per minute._
 * ```
 * <img src="https://typst.app/assets/docs/YnvzY3ls2HrcPgokDMxVqwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("pad")
data class TPad(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to pad at the sides.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The padding at the left side.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val left: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The padding at the top side.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val top: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The padding at the right side.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val right: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The padding at the bottom side.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val bottom: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A shorthand to set `left` and `right` to the same value.
     * 
     * Typst type: relative
     */
    val x: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A shorthand to set `top` and `bottom` to the same value.
     * 
     * Typst type: relative
     */
    val y: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A shorthand to set all four sides to the same value.
     * 
     * Typst type: relative
     */
    val rest: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("pad")
    }
}


@SerialName("set-pad")
data class TSetPad(
    override val internals: SetRuleInternals? = null,
    val left: TRelative? = null,
    val top: TRelative? = null,
    val right: TRelative? = null,
    val bottom: TRelative? = null
) : TSetRule()
