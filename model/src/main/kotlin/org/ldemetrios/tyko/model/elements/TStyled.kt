package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("styled")
data class TStyled(
    @all:Positional val styles: TStyles,
    @all:Positional val child: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("styled")
    }

    override fun repr(sb: StringBuilder) {
        sb.append("{")
        for (style in styles.value) {
            style.repr(sb)
            sb.append(";")
        }
        child.repr(sb)
        sb.append("}")
    }
}
