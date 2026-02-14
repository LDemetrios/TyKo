package org.ldemetrios.tyko.model

import java.util.function.IntFunction
import kotlin.reflect.KType

import org.ldemetrios.tyko.model.TCollection

//!https://typst.app/docs/reference/foundations/array/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/array/](https://typst.app/docs/reference/foundations/array/)
 * 
 * A sequence of values.
 * 
 * You can construct an array by enclosing a comma-separated sequence of values in parentheses. The values do not have to be of the same type.
 * 
 * You can access and update array items with the `.at()` method. Indices are zero-based and negative indices wrap around to the end of the array. You can iterate over an array using a [for loop](https://typst.app/docs/reference/scripting/#loops). Arrays can be added together with the `+` operator, [joined together](https://typst.app/docs/reference/scripting/#blocks) and multiplied with integers.
 * 
 * __Note:__ An array of length one needs a trailing comma, as in `(1,)`. This is to disambiguate from a simple parenthesized expressions like `(1 + 2) * 3`. An empty array is written as `()`.
 * 
 * **_Example_**
 * 
 * ```typ
 * #let values = (1, 7, 4, -3, 2)
 * 
 * #values.at(0) \
 * #(values.at(0) = 3)
 * #values.at(-1) \
 * #values.find(calc.even) \
 * #values.filter(calc.odd) \
 * #values.map(calc.abs) \
 * #values.rev() \
 * #(1, (2, 3)).flatten() \
 * #(("A", "B", "C")
 *     .join(", ", last: " and "))
 * ```
 * <img src="https://typst.app/assets/docs/uC3P-2nGePaWZlTLapiUowAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("array")
data class TArray<out T : IntoValue>(val value: List<T>) : TValue, Option<TArray<T>>, ArrayOrSingle<T>, TCollection<T>,
    Computable<TArray<T>>, List<T> by value {
    override fun type(): TType = TYPE

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): TArray<T> = when (value) {
            is TArray<*> -> TArray(value.value.map { it.intoValue().into<T>() })
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TArray<*> {
            val arg = type.arguments.firstOrNull()?.type ?: return fromValue<IntoValue>(value)
            val array = value as TArray<*>
            return TArray(array.value.map { (it as TValue).into(arg) })
        }

        val TYPE = TType.ARRAY
    }

    @Deprecated("")
    override fun <T : Any?> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? {
        return super.toArray(generator)
    }

    override fun repr(sb: StringBuilder) {
        sb.append("(")
        value.forEach {
            it.repr(sb)
            sb.append(",")
        }
        sb.append(")")
    }
}

/**
 * Convenience extension converting `<T : IntoValue> List<T>` into the corresponding Typst value.
 */
val <T : IntoValue> List<T>.t get() = TArray(this)
