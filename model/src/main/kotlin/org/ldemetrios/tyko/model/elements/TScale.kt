package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/scale/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
 * 
 * Scales content without affecting layout.
 * 
 * Lets you mirror content by specifying a negative scale on a single axis.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set align(center)
 * #scale(x: -100%)[This is mirrored.]
 * #scale(x: -100%, reflow: true)[This is mirrored.]
 * ```
 * <img src="https://typst.app/assets/docs/ShH8NomqhuEYrrdUbApjaAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("scale")
data class TScale(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
     * 
     * The content to scale.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
     * 
     * The scaling factor for both axes, as a positional argument. This is just an optional shorthand notation for setting `x` and `y` to the same value.
     * 
     * Typst type: auto|length|ratio
     */
    val factor: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
     * 
     * The horizontal scaling factor.
     * 
     * The body will be mirrored horizontally if the parameter is negative.
     * 
     * Settable; Typst type: auto|length|ratio
     */
    @all:Settable val x: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
     * 
     * The vertical scaling factor.
     * 
     * The body will be mirrored vertically if the parameter is negative.
     * 
     * Settable; Typst type: auto|length|ratio
     */
    @all:Settable val y: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
     * 
     * The origin of the transformation.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val origin: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/scale/](https://typst.app/docs/reference/layout/scale/)
     * 
     * Whether the scaling impacts the layout.
     * 
     * If set to `false`, the scaled content will be allowed to overlap other content. If set to `true`, it will compute the new size of the scaled content and adjust the layout accordingly.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val reflow: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("scale")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TScale]
 */
@SerialName("set-scale")
data class TSetScale(
    override val internals: SetRuleInternals? = null,
    val x: Smart<TRelative>? = null,
    val y: Smart<TRelative>? = null,
    val origin: TAlignment? = null,
    val reflow: TBool? = null
) : TSetRule()
