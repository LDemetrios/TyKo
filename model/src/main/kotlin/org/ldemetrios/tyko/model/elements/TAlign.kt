package org.ldemetrios.tyko.model

//!https://typst.app/docs/reference/layout/align/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Aligns content horizontally and vertically.
 * 
 * **_Example_**
 * 
 * Let's start with centering our content horizontally:
 * 
 * ```typ
 * #set page(height: 120pt)
 * #set align(center)
 * 
 * Centered text, a sight to see \
 * In perfect balance, visually \
 * Not left nor right, it stands alone \
 * A work of art, a visual throne
 * ```
 * <img src="https://typst.app/assets/docs/kcNIG-bYA8T9BUDnjCUJGgAAAAAAAAAA.png" alt="Preview" />
 * 
 * To center something vertically, use *horizon* alignment:
 * 
 * ```typ
 * #set page(height: 120pt)
 * #set align(horizon)
 * 
 * Vertically centered, \
 * the stage had entered, \
 * a new paragraph.
 * ```
 * <img src="https://typst.app/assets/docs/y9OO-MSDQIHWsGPc_6pNnAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Combining alignments_**
 * 
 * You can combine two alignments with the `+` operator. Let's also only apply this to one piece of content by using the function form instead of a set rule:
 * 
 * ```typ
 * #set page(height: 120pt)
 * Though left in the beginning ...
 * 
 * #align(right + bottom)[
 *   ... they were right in the end, \
 *   and with addition had gotten, \
 *   the paragraph to the bottom!
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/gXaqAMYC8Licj_UCK0JSFgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Nested alignment_**
 * 
 * You can use varying alignments for layout containers and the elements within them. This way, you can create intricate layouts:
 * 
 * ```typ
 * #align(center, block[
 *   #set align(left)
 *   Though centered together \
 *   alone \
 *   we \
 *   are \
 *   left.
 * ])
 * ```
 * <img src="https://typst.app/assets/docs/B6Y-WWFtiUjCHNJ9B8R8vQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Alignment within the same line_**
 * 
 * The `align` function performs block-level alignment and thus always interrupts the current paragraph. To have different alignment for parts of the same line, you should use [fractional spacing](https://typst.app/docs/reference/layout/h/) instead:
 * 
 * ```typ
 * Start #h(1fr) End
 * ```
 * <img src="https://typst.app/assets/docs/jlafwbE2ZuISwJPQNRzA3gAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("align")
data class TAlign(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The [alignment](https://typst.app/docs/reference/layout/alignment/) along both axes.
     * 
     * Positional, settable; Typst type: alignment
     */
    @all:Settable @all:Positional val alignment: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to align.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional @all:Body val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM
    companion object {
        val ELEM = TElement("align")
    }
}

@SerialName("set-align")
data class TSetAlign(
    override val internals: SetRuleInternals? = null,
    @all:Positional val alignment: TAlignment? = null
) : TSetRule()
