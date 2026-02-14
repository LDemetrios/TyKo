package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/strong/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/strong/](https://typst.app/docs/reference/model/strong/)
 * 
 * Strongly emphasizes content by increasing the font weight.
 * 
 * Increases the current font weight by a given `delta`.
 * 
 * **_Example_**
 * 
 * ```typ
 * This is *strong.* \
 * This is #strong[too.] \
 * 
 * #show strong: set text(red)
 * And this is *evermore.*
 * ```
 * <img src="https://typst.app/assets/docs/8PFV4SUNXNbbYe9uHW1ITAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: To strongly emphasize content, simply enclose it in stars/asterisks (`*`). Note that this only works at word boundaries. To strongly emphasize part of a word, you have to use the function.
 */
@SerialName("strong")
data class TStrong(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/strong/](https://typst.app/docs/reference/model/strong/)
     * 
     * The content to strongly emphasize.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/strong/](https://typst.app/docs/reference/model/strong/)
     * 
     * The delta to apply on the font weight.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val delta: TInt? = null,
    override val label: TLabel? = null
) : TSelectableContent<TStrong>() {
    override fun elem(): TLocatableElement<TStrong> = ELEM

    companion object {
        val ELEM = TLocatableElement<TStrong>("strong")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TStrong]
 */
@SerialName("set-strong")
data class TSetStrong(
    override val internals: SetRuleInternals? = null,
    val delta: TInt? = null
) : TSetRule()
