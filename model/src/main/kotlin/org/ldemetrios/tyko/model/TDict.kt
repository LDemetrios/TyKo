package org.ldemetrios.tyko.model

import kotlin.reflect.KType

import org.ldemetrios.tyko.model.TCollection

//!https://typst.app/docs/reference/foundations/dictionary/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/dictionary/](https://typst.app/docs/reference/foundations/dictionary/)
 * 
 * A map from string keys to values.
 * 
 * You can construct a dictionary by enclosing comma-separated `key: value` pairs in parentheses. The values do not have to be of the same type. Since empty parentheses already yield an empty array, you have to use the special `(:)` syntax to create an empty dictionary.
 * 
 * A dictionary is conceptually similar to an [array](https://typst.app/docs/reference/foundations/array/), but it is indexed by strings instead of integers. You can access and create dictionary entries with the `.at()` method. If you know the key statically, you can alternatively use [field access notation](https://typst.app/docs/reference/scripting/#fields) (`.key`) to access the value. To check whether a key is present in the dictionary, use the `in` keyword.
 * 
 * You can iterate over the pairs in a dictionary using a [for loop](https://typst.app/docs/reference/scripting/#loops). This will iterate in the order the pairs were inserted / declared initially.
 * 
 * Dictionaries can be added with the `+` operator and [joined together](https://typst.app/docs/reference/scripting/#blocks). They can also be [spread](https://typst.app/docs/reference/foundations/arguments/#spreading) into a function call or another dictionary with the `..spread` operator. In each case, if a key appears multiple times, the last value will override the others.
 * 
 * **_Example_**
 * 
 * ```typ
 * #let dict = (
 *   name: "Typst",
 *   born: 2019,
 * )
 * 
 * #dict.name \
 * #(dict.launch = 20)
 * #dict.len() \
 * #dict.keys() \
 * #dict.values() \
 * #dict.at("born") \
 * #dict.insert("city", "Berlin")
 * #("name" in dict)
 * ```
 * <img src="https://typst.app/assets/docs/6aReiL9XIA-r_ry2aRN0EQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("dict")
data class TDict<out V: IntoValue>(val value: Map<String, V>) : TValue, Map<String, V> by value, IntoDict<V>,
    TCollection<V> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DICT

        inline fun <reified V: IntoValue> fromValue(value: TValue): TDict<V> = when (value) {
            is TDict<*> -> TDict(value.value.mapValues { it.value.intoValue().into<V>() })
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TDict<*> {
            val arg = type.arguments.firstOrNull()?.type ?: return fromValue<IntoValue>(value)
            val dict = value as TDict<*>
            return TDict(dict.value.mapValues { (it.value as TValue).into(arg) })
        }
    }

    override fun intoValue(): TDict<V> = this

    override fun repr(sb: StringBuilder) {
        if (value.isEmpty()) {
            sb.append("(:)")
        } else {
            sb.append("(")
            value.forEach { (key, value) ->
                sb.append(key)
                sb.append(":")
                value.repr(sb)
                sb.append(", ")
            }
            sb.append(")")
        }
    }
}
