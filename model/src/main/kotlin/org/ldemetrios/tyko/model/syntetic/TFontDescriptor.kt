package org.ldemetrios.tyko.model


sealed interface TFontDescriptor : IntoValue {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TDict<*> -> TFontDescriptorImpl.fromValue(value)
            is TStr -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class TFontDescriptorImpl(
    val name: TStr,
    val covers: TFontCoverage? = null
) : IntoDict<IntoValue>, TFontDescriptor, Self<TFontDescriptorImpl> {
    override fun intoValue(): TDict<IntoValue> = if (covers == null) {
        TDict(mapOf("name" to name))
    } else {
        TDict(mapOf("name" to name, "covers" to covers.intoValue()))
    }
    companion object {
        fun fromValue(value: TValue) = if (value is TDict<*>) TFontDescriptorImpl(
            value["name"]!!.intoValue() as TStr,
            value["covers"]?.intoValue()?.let { TFontCoverage.fromValue(it) }
        ) else throw AssertionError("Can't convert from $value")
    }
}
