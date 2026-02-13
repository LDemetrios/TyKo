package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/layout/block/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
 * 
 * A block-level container.
 * 
 * Such a container can be used to separate content, size it, and give it a background or border.
 * 
 * Blocks are also the primary way to control whether text becomes part of a paragraph or not. See [the paragraph documentation](https://typst.app/docs/reference/model/par/#what-becomes-a-paragraph) for more details.
 * 
 * **_Examples_**
 * 
 * With a block, you can give a background to content while still allowing it to break across multiple pages.
 * 
 * ```typ
 * #set page(height: 100pt)
 * #block(
 *   fill: luma(230),
 *   inset: 8pt,
 *   radius: 4pt,
 *   lorem(30),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/ANNbdXVxvjEeHE66qUzAcwAAAAAAAAAA.png" alt="Preview" /><img src="https://typst.app/assets/docs/ANNbdXVxvjEeHE66qUzAcwAAAAAAAAAB.png" alt="Preview" />
 * 
 * Blocks are also useful to force elements that would otherwise be inline to become block-level, especially when writing show rules.
 * 
 * ```typ
 * #show heading: it => it.body
 * = Blockless
 * More text.
 * 
 * #show heading: it => block(it.body)
 * = Blocky
 * More text.
 * ```
 * <img src="https://typst.app/assets/docs/oxrD9vHAqcb-9gLEkFF_PQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("block")
data class TBlock(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The contents of the block.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional @all:Body val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The block's width.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val width: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The block's height. When the height is larger than the remaining space on a page and [`breakable`](https://typst.app/docs/reference/layout/block/#parameters-breakable) is `true`, the block will continue on the next page with the remaining height.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val height: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * Whether the block can be broken and continue on the next page.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val breakable: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The block's background color. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-fill) for more details.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The block's border color. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Option<TStroke>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * How much to round the block's corners. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-radius) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val radius: Corners<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * How much to pad the block's content. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val inset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * How much to expand the block's size without affecting the layout. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-outset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val outset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The spacing around the block. When `auto`, inherits the paragraph [`spacing`](https://typst.app/docs/reference/model/par/#parameters-spacing).
     * 
     * For two adjacent blocks, the larger of the first block's `above` and the second block's `below` spacing wins. Moreover, block spacing takes precedence over paragraph [`spacing`](https://typst.app/docs/reference/model/par/#parameters-spacing).
     * 
     * Note that this is only a shorthand to set `above` and `below` to the same value. Since the values for `above` and `below` might differ, a [context](https://typst.app/docs/reference/context/) block only provides access to `block.above` and `block.below`, not to `block.spacing` directly.
     * 
     * This property can be used in combination with a show rule to adjust the spacing around arbitrary block-level elements.
     * 
     * Typst type: relative|fraction
     */
    @all:Settable val spacing: Spacing? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The spacing between this block and its predecessor.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val above: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * The spacing between this block and its successor.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val below: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * Whether to clip the content inside the block.
     * 
     * Clipping is useful when the block's content is larger than the block itself, as any content that exceeds the block's bounds will be hidden.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val clip: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/block/](https://typst.app/docs/reference/layout/block/)
     * 
     * Whether this block must stick to the following one, with no break in between.
     * 
     * This is, by default, set on heading blocks to prevent orphaned headings at the bottom of the page.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val sticky: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM
    companion object {
        val ELEM = TElement("block")
    }
}

@SerialName("set-block")
data class TSetBlock(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Option<TContent>? = null,
    val width: Smart<TRelative>? = null,
    val height: Smart<Spacing>? = null,
    val breakable: TBool? = null,
    val fill: Option<TPaint>? = null,
    val stroke: Option<TStroke>? = null,
    val radius: Corners<TRelative>? = null,
    val inset: Sides<TRelative>? = null,
    val outset: Sides<TRelative>? = null,
    val above: Smart<Spacing>? = null,
    val below: Smart<Spacing>? = null,
    val clip: TBool? = null,
    val sticky: TBool? = null
) : TSetRule()
