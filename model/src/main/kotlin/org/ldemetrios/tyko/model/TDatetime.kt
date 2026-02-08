package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("datetime")
data class TDatetime(
    val year: TInt? = null,
    val month: TInt? = null,
    val day: TInt? = null,
    val hour: TInt? = null,
    val minute: TInt? = null,
    val second: TInt? = null,
) : TValue, DocumentDatetime {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DATETIME
    }
}
