package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/html/elem/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * An HTML element that can contain Typst content.
 * 
 * Typst's HTML export automatically generates the appropriate tags for most elements. However, sometimes, it is desirable to retain more control. For example, when using Typst to generate your blog, you could use this function to wrap each article in an `<article>` tag.
 * 
 * Typst is aware of what is valid HTML. A tag and its attributes must form syntactically valid HTML. Some tags, like `meta` do not accept content. Hence, you must not provide a body for them. We may add more checks in the future, so be sure that you are generating valid HTML when using this function.
 * 
 * Normally, Typst will generate `html`, `head`, and `body` tags for you. If you instead create them with this function, Typst will omit its own tags.
 * 
 * ```typ
 * #html.elem("div", attrs: (style: "background: aqua"))[
 *   A div with _Typst content_ inside!
 * ]
 * ```
 */
@SerialName("html.elem")
data class THtmlElem(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The element's tag.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val tag: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The contents of the HTML element.
     * 
     * The body can be arbitrary Typst content.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The element's HTML attributes.
     * 
     * Settable; Typst type: dictionary
     */
    @all:Settable val attrs: TDict<TValue>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("html.elem")
    }
}


@SerialName("set-html.elem")
data class TSetHtmlElem(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Option<TContent>? = null,
    val attrs: TDict<IntoValue>? = null
) : TSetRule()
