package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/skew/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/skew/](https://typst.app/docs/reference/layout/skew/)
 * 
 * Skews content.
 * 
 * Skews an element in horizontal and/or vertical direction. The layout will act as if the element was not skewed unless you specify `reflow: true`.
 * 
 * **_Example_**
 * 
 * ```typ
 * #skew(ax: -12deg)[
 *   This is some fake italic text.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/FUtSyVs-Ma5rvUP8B0w5fQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("skew")
data class TSkew(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/skew/](https://typst.app/docs/reference/layout/skew/)
     * 
     * The content to skew.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/skew/](https://typst.app/docs/reference/layout/skew/)
     * 
     * The horizontal skewing angle.
     * 
     * Settable; Typst type: angle
     */
    @all:Settable val ax: TAngle? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/skew/](https://typst.app/docs/reference/layout/skew/)
     * 
     * The vertical skewing angle.
     * 
     * Settable; Typst type: angle
     */
    @all:Settable val ay: TAngle? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/skew/](https://typst.app/docs/reference/layout/skew/)
     * 
     * The origin of the skew transformation.
     * 
     * The origin will stay fixed during the operation.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val origin: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/skew/](https://typst.app/docs/reference/layout/skew/)
     * 
     * Whether the skew transformation impacts the layout.
     * 
     * If set to `false`, the skewed content will retain the bounding box of the original content. If set to `true`, the bounding box will take the transformation of the content into account and adjust the layout accordingly.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val reflow: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("skew")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TSkew]
 */
@SerialName("set-skew")
data class TSetSkew(
    override val internals: SetRuleInternals? = null,
    val ax: TAngle? = null,
    val ay: TAngle? = null,
    val origin: TAlignment? = null,
    val reflow: TBool? = null
) : TSetRule()
