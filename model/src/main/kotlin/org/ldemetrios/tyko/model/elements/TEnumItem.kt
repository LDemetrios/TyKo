package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/enum/#definitions-item
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/enum/#definitions-item](https://typst.app/docs/reference/model/enum/#definitions-item)
 * 
 * An enumeration item.
 */
@SerialName("enum.item")
data class TEnumItem(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/#definitions-item](https://typst.app/docs/reference/model/enum/#definitions-item)
     * 
     * The item's number.
     * 
     * Positional, settable; Typst type: auto|int
     */
    @all:Settable @all:Positional val number: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/enum/#definitions-item](https://typst.app/docs/reference/model/enum/#definitions-item)
     * 
     * The item's body.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("enum.item")
    }

    public constructor(
        body: TContent,
        label: TLabel? = null
    ) : this(null, body, label)
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TEnumItem]
 */
@SerialName("set-enum.item")
data class TSetEnumItem(
    override val internals: SetRuleInternals? = null,
    @all:Positional val number: Smart<TInt>? = null
) : TSetRule()
