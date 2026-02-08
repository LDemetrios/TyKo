package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/class/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Forced use of a certain math class.
 * 
 * This is useful to treat certain symbols as if they were of a different class, e.g. to make a symbol behave like a relation. The class of a symbol defines the way it is laid out, including spacing around it, and how its scripts are attached by default. Note that the latter can always be overridden using [`limits`](https://typst.app/docs/reference/math/attach/#functions-limits) and [`scripts`](https://typst.app/docs/reference/math/attach/#functions-scripts).
 * 
 * **_Example_**
 * 
 * ```typ
 * #let loves = math.class(
 *   "relation",
 *   sym.suit.heart,
 * )
 * 
 * $x loves y and y loves 5$
 * ```
 * <img src="https://typst.app/assets/docs/4-1urHqzMZfIf7fLTw_1MAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.class")
data class TMathClass(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The class to apply to the content.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val `class`: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to which the class is applied.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.class")
    }
}
