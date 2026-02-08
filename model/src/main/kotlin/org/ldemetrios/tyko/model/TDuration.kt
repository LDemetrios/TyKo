package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("duration")
data class TDuration(
   val weeks : TInt? = null,
   val days : TInt? = null,
   val hours : TInt? = null,
   val minutes : TInt? = null,
   val seconds : TInt? = null,
) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DURATION
    }
}
