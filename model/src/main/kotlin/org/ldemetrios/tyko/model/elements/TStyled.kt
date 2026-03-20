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

    override fun repr(sb: StringBuilder, registrar: (Any) -> Pair<Long, Long>) {
        sb.append("{")
        for (style in styles.value) {
            style.repr(sb, registrar)
            sb.append(";")
        }
        child.repr(sb, registrar)
        sb.append("}")
    }
}
