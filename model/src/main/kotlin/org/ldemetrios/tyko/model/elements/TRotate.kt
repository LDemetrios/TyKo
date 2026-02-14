package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/rotate/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/rotate/](https://typst.app/docs/reference/layout/rotate/)
 * 
 * Rotates content without affecting layout.
 * 
 * Rotates an element by a given angle. The layout will act as if the element was not rotated unless you specify `reflow: true`.
 * 
 * **_Example_**
 * 
 * ```typ
 * #stack(
 *   dir: ltr,
 *   spacing: 1fr,
 *   ..range(16)
 *     .map(i => rotate(24deg * i)[X]),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/KRNlJxFzPXxwMsKBe0vSFQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("rotate")
data class TRotate(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/rotate/](https://typst.app/docs/reference/layout/rotate/)
     * 
     * The amount of rotation.
     * 
     * Positional, settable; Typst type: angle
     */
    @all:Settable @all:Positional val angle: TAngle? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/rotate/](https://typst.app/docs/reference/layout/rotate/)
     * 
     * The content to rotate.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/rotate/](https://typst.app/docs/reference/layout/rotate/)
     * 
     * The origin of the rotation.
     * 
     * If, for instance, you wanted the bottom left corner of the rotated element to stay aligned with the baseline, you would set it to `bottom + left` instead.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val origin: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/rotate/](https://typst.app/docs/reference/layout/rotate/)
     * 
     * Whether the rotation impacts the layout.
     * 
     * If set to `false`, the rotated content will retain the bounding box of the original content. If set to `true`, the bounding box will take the rotation of the content into account and adjust the layout accordingly.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val reflow: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("rotate")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TRotate]
 */
@SerialName("set-rotate")
data class TSetRotate(
    override val internals: SetRuleInternals? = null,
    @all:Positional val angle: TAngle? = null,
    val origin: TAlignment? = null,
    val reflow: TBool? = null
) : TSetRule()
