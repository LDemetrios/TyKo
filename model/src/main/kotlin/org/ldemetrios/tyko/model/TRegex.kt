package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("regex")
data class TRegex(@all:Positional val value: TStr) : TValue, TFontCoverage, Smart<TRegex>, TSelectable<TValue> { // TODO or not?
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.REGEX
    }
}
