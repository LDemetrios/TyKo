package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


//!https://typst.app/docs/reference/foundations/arguments/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/arguments/](https://typst.app/docs/reference/foundations/arguments/)
 * 
 * Captured arguments to a function.
 * 
 * **_Argument Sinks_**
 * 
 * Like built-in functions, custom functions can also take a variable number of arguments. You can specify an *argument sink* which collects all excess arguments as `..sink`. The resulting `sink` value is of the `arguments` type. It exposes methods to access the positional and named arguments.
 * 
 * ```typ
 * #let format(title, ..authors) = {
 *   let by = authors
 *     .pos()
 *     .join(", ", last: " and ")
 * 
 *   [*#title* \ _Written by #by;_]
 * }
 * 
 * #format("ArtosFlow", "Jane", "Joe")
 * ```
 * <img src="https://typst.app/assets/docs/DWzn69gGuCd1q_LVZvjEEgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Spreading_**
 * 
 * Inversely to an argument sink, you can *spread* arguments, arrays and dictionaries into a function call with the `..spread` operator:
 * 
 * ```typ
 * #let array = (2, 3, 5)
 * #calc.min(..array)
 * #let dict = (fill: blue)
 * #text(..dict)[Hello]
 * ```
 * <img src="https://typst.app/assets/docs/kcmqtH9qxq6Bg8ZwwKnMCQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("args")
data class TArgs<out T : IntoValue>(val positional: TArray<T>, val named: TDict<T>) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.ARGS

        fun of(vararg arguments: Argument) = TArgs(
            TArray(arguments.filterIsInstance<IntoValue>()),
            TDict(arguments.filterIsInstance<NamedArgument>().associate { (k, v) -> k to v })
        )
    }

    override fun repr(sb: StringBuilder) {
        sb.append("arguments(")
        sb.append("..")
        positional.repr(sb)
        sb.append(", ..")
        named.repr(sb)
        sb.append(")")
    }
}
