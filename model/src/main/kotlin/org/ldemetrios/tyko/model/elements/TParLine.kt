package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.Numbering


//!https://typst.app/docs/reference/model/par/#definitions-line
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/par/#definitions-line](https://typst.app/docs/reference/model/par/#definitions-line)
 * 
 * A paragraph line.
 * 
 * This element is exclusively used for line number configuration through set rules and cannot be placed.
 * 
 * The [`numbering`](https://typst.app/docs/reference/model/par/#definitions-line-numbering) option is used to enable line numbers by specifying a numbering format.
 * 
 * The `numbering` option takes either a predefined [numbering pattern](https://typst.app/docs/reference/model/numbering/) or a function returning styled content. You can disable line numbers for text inside certain elements by setting the numbering to `none` using show-set rules.
 * 
 * This element exposes further options which may be used to control other aspects of line numbering, such as its [alignment](https://typst.app/docs/reference/model/par/#definitions-line-number-align) or [margin](https://typst.app/docs/reference/model/par/#definitions-line-number-margin). In addition, you can control whether the numbering is reset on each page through the [`numbering-scope`](https://typst.app/docs/reference/model/par/#definitions-line-numbering-scope) option.
 */
@SerialName("par.line")
data class TParLine(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/par/#definitions-line](https://typst.app/docs/reference/model/par/#definitions-line)
     * 
     * How to number each line. Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/) taking a single number.
     * 
     * Settable; Typst type: none|str|function
     */
    @all:Settable val numbering: Option<Numbering>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/par/#definitions-line](https://typst.app/docs/reference/model/par/#definitions-line)
     * 
     * The alignment of line numbers associated with each line.
     * 
     * The default of `auto` indicates a smart default where numbers grow horizontally away from the text, considering the margin they're in and the current text direction.
     * 
     * Settable; Typst type: auto|alignment
     */
    @all:Settable val numberAlign: Smart<TAlignment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/par/#definitions-line](https://typst.app/docs/reference/model/par/#definitions-line)
     * 
     * The margin at which line numbers appear.
     * 
     * *Note:* In a multi-column document, the line numbers for paragraphs inside the last column will always appear on the `end` margin (right margin for left-to-right text and left margin for right-to-left), regardless of this configuration. That behavior cannot be changed at this moment.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val numberMargin: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/par/#definitions-line](https://typst.app/docs/reference/model/par/#definitions-line)
     * 
     * The distance between line numbers and text.
     * 
     * The default value of `auto` results in a clearance that is adaptive to the page width and yields reasonable results in most cases.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val numberClearance: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/par/#definitions-line](https://typst.app/docs/reference/model/par/#definitions-line)
     * 
     * Controls when to reset line numbering.
     * 
     * *Note:* The line numbering scope must be uniform across each page run (a page run is a sequence of pages without an explicit pagebreak in between). For this reason, set rules for it should be defined before any page content, typically at the very start of the document.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"document"` | Indicates that the line number counter spans the whole document, i.e., it's never automatically reset. |
     * | `"page"` | Indicates that the line number counter should be reset at the start of every new page. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val numberingScope: TStr? = null,
    override val label: TLabel? = null
) : TSelectableContent<TParLine>() {
    override fun elem(): TLocatableElement<TParLine> = ELEM

    companion object {
        val ELEM = TLocatableElement<TParLine>("par.line")
    }
}

/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TParLine]
 */
@SerialName("set-par.line")
data class TSetParLine(
    override val internals: SetRuleInternals? = null,
    val numbering: Option<Numbering>? = null,
    val numberAlign: Smart<TAlignment>? = null,
    val numberMargin: TAlignment? = null,
    val numberClearance: Smart<TLength>? = null,
    val numberingScope: TStr? = null,
) : TSetRule()
