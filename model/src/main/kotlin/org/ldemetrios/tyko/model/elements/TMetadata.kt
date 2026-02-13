package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/introspection/metadata/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/introspection/metadata/](https://typst.app/docs/reference/introspection/metadata/)
 * 
 * Exposes a value to the query system without producing visible content.
 * 
 * This element can be retrieved with the [`query`](https://typst.app/docs/reference/introspection/query/) function and from the command line with [`typst query`](https://typst.app/docs/reference/introspection/query/#command-line-queries). Its purpose is to expose an arbitrary value to the introspection system. To identify a metadata value among others, you can attach a [`label`](https://typst.app/docs/reference/foundations/label/) to it and query for that label.
 * 
 * The `metadata` element is especially useful for command line queries because it allows you to expose arbitrary values to the outside world.
 * 
 * ```typ
 * // Put metadata somewhere.
 * #metadata("This is a note") <note>
 * 
 * // And find it from anywhere else.
 * #context {
 *   query(<note>).first().value
 * }
 * ```
 * <img src="https://typst.app/assets/docs/sbF_Ac863-gI1m3qoL9avwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("metadata")
data class TMetadata<out D: IntoValue>(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/introspection/metadata/](https://typst.app/docs/reference/introspection/metadata/)
     * 
     * The value to embed into the document.
     * 
     * Required, positional; Typst type: any
     */
    @all:Positional val value: D,
    override val label: TLabel? = null
) : TSelectableContent<TMetadata<*>>() {
    override fun elem(): TLocatableElement<TMetadata<*>> = ELEM

    companion object {
        val ELEM = TLocatableElement<TMetadata<*>>("metadata")
    }
}
