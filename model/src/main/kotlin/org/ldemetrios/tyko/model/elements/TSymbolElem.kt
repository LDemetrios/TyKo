package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

@SerialName("symbol-elem")
data class TSymbolElem(
    @all:Positional val text: TStr,
    override val label: TLabel? = null
) : TSelectableContent<TSymbolElem>() {
    override fun elem(): TLocatableElement<TSymbolElem> = ELEM

    companion object {
        val ELEM = TLocatableElement<TSymbolElem>("symbol")
    }
}
