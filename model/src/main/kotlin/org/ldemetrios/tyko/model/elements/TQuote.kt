package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/quote/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/quote/](https://typst.app/docs/reference/model/quote/)
 * 
 * Displays a quote alongside an optional attribution.
 * 
 * **_Example_**
 * 
 * ```typ
 * Plato is often misquoted as the author of #quote[I know that I know
 * nothing], however, this is a derivation form his original quote:
 * 
 * #set quote(block: true)
 * 
 * #quote(attribution: [Plato])[
 *   ... ἔοικα γοῦν τούτου γε σμικρῷ τινι αὐτῷ τούτῳ σοφώτερος εἶναι, ὅτι
 *   ἃ μὴ οἶδα οὐδὲ οἴομαι εἰδέναι.
 * ]
 * #quote(attribution: [from the Henry Cary literal translation of 1897])[
 *   ... I seem, then, in just this little thing to be wiser than this man at
 *   any rate, that what I do not know I do not think I know either.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/SJpe1zkhE_liZRMF5cAy4gAAAAAAAAAA.png" alt="Preview" />
 * 
 * By default block quotes are padded left and right by `1em`, alignment and padding can be controlled with show rules:
 * 
 * ```typ
 * #set quote(block: true)
 * #show quote: set align(center)
 * #show quote: set pad(x: 5em)
 * 
 * #quote[
 *   You cannot pass... I am a servant of the Secret Fire, wielder of the
 *   flame of Anor. You cannot pass. The dark fire will not avail you,
 *   flame of Udûn. Go back to the Shadow! You cannot pass.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/QLNv4Pfp0zBKSvwxIfby-wAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("quote")
data class TQuote(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/quote/](https://typst.app/docs/reference/model/quote/)
     * 
     * The quote.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/quote/](https://typst.app/docs/reference/model/quote/)
     * 
     * Whether this is a block quote.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val block: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/quote/](https://typst.app/docs/reference/model/quote/)
     * 
     * Whether double quotes should be added around this quote.
     * 
     * The double quotes used are inferred from the `quotes` property on [smartquote](https://typst.app/docs/reference/text/smartquote/), which is affected by the `lang` property on [text](https://typst.app/docs/reference/text/text/).
     * 
     * - `true`: Wrap this quote in double quotes.
     * - `false`: Do not wrap this quote in double quotes.
     * - `auto`: Infer whether to wrap this quote in double quotes based on the `block` property. If `block` is `false`, double quotes are automatically added.
     * 
     * Settable; Typst type: auto|bool
     */
    @all:Settable val quotes: Smart<TBool>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/quote/](https://typst.app/docs/reference/model/quote/)
     * 
     * The attribution of this quote, usually the author or source. Can be a label pointing to a bibliography entry or any content. By default only displayed for block quotes, but can be changed using a `show` rule.
     * 
     * Settable; Typst type: none|label|content
     */
    @all:Settable val attribution: Option<Attribution>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TQuote>() {
    override fun elem(): TLocatableElement<TQuote> = ELEM

    companion object {
        val ELEM = TLocatableElement<TQuote>("quote")
    }
}


@SerialName("set-quote")
data class TSetQuote(
    override val internals: SetRuleInternals? = null,
    val block: TBool? = null,
    val quotes: Smart<TBool>? = null,
    val attribution: Option<Attribution>? = null
) : TSetRule()
