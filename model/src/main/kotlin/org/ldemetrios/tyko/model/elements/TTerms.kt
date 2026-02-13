package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/terms/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
 * 
 * A list of terms and their descriptions.
 * 
 * Displays a sequence of terms and their descriptions vertically. When the descriptions span over multiple lines, they use hanging indent to communicate the visual hierarchy.
 * 
 * **_Example_**
 * 
 * ```typ
 * / Ligature: A merged glyph.
 * / Kerning: A spacing adjustment
 *   between two adjacent letters.
 * ```
 * <img src="https://typst.app/assets/docs/qjdQTTJFa_RYtcfu42IiawAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: Starting a line with a slash, followed by a term, a colon and a description creates a term list item.
 */
@SerialName("terms")
data class TTerms(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
     * 
     * The term list's children.
     * 
     * When using the term list syntax, adjacent items are automatically collected into term lists, even through constructs like for loops.
     * 
     * Required, positional, variadic; Typst type: content|array
     */
    @all:Variadic @all:Positional  val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
     * 
     * Defines the default [spacing](https://typst.app/docs/reference/model/terms/#parameters-spacing) of the term list. If it is `false`, the items are spaced apart with [paragraph spacing](https://typst.app/docs/reference/model/par/#parameters-spacing). If it is `true`, they use [paragraph leading](https://typst.app/docs/reference/model/par/#parameters-leading) instead. This makes the list more compact, which can look better if the items are short.
     * 
     * In markup mode, the value of this parameter is determined based on whether items are separated with a blank line. If items directly follow each other, this is set to `true`; if items are separated by a blank line, this is set to `false`. The markup-defined tightness cannot be overridden with set rules.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val tight: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
     * 
     * The separator between the item and the description.
     * 
     * If you want to just separate them with a certain amount of space, use `h(2cm, weak: true)` as the separator and replace `2cm` with your desired amount of space.
     * 
     * Settable; Typst type: content
     */
    @all:Settable val separator: TContent? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
     * 
     * The indentation of each item.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val indent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
     * 
     * The hanging indent of the description.
     * 
     * This is in addition to the whole item's `indent`.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val hangingIndent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/](https://typst.app/docs/reference/model/terms/)
     * 
     * The spacing between the items of the term list.
     * 
     * If set to `auto`, uses paragraph [`leading`](https://typst.app/docs/reference/model/par/#parameters-leading) for tight term lists and paragraph [`spacing`](https://typst.app/docs/reference/model/par/#parameters-spacing) for wide (non-tight) term lists.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val spacing: Smart<TLength>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TTerms>() {
    override fun elem(): TLocatableElement<TTerms> = ELEM

    companion object {
        val ELEM = TLocatableElement<TTerms>("terms")
    }
}


@SerialName("set-terms")
data class TSetTerms(
    override val internals: SetRuleInternals? = null,
    val tight: TBool? = null,
    val separator: TContent? = null,
    val indent: TLength? = null,
    val hangingIndent: TLength? = null,
    val spacing: Smart<TLength>? = null
) : TSetRule()
