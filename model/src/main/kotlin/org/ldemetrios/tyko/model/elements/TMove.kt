package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/move/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Moves content without affecting layout.
 * 
 * The `move` function allows you to move content while the layout still 'sees' it at the original positions. Containers will still be sized as if the content was not moved.
 * 
 * **_Example_**
 * 
 * ```typ
 * #rect(inset: 0pt, fill: gray, move(
 *   dx: 4pt, dy: 6pt,
 *   rect(
 *     inset: 8pt,
 *     fill: white,
 *     stroke: black,
 *     [Abra cadabra]
 *   )
 * ))
 * ```
 * <img src="https://typst.app/assets/docs/asQdhB-KaUubWCFzAxhPdAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Accessibility_**
 * 
 * Moving is transparent to Assistive Technology (AT). Your content will be read in the order it appears in the source, regardless of any visual movement. If you need to hide content from AT altogether in PDF export, consider using [`pdf.artifact`](https://typst.app/docs/reference/pdf/artifact/).
 */
@SerialName("move")
data class TMove(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to move.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The horizontal displacement of the content.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val dx: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The vertical displacement of the content.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val dy: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("move")
    }
}


@SerialName("set-move")
data class TSetMove(
    override val internals: SetRuleInternals? = null,
    val dx: TRelative? = null,
    val dy: TRelative? = null
) : TSetRule()
