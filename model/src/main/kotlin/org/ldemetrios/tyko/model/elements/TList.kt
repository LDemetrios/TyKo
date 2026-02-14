package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/list/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
 * 
 * A bullet list.
 * 
 * Displays a sequence of items vertically, with each item introduced by a marker.
 * 
 * **_Example_**
 * 
 * ```typ
 * Normal list.
 * - Text
 * - Math
 * - Layout
 * - ...
 * 
 * Multiple lines.
 * - This list item spans multiple
 *   lines because it is indented.
 * 
 * Function call.
 * #list(
 *   [Foundations],
 *   [Calculate],
 *   [Construct],
 *   [Data Loading],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/dGd96M9aTTHo-jKJ9y73kwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This functions also has dedicated syntax: Start a line with a hyphen, followed by a space to create a list item. A list item can contain multiple paragraphs and other block-level content. All content that is indented more than an item's marker becomes part of that item.
 */
@SerialName("list")
data class TList(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
     * 
     * The bullet list's children.
     * 
     * When using the list syntax, adjacent items are automatically collected into lists, even through constructs like for loops.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TListItem>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
     * 
     * Defines the default [spacing](https://typst.app/docs/reference/model/list/#parameters-spacing) of the list. If it is `false`, the items are spaced apart with [paragraph spacing](https://typst.app/docs/reference/model/par/#parameters-spacing). If it is `true`, they use [paragraph leading](https://typst.app/docs/reference/model/par/#parameters-leading) instead. This makes the list more compact, which can look better if the items are short.
     * 
     * In markup mode, the value of this parameter is determined based on whether items are separated with a blank line. If items directly follow each other, this is set to `true`; if items are separated by a blank line, this is set to `false`. The markup-defined tightness cannot be overridden with set rules.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val tight: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
     * 
     * The marker which introduces each item.
     * 
     * Instead of plain content, you can also pass an array with multiple markers that should be used for nested lists. If the list nesting depth exceeds the number of markers, the markers are cycled. For total control, you may pass a function that maps the list's nesting depth (starting from `0`) to a desired marker.
     * 
     * Settable; Typst type: content|array|function
     */
    @all:Settable val marker: Progression<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
     * 
     * The indent of each item.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val indent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
     * 
     * The spacing between the marker and the body of each item.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val bodyIndent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/](https://typst.app/docs/reference/model/list/)
     * 
     * The spacing between the items of the list.
     * 
     * If set to `auto`, uses paragraph [`leading`](https://typst.app/docs/reference/model/par/#parameters-leading) for tight lists and paragraph [`spacing`](https://typst.app/docs/reference/model/par/#parameters-spacing) for wide (non-tight) lists.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val spacing: Smart<TLength>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TList>() {
    override fun elem(): TLocatableElement<TList> = ELEM

    companion object {
        val ELEM = TLocatableElement<TList>("list")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TList]
 */
@SerialName("set-list")
data class TSetList(
    override val internals: SetRuleInternals? = null,
    val tight: TBool? = null,
    val marker: Progression<TContent>? = null,
    val indent: TLength? = null,
    val bodyIndent: TLength? = null,
    val spacing: Smart<TLength>? = null
) : TSetRule()
