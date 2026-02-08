package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("math.align-point")
data class TMathAlignPoint(
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.align-point")
    }
}
