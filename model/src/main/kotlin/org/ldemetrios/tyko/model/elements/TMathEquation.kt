package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Numbering


//!https://typst.app/docs/reference/math/equation/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
 * 
 * A mathematical equation.
 * 
 * Can be displayed inline with text or as a separate block. An equation becomes block-level through the presence of whitespace after the opening dollar sign and whitespace before the closing dollar sign.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set text(font: "New Computer Modern")
 * 
 * Let $a$, $b$, and $c$ be the side
 * lengths of right-angled triangle.
 * Then, we know that:
 * $ a^2 + b^2 = c^2 $
 * 
 * Prove by induction:
 * $ sum_(k=1)^n k = (n(n+1)) / 2 $
 * ```
 * <img src="https://typst.app/assets/docs/JtxOgQArvspfmmStl8-3_gAAAAAAAAAA.png" alt="Preview" />
 * 
 * By default, block-level equations will not break across pages. This can be changed through `show math.equation: set block(breakable: true)`.
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: Write mathematical markup within dollar signs to create an equation. Starting and ending the equation with whitespace lifts it into a separate block that is centered horizontally. For more details about math syntax, see the [main math page](https://typst.app/docs/reference/math/).
 */
@SerialName("math.equation")
data class TEquation(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
     * 
     * The contents of the equation.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
     * 
     * Whether the equation is displayed as a separate block.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val block: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
     * 
     * How to number block-level equations. Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/) taking a single number.
     * 
     * Settable; Typst type: none|str|function
     */
    @all:Settable val numbering: Option<Numbering>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
     * 
     * The alignment of the equation numbering.
     * 
     * By default, the alignment is `end + horizon`. For the horizontal component, you can use `right`, `left`, or `start` and `end` of the text direction; for the vertical component, you can use `top`, `horizon`, or `bottom`.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val numberAlign: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
     * 
     * A supplement for the equation.
     * 
     * For references to equations, this is added before the referenced number.
     * 
     * If a function is specified, it is passed the referenced equation and should return content.
     * 
     * Settable; Typst type: none|auto|content|function
     */
    @all:Settable val supplement: Smart<Option<Computable<TContent>>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/equation/](https://typst.app/docs/reference/math/equation/)
     * 
     * An alternative description of the mathematical equation.
     * 
     * This should describe the full equation in natural language and will be made available to Assistive Technology. You can learn more in the [Textual Representations section of the Accessibility Guide](https://typst.app/docs/guides/accessibility/#textual-representations).
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val alt: Option<TStr>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TEquation>() {
    override fun elem(): TLocatableElement<TEquation> = ELEM

    companion object {
        val ELEM = TLocatableElement<TEquation>("math.equation")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TEquation]
 */
@SerialName("set-math.equation")
data class TSetEquation(
    override val internals: SetRuleInternals? = null,
    val block: TBool? = null,
    val numbering: Option<Numbering>? = null,
    val numberAlign: TAlignment? = null,
    val supplement: Smart<Option<Computable<TContent>>>? = null,
    val alt: Option<TStr>? = null
) : TSetRule()
