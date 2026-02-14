package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.TAlternation

//!https://typst.app/docs/reference/foundations/bool/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/bool/](https://typst.app/docs/reference/foundations/bool/)
 * 
 * A type with two states.
 * 
 * The boolean type has two values: `true` and `false`. It denotes whether something is active or enabled.
 * 
 * **_Example_**
 * 
 * ```typ
 * #false \
 * #true \
 * #(1 < 2)
 * ```
 * <img src="https://typst.app/assets/docs/kY06WRyR--IwV2unWZl-NwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("bool")
data class TBool(val value: Boolean) : TValue, Smart<TBool>, Option<TBool>, TAlternation {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.BOOL
    }

    override fun repr(sb: StringBuilder)  {
        sb.append(value)
    }
}

/**
 * Convenience extension converting `Boolean` into the corresponding Typst value.
 */
val Boolean.t get() = TBool(this)
