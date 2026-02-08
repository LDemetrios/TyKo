package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("space")
data class TSpace(
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("space")
    }

    override fun repr(sb: StringBuilder) {
        sb.append( "[ ]")
    }
}
