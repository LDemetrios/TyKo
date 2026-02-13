package org.ldemetrios.tyko.model

import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/introspection/counter/#definitions-step
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/introspection/counter/#definitions-step](https://typst.app/docs/reference/introspection/counter/#definitions-step)
 * 
 * Increases the value of the counter by one.
 * 
 * The update will be in effect at the position where the returned content is inserted into the document. If you don't put the output into the document, nothing happens! This would be the case, for example, if you write `let _ = counter(page).step()`. Counter updates are always applied in layout order and in that case, Typst wouldn't know when to step the counter.
 */
@SerialName("counter.step")
data class TCounterStep(
    /**
     * `self`, the counter.
     */
    val key: IntoCounter,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/introspection/counter/#definitions-step](https://typst.app/docs/reference/introspection/counter/#definitions-step)
     * 
     * The depth at which to step the counter. Defaults to `1`.
     * 
     * Typst type: int
     */
    val level: TInt,
    override val label: TLabel? = null,
) : TSelectableContent<TCounterStep>() {
    override fun elem(): TLocatableElement<TCounterStep> = ELEM

    override fun reprImpl(sb: StringBuilder) {
        key.counter().repr(sb)
        sb.append(".step(level:")
        level.repr(sb)
        sb.append(")")
    }

    companion object {
        val ELEM = TLocatableElement<TCounterStep>("counter.step")
    }
}
