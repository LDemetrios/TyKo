package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Attribution

//!https://typst.app/docs/reference/foundations/label/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/label/](https://typst.app/docs/reference/foundations/label/)
 * 
 * A label for an element.
 * 
 * Inserting a label into content attaches it to the closest preceding element that is not a space. The preceding element must be in the same scope as the label, which means that `Hello #[<label>]`, for instance, wouldn't work.
 * 
 * A labelled element can be [referenced](https://typst.app/docs/reference/model/ref/), [queried](https://typst.app/docs/reference/introspection/query/) for, and [styled](https://typst.app/docs/reference/styling/) through its label.
 * 
 * Once constructed, you can get the name of a label using [`str`](https://typst.app/docs/reference/foundations/str/#constructor).
 * 
 * **_Example_**
 * 
 * ```typ
 * #show <a>: set text(blue)
 * #show label("b"): set text(red)
 * 
 * = Heading <a>
 * *Strong* #label("b")
 * ```
 * <img src="https://typst.app/assets/docs/l3ZXI9iv-ZpcNuL82oagnwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: You can create a label by enclosing its name in angle brackets. This works both in markup and code. A label's name can contain letters, numbers, `_`, `-`, `:`, and `.`. A label cannot be empty.
 * 
 * Note that there is a syntactical difference when using the dedicated syntax for this function. In the code below, the `<a>` terminates the heading and thus attaches to the heading itself, whereas the `#label("b")` is part of the heading and thus attaches to the heading's text.
 * 
 * ```typ
 * // Equivalent to `#heading[Introduction] <a>`.
 * = Introduction <a>
 * 
 * // Equivalent to `#heading[Conclusion #label("b")]`.
 * = Conclusion #label("b")
 * ```
 * 
 * Currently, labels can only be attached to elements in markup mode, not in code mode. This might change in the future.
 */
@SerialName("label")
data class TLabel(@all: Positional val value: TStr) : TValue, TSelectable<TValue>, Option<TLabel>, Attribution,TCounterKey, TLinkDestination {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.LABEL
    }
}
