package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/attach/#functions-attach
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
 * 
 * A base with optional attachments.
 * 
 * 
 */
@SerialName("math.attach")
data class TMathAttach(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The base to which things are attached.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val base: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The top attachment, smartly positioned at top-right or above the base.
     * 
     * You can wrap the base in `limits()` or `scripts()` to override the smart positioning.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val t: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The bottom attachment, smartly positioned at the bottom-right or below the base.
     * 
     * You can wrap the base in `limits()` or `scripts()` to override the smart positioning.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val b: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The top-left attachment (before the base).
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val tl: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The bottom-left attachment (before base).
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val bl: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The top-right attachment (after the base).
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val tr: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/attach/#functions-attach](https://typst.app/docs/reference/math/attach/#functions-attach)
     * 
     * The bottom-right attachment (after the base).
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val br: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.attach")
    }
}


@SerialName("set-math.attach")
data class TSetMathAttach(
    override val internals: SetRuleInternals? = null,
    val t: Option<TContent>? = null,
    val b: Option<TContent>? = null,
    val tl: Option<TContent>? = null,
    val bl: Option<TContent>? = null,
    val tr: Option<TContent>? = null,
    val br: Option<TContent>? = null
) : TSetRule()
