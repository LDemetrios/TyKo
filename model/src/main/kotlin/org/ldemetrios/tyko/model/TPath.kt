package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("path")
data class TPath(@all:Positional val str: String) : TValue, Smart<TPath>, DataSource {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.PATH
    }
}
