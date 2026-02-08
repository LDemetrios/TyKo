package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/layout/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Provides access to the current outer container's (or page's, if none) dimensions (width and height).
 * 
 * Accepts a function that receives a single parameter, which is a dictionary with keys `width` and `height`, both of type [`length`](https://typst.app/docs/reference/layout/length/). The function is provided [context](https://typst.app/docs/reference/context/), meaning you don't need to use it in combination with the `context` keyword. This is why [`measure`](https://typst.app/docs/reference/layout/measure/) can be called in the example below.
 * 
 * ```typ
 * #let text = lorem(30)
 * #layout(size => [
 *   #let (height,) = measure(
 *     width: size.width,
 *     text,
 *   )
 *   This text is #height high with
 *   the current page width: \
 *   #text
 * ])
 * ```
 * <img src="https://typst.app/assets/docs/zHvMpi5pf_sw3zQ4Vw2xqwAAAAAAAAAA.png" alt="Preview" />
 * 
 * Note that the `layout` function forces its contents into a [block](https://typst.app/docs/reference/layout/block/)-level container, so placement relative to the page or pagebreaks are not possible within it.
 * 
 * If the `layout` call is placed inside a box with a width of `800pt` and a height of `400pt`, then the specified function will be given the argument `(width: 800pt, height: 400pt)`. If it is placed directly into the page, it receives the page's dimensions minus its margins. This is mostly useful in combination with [measurement](https://typst.app/docs/reference/layout/measure/).
 * 
 * To retrieve the *remaining* height of the page rather than its full size, you can wrap your `layout` call in a `block(height: 1fr)`. This works because the block automatically grows to fill the remaining space (see the [fraction](https://typst.app/docs/reference/layout/fraction/) documentation for more details).
 * 
 * ```typ
 * #set page(height: 150pt)
 * 
 * #lorem(20)
 * 
 * #block(height: 1fr, layout(size => [
 *   Remaining height: #size.height
 * ]))
 * ```
 * <img src="https://typst.app/assets/docs/KHhhuOMSnLMBt0r6kv96BwAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can also use this function to resolve a [`ratio`](https://typst.app/docs/reference/layout/ratio/) to a fixed length. This might come in handy if you're building your own layout abstractions.
 * 
 * ```typ
 * #layout(size => {
 *   let half = 50% * size.width
 *   [Half a page is #half wide.]
 * })
 * ```
 * <img src="https://typst.app/assets/docs/1AoOPrEARH2i9ZcdcamicAAAAAAAAAAA.png" alt="Preview" />
 * 
 * Note that the width or height provided by `layout` will be infinite if the corresponding page dimension is set to `auto`.
 */
@SerialName("layout")
data class TLayout(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A function to call with the outer container's size. Its return value is displayed in the document.
     * 
     * The container's size is given as a [dictionary](https://typst.app/docs/reference/foundations/dictionary/) with the keys `width` and `height`, both of type [`length`](https://typst.app/docs/reference/layout/length/).
     * 
     * This function is called once for each time the content returned by `layout` appears in the document. This makes it possible to generate content that depends on the dimensions of its container.
     * 
     * Required, positional; Typst type: function
     */
    @all:Positional val func: TFunc,
    override val label: TLabel? = null
) : TSelectableContent<TLayout>() {
    override fun elem(): TLocatableElement<TLayout> = ELEM

    companion object {
        val ELEM = TLocatableElement<TLayout>("layout")
    }
}
