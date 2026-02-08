package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/repeat/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Repeats content to the available space.
 * 
 * This can be useful when implementing a custom index, reference, or outline.
 * 
 * Space may be inserted between the instances of the body parameter, so be sure to adjust the [`justify`](https://typst.app/docs/reference/layout/repeat/#parameters-justify) parameter accordingly.
 * 
 * Errors if there are no bounds on the available space, as it would create infinite content.
 * 
 * **_Example_**
 * 
 * ```typ
 * Sign on the dotted line:
 * #box(width: 1fr, repeat[.])
 * 
 * #set text(10pt)
 * #v(8pt, weak: true)
 * #align(right)[
 *   Berlin, the 22nd of December, 2022
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/LGILa4453RB6xoEobzmQcAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Accessibility_**
 * 
 * Repeated content is automatically marked as an [artifact](https://typst.app/docs/reference/pdf/artifact/) and hidden from Assistive Technology (AT). Do not use this function to create content that contributes to the meaning of your document.
 */
@SerialName("repeat")
data class TRepeat(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to repeat.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The gap between each instance of the body.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val gap: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether to increase the gap between instances to completely fill the available space.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val justify: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("repeat")
    }
}


@SerialName("set-repeat")
data class TSetRepeat(
    override val internals: SetRuleInternals? = null,
    val gap: TLength? = null,
    val justify: TBool? = null
) : TSetRule()
