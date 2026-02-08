package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("sequence")
data class TSequence(
    @all:Positional val children: TArray<SequenceElement>,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("sequence")
    }
}
