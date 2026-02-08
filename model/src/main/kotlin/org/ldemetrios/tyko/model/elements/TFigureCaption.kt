package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/figure/#definitions-caption
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * The caption of a figure. This element can be used in set and show rules to customize the appearance of captions for all figures or figures of a specific kind.
 * 
 * In addition to its `position` and `body`, the `caption` also provides the figure's `kind`, `supplement`, `counter`, and `numbering` as fields. These parts can be used in [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selectors and show rules to build a completely custom caption.
 * 
 * 
 */
@SerialName("figure.caption")
data class TFigureCaption(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The caption's body.
     * 
     * Can be used alongside `kind`, `supplement`, `counter`, `numbering`, and `location` to completely customize the caption.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The caption's position in the figure. Either `top` or `bottom`.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val position: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The separator which will appear between the number and body.
     * 
     * If set to `auto`, the separator will be adapted to the current [language](https://typst.app/docs/reference/text/text/#parameters-lang) and [region](https://typst.app/docs/reference/text/text/#parameters-region).
     * 
     * Settable; Typst type: auto|content
     */
    @all:Settable val separator: Smart<TContent>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TFigureCaption>() {
    override fun elem(): TLocatableElement<TFigureCaption> = ELEM

    companion object {
        val ELEM = TLocatableElement<TFigureCaption>("figure.caption")
    }
}


@SerialName("set-figure.caption")
data class TSetFigureCaption(
    override val internals: SetRuleInternals? = null,
    val position: TAlignment? = null,
    val separator: Smart<TContent>? = null
) : TSetRule()
