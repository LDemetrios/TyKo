package org.ldemetrios.tyko.model

@SerialName("state")
data class TState(
    @all:Positional val key: TStr,
    @all:Positional val init: TValue? = null
) : TValue {
    override fun type(): TType = TType.STATE
}