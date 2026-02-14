package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Numbering


//!https://typst.app/docs/reference/model/enum/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
 * 
 * A numbered list.
 * 
 * Displays a sequence of items vertically and numbers them consecutively.
 * 
 * **_Example_**
 * 
 * ```typ
 * Automatically numbered:
 * + Preparations
 * + Analysis
 * + Conclusions
 * 
 * Manually numbered:
 * 2. What is the first step?
 * 5. I am confused.
 * +  Moving on ...
 * 
 * Multiple lines:
 * + This enum item has multiple
 *   lines because the next line
 *   is indented.
 * 
 * Function call.
 * #enum[First][Second]
 * ```
 * <img src="https://typst.app/assets/docs/HrnJ1mRKvbXNf6U4DmZCaAAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can easily switch all your enumerations to a different numbering style with a set rule.
 * 
 * ```typ
 * #set enum(numbering: "a)")
 * 
 * + Starting off ...
 * + Don't forget step two
 * ```
 * <img src="https://typst.app/assets/docs/hFb68a8DC-Rvf_eMOYtVMwAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can also use [`enum.item`](https://typst.app/docs/reference/model/enum/#definitions-item) to programmatically customize the number of each item in the enumeration:
 * 
 * ```typ
 * #enum(
 *   enum.item(1)[First step],
 *   enum.item(5)[Fifth step],
 *   enum.item(10)[Tenth step]
 * )
 * ```
 * <img src="https://typst.app/assets/docs/uRQbjXrkv7FwltBxluVMMAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This functions also has dedicated syntax:
 * 
 * - Starting a line with a plus sign creates an automatically numbered enumeration item.
 * - Starting a line with a number followed by a dot creates an explicitly numbered enumeration item.
 * 
 * Enumeration items can contain multiple paragraphs and other block-level content. All content that is indented more than an item's marker becomes part of that item.
 */
@SerialName("enum")
data class TEnum(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * The numbered list's items.
     * 
     * When using the enum syntax, adjacent items are automatically collected into enumerations, even through constructs like for loops.
     * 
     * Required, positional, variadic; Typst type: content|array
     */
    @all:Variadic @all:Positional val children: TArray<TEnumItem>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * Defines the default [spacing](https://typst.app/docs/reference/model/enum/#parameters-spacing) of the enumeration. If it is `false`, the items are spaced apart with [paragraph spacing](https://typst.app/docs/reference/model/par/#parameters-spacing). If it is `true`, they use [paragraph leading](https://typst.app/docs/reference/model/par/#parameters-leading) instead. This makes the list more compact, which can look better if the items are short.
     * 
     * In markup mode, the value of this parameter is determined based on whether items are separated with a blank line. If items directly follow each other, this is set to `true`; if items are separated by a blank line, this is set to `false`. The markup-defined tightness cannot be overridden with set rules.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val tight: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * How to number the enumeration. Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/).
     * 
     * If the numbering pattern contains multiple counting symbols, they apply to nested enums. If given a function, the function receives one argument if `full` is `false` and multiple arguments if `full` is `true`.
     * 
     * Settable; Typst type: str|function
     */
    @all:Settable val numbering: Numbering? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * Which number to start the enumeration with.
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val start: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * Whether to display the full numbering, including the numbers of all parent enumerations.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val full: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * Whether to reverse the numbering for this enumeration.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val reversed: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * The indentation of each item.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val indent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * The space between the numbering and the body of each item.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val bodyIndent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * The spacing between the items of the enumeration.
     * 
     * If set to `auto`, uses paragraph [`leading`](https://typst.app/docs/reference/model/par/#parameters-leading) for tight enumerations and paragraph [`spacing`](https://typst.app/docs/reference/model/par/#parameters-spacing) for wide (non-tight) enumerations.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val spacing: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/](https://typst.app/docs/reference/model/enum/)
     * 
     * The alignment that enum numbers should have.
     * 
     * By default, this is set to `end + top`, which aligns enum numbers towards end of the current text direction (in left-to-right script, for example, this is the same as `right`) and at the top of the line. The choice of `end` for horizontal alignment of enum numbers is usually preferred over `start`, as numbers then grow away from the text instead of towards it, avoiding certain visual issues. This option lets you override this behaviour, however. (Also to note is that the [unordered list](https://typst.app/docs/reference/model/list/) uses a different method for this, by giving the `marker` content an alignment directly.).
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val numberAlign: TAlignment? = null,
    override val label: TLabel? = null
) : TSelectableContent<TEnum>() {
    override fun elem(): TLocatableElement<TEnum> = ELEM

    companion object {
        val ELEM = TLocatableElement<TEnum>("enum")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TEnum]
 */
@SerialName("set-enum")
data class TSetEnum(
    override val internals: SetRuleInternals? = null,
    val tight: TBool? = null,
    val numbering: Numbering? = null,
    val start: Smart<TInt>? = null,
    val full: TBool? = null,
    val reversed: TBool? = null,
    val indent: TLength? = null,
    val bodyIndent: TLength? = null,
    val spacing: Smart<TLength>? = null,
    val numberAlign: TAlignment? = null
) : TSetRule()
