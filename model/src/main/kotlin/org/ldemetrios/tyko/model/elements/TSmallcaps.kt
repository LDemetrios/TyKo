package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/smallcaps/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Displays text in small capitals.
 * 
 * **_Example_**
 * 
 * ```typ
 * Hello \
 * #smallcaps[Hello]
 * ```
 * <img src="https://typst.app/assets/docs/2GDSP4AltxmHWBvxVXZrwQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Smallcaps fonts_**
 * 
 * By default, this uses the `smcp` and `c2sc` OpenType features on the font. Not all fonts support these features. Sometimes, smallcaps are part of a dedicated font. This is, for example, the case for the *Latin Modern* family of fonts. In those cases, you can use a show-set rule to customize the appearance of the text in smallcaps:
 * 
 * `#show smallcaps: set text(font: "Latin Modern Roman Caps")`
 * 
 * In the future, this function will support synthesizing smallcaps from normal letters, but this is not yet implemented.
 * 
 * **_Smallcaps headings_**
 * 
 * You can use a [show rule](https://typst.app/docs/reference/styling/#show-rules) to apply smallcaps formatting to all your headings. In the example below, we also center-align our headings and disable the standard bold font.
 * 
 * ```typ
 * #set par(justify: true)
 * #set heading(numbering: "I.")
 * 
 * #show heading: smallcaps
 * #show heading: set align(center)
 * #show heading: set text(
 *   weight: "regular"
 * )
 * 
 * = Introduction
 * #lorem(40)
 * ```
 * <img src="https://typst.app/assets/docs/f0e4HVzW7NKFp4uqk6LvqgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("smallcaps")
data class TSmallcaps(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to display in small capitals.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether to turn uppercase letters into small capitals as well.
     * 
     * Unless overridden by a show rule, this enables the `c2sc` OpenType feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val all: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("smallcaps")
    }
}


@SerialName("set-smallcaps")
data class TSetSmallcaps(
    override val internals: SetRuleInternals? = null,
    val all: TBool? = null
) : TSetRule()
