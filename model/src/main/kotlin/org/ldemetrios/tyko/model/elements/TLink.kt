package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


sealed interface TLinkDestination : IntoValue {
    companion object {
        fun fromValue(value: TValue): TLinkDestination = when (value) {
            is TDict<*> -> LocationOnPage.fromValue(value)
            is TStr -> value
            is TLabel -> value
            is TLocation -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class LocationOnPage(val page: TInt, val x: TLength, val y: TLength) : IntoDict<IntoValue>, TLinkDestination {
    override fun intoValue(): TDict<IntoValue> = TDict(
        mapOf(
            "page" to page,
            "x" to x,
            "y" to y
        )
    )

    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TDict<*> -> LocationOnPage(
                value["page"]!!.intoValue() as TInt,
                value["x"]!!.intoValue() as TLength,
                value["y"]!!.intoValue() as TLength
            )
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

//!https://typst.app/docs/reference/model/link/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Links to a URL or a location in the document.
 * 
 * By default, links do not look any different from normal text. However, you can easily apply a style of your choice with a show rule.
 * 
 * **_Example_**
 * 
 * ```typ
 * #show link: underline
 * 
 * https://example.com \
 * 
 * #link("https://example.com") \
 * #link("https://example.com")[
 *   See example.com
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/mBfQJYO4ObjIyuLi_FjKfgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: Text that starts with `http://` or `https://` is automatically turned into a link.
 * 
 * **_Hyphenation_**
 * 
 * If you enable hyphenation or justification, by default, it will not apply to links to prevent unwanted hyphenation in URLs. You can opt out of this default via `show link: set text(hyphenate: true)`.
 * 
 * **_Accessibility_**
 * 
 * The destination of a link should be clear from the link text itself, or at least from the text immediately surrounding it. In PDF export, Typst will automatically generate a tooltip description for links based on their destination. For links to URLs, the URL itself will be used as the tooltip.
 * 
 * **_Links in HTML export_**
 * 
 * In HTML export, a link to a [label](https://typst.app/docs/reference/foundations/label/) or [location](https://typst.app/docs/reference/introspection/location/) will be turned into a fragment link to a named anchor point. To support this, targets without an existing ID will automatically receive an ID in the DOM. How this works varies by which kind of HTML node(s) the link target turned into:
 * 
 * - If the link target turned into a single HTML element, that element will receive the ID. This is, for instance, typically the case when linking to a top-level heading (which turns into a single `<h2>` element).
 * - If the link target turned into a single text node, the node will be wrapped in a `<span>`, which will then receive the ID.
 * - If the link target turned into multiple nodes, the first node will receive the ID.
 * - If the link target turned into no nodes at all, an empty span will be generated to serve as a link target.
 * 
 * If you rely on a specific DOM structure, you should ensure that the link target turns into one or multiple elements, as the compiler makes no guarantees on the precise segmentation of text into text nodes.
 * 
 * If present, the automatic ID generation tries to reuse the link target's label to create a human-readable ID. A label can be reused if:
 * 
 * - All characters are alphabetic or numeric according to Unicode, or a hyphen, or an underscore.
 * - The label does not start with a digit or hyphen.
 * 
 * These rules ensure that the label is both a valid CSS identifier and a valid URL fragment for linking.
 * 
 * As IDs must be unique in the DOM, duplicate labels might need disambiguation when reusing them as IDs. The precise rules for this are as follows:
 * 
 * - If a label can be reused and is unique in the document, it will directly be used as the ID.
 * - If it's reusable, but not unique, a suffix consisting of a hyphen and an integer will be added. For instance, if the label `<mylabel>` exists twice, it would turn into `mylabel-1` and `mylabel-2`.
 * - Otherwise, a unique ID of the form `loc-` followed by an integer will be generated.
 */
@SerialName("link")
data class TLink(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The destination the link points to.
     * 
     * - To link to web pages, `dest` should be a valid URL string. If the URL is in the `mailto:` or `tel:` scheme and the `body` parameter is omitted, the email address or phone number will be the link's body, without the scheme.
     * - To link to another part of the document, `dest` can take one of three forms:
     *   - A [label](https://typst.app/docs/reference/foundations/label/) attached to an element. If you also want automatic text for the link based on the element, consider using a [reference](https://typst.app/docs/reference/model/ref/) instead.
     *   - A [`location`](https://typst.app/docs/reference/introspection/location/) (typically retrieved from [`here`](https://typst.app/docs/reference/introspection/here/), [`locate`](https://typst.app/docs/reference/introspection/locate/) or [`query`](https://typst.app/docs/reference/introspection/query/)).
     *   - A dictionary with a `page` key of type [integer](https://typst.app/docs/reference/foundations/int/) and `x` and `y` coordinates of type [length](https://typst.app/docs/reference/layout/length/). Pages are counted from one, and the coordinates are relative to the page's top left corner.
     * 
     * Required, positional; Typst type: str|label|location|dictionary
     */
    @all:Positional val dest: TLinkDestination,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content that should become a link.
     * 
     * If `dest` is an URL string, the parameter can be omitted. In this case, the URL will be shown as the link.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TSelectableContent<TLink>() {
    override fun elem(): TLocatableElement<TLink> = ELEM

    companion object {
        val ELEM = TLocatableElement<TLink>("link")
    }
}
