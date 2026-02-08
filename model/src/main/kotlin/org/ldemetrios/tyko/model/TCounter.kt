package org.ldemetrios.tyko.model

@UnionType(["str", "label", "selector", "location", "function", "counter"])
sealed interface IntoCounter: TValue {
    fun counter() : TCounter
}

@UnionType(["str", "label", "selector", "location", "function"])
sealed interface TCounterKey : TValue, IntoCounter {
    override fun counter(): TCounter = TCounter(this)
}

@SerialName("counter")
data class TCounter(@all:Positional val key: TCounterKey) : TValue,
    TCounterKey { // TCounter: TCounterKey, this is ridiculous...
    override fun type(): TType = TType.COUNTER
    override fun counter(): TCounter = this
}