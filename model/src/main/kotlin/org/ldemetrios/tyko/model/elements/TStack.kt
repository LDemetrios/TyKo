package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/stack/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/stack/](https://typst.app/docs/reference/layout/stack/)
 * 
 * Arranges content and spacing horizontally or vertically.
 * 
 * The stack places a list of items along an axis, with optional spacing between each item.
 * 
 * **_Example_**
 * 
 * ```typ
 * #stack(
 *   dir: ttb,
 *   rect(width: 40pt),
 *   rect(width: 120pt),
 *   rect(width: 90pt),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/rblc_gO4o5qSEPJtXD1qPgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Accessibility_**
 * 
 * Stacks do not carry any special semantics. The contents of the stack are read by Assistive Technology (AT) in the order in which they have been passed to this function.
 */
@SerialName("stack")
data class TStack(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/stack/](https://typst.app/docs/reference/layout/stack/)
     * 
     * The children to stack along the axis.
     * 
     * Required, positional, variadic; Typst type: relative|fraction|content
     */
    @all:Variadic @all:Positional val children: TArray<TStackComponent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/stack/](https://typst.app/docs/reference/layout/stack/)
     * 
     * The direction along which the items are stacked. Possible values are:
     * 
     * - `ltr`: Left to right.
     * - `rtl`: Right to left.
     * - `ttb`: Top to bottom.
     * - `btt`: Bottom to top.
     * 
     * You can use the `start` and `end` methods to obtain the initial and final points (respectively) of a direction, as `alignment`. You can also use the `axis` method to determine whether a direction is `"horizontal"` or `"vertical"`. The `inv` method returns a direction's inverse direction.
     * 
     * For example, `ttb.start()` is `top`, `ttb.end()` is `bottom`, `ttb.axis()` is `"vertical"` and `ttb.inv()` is equal to `btt`.
     * 
     * Settable; Typst type: direction
     */
    @all:Settable val dir: TDirection? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/stack/](https://typst.app/docs/reference/layout/stack/)
     * 
     * Spacing to insert between items where no explicit spacing was provided.
     * 
     * Settable; Typst type: none|relative|fraction
     */
    @all:Settable val spacing: Option<Spacing>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("stack")
    }
}


@SerialName("set-stack")
data class TSetStack(
    override val internals: SetRuleInternals? = null,
    val dir: TDirection? = null,
    val spacing: Option<Spacing>? = null
) : TSetRule()
