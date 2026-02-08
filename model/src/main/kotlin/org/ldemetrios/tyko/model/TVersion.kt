package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("version")
data class TVersion(@all:Variadic @all:SerialName("value") val components: TArray<TInt>) : TValue, Smart<TVersion> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.VERSION
    }
}
