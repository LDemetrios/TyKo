package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/introspection/counter/#definitions-update
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Updates the value of the counter.
 * 
 * Just like with `step`, the update only occurs if you put the resulting content into the document.
 */
@SerialName("counter.update")
data class TCounterUpdate(
    /**
     * `self`, the counter.
     */
    val key: IntoCounter,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * If given an integer or array of integers, sets the counter to that value. If given a function, that function receives the previous counter value (with each number as a separate argument) and has to return the new value (integer or array).
     * 
     * Required, positional; Typst type: int|array|function
     */
    val update: Computable<TArray<TInt>>,
    override val label: TLabel? = null,
) : TSelectableContent<TCounterUpdate>() {
    override fun elem(): TLocatableElement<TCounterUpdate> = ELEM

    override fun reprImpl(sb: StringBuilder) {
        key.counter().repr(sb)
        sb.append(".update(")
        update.repr(sb)
        sb.append(")")
    }

    companion object {
        val ELEM = TLocatableElement<TCounterUpdate>("counter.update")
    }
}
