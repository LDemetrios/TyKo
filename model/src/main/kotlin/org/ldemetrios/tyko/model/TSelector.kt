package org.ldemetrios.tyko.model

/**
 * Generated based on: [https://typst.app/docs/reference/foundations/selector/](https://typst.app/docs/reference/foundations/selector/)
 * 
 * A filter for selecting elements within the document.
 * 
 * To construct a selector you can:
 * 
 * - use an [element function](https://typst.app/docs/reference/foundations/function/#element-functions)
 * - filter for an element function with [specific fields](https://typst.app/docs/reference/foundations/function/#definitions-where)
 * - use a [string](https://typst.app/docs/reference/foundations/str/) or [regular expression](https://typst.app/docs/reference/foundations/regex/)
 * - use a [`<label>`](https://typst.app/docs/reference/foundations/label/)
 * - use a [`location`](https://typst.app/docs/reference/introspection/location/)
 * - call the [`selector`](https://typst.app/docs/reference/foundations/selector/) constructor to convert any of the above types into a selector value and use the methods below to refine it
 * 
 * Selectors are used to [apply styling rules](https://typst.app/docs/reference/styling/#show-rules) to elements. You can also use selectors to [query](https://typst.app/docs/reference/introspection/query/) the document for certain types of elements.
 * 
 * Furthermore, you can pass a selector to several of Typst's built-in functions to configure their behaviour. One such example is the [outline](https://typst.app/docs/reference/model/outline/) where it can be used to change which elements are listed within the outline.
 * 
 * Multiple selectors can be combined using the methods shown below. However, not all kinds of selectors are supported in all places, at the moment.
 * 
 * **_Example_**
 * 
 * ```typ
 * #context query(
 *   heading.where(level: 1)
 *     .or(heading.where(level: 2))
 * )
 * 
 * = This will be found
 * == So will this
 * === But this will not.
 * ```
 * <img src="https://typst.app/assets/docs/SW-2iLP1LIGQ0ITsB7LGEQAAAAAAAAAA.png" alt="Preview" />
 */
sealed interface TSelector<out S : TValue> : TCounterKey, TValue {
    override fun type(): TType = TYPE

    fun reprAsSelector(sb: StringBuilder) = repr(sb)

    companion object {
        val TYPE = TType.SELECTOR
    }
}

/**
 * Builds a selector that matches if any of the given selectors match.
 */
fun <S : TValue> TSelector<S>.or(vararg others: TSelector<S>) = TOrSelector(TArray(listOf(this, *others)))
/**
 * Builds a selector that matches only when all given selectors match.
 */
fun <S : TValue> TSelector<S>.and(vararg others: TSelector<*>): TSelector<S> =
    TAndSelector(TArray(listOf(this, *others))) as TSelector<S>

/**
 * Constrains selector matches to content before the `end` selector.
 */
fun <S : TValue> TSelector<S>.before(end: TSelector<*>, inclusive: Boolean = true) =
    TBeforeSelector(this, end, inclusive)

/**
 * Constrains selector matches to content after the `start` selector.
 */
fun <S : TValue> TSelector<S>.after(start: TSelector<*>, inclusive: Boolean = true) =
    TAfterSelector(this, start, inclusive)

/**
 * Represents something that doesn't have a `selector` type in Typst, but can be used as a selector in specific instances,
 * in others can be explicitly transformed into a selector.
 */
sealed interface TSelectable<out S : TValue> : TSelector<S> {
    override fun reprAsSelector(sb: StringBuilder) {
        sb.append("selector(")
        repr(sb)
        sb.append(")")
    }
}

/**
 * Selector, which has been constructed via [selector.or](https://typst.app/docs/reference/foundations/selector/#definitions-or)
 * function in Typst, or [TSelector.or] in Kotlin.
 */
@SerialName("selector.or")
data class TOrSelector<out S : TValue>(val options: TArray<TSelector<S>>) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        options.first<TSelector<S>>().reprAsSelector(sb)
        if (options.size > 1) {
            sb.append(".or(")
            options.drop(1).forEach { it.reprAsSelector(sb); sb.append(",") }
            sb.append(")")
        }
    }
}

/**
 * Selector, which has been constructed via [selector.and](https://typst.app/docs/reference/foundations/selector/#definitions-or)
 * function in Typst, or [TSelector.and] in Kotlin.
 */
@SerialName("selector.and")
data class TAndSelector<S : TValue>(val options: TArray<TSelector<S>>) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        options.first<TSelector<S>>().reprAsSelector(sb)
        if (options.size > 1) {
            sb.append(".and(")
            options.drop(1).forEach { it.reprAsSelector(sb); sb.append(",") }
            sb.append(")")
        }
    }
}

/**
 * Selector, which has been constructed via [selector.before](https://typst.app/docs/reference/foundations/selector/#definitions-or)
 * function in Typst, or [TSelector.before] in Kotlin.
 */
@SerialName("selector.before")
data class TBeforeSelector<S : TValue>(
    val selector: TSelector<S>,
    val end: TSelector<*>,
    val inclusive: Boolean = true
) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        selector.reprAsSelector(sb)
        sb.append(".before(")
        end.repr(sb)
        if (!inclusive) {
            sb.append(", inclusive: false")
        }
        sb.append(")")
    }
}

/**
 * Selector, which has been constructed via [selector.after](https://typst.app/docs/reference/foundations/selector/#definitions-or)
 * function in Typst, or [TSelector.after] in Kotlin.
 */
@SerialName("selector.after")
data class TAfterSelector<S : TValue>(
    val selector: TSelector<S>,
    val start: TSelector<*>,
    val inclusive: Boolean = true
) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        selector.reprAsSelector(sb)
        sb.append(".after(")
        start.repr(sb)
        if (!inclusive) {
            sb.append(", inclusive: false")
        }
        sb.append(")")
    }
}

/**
 * Selector, which has been constructed via [selector.where](https://typst.app/docs/reference/foundations/selector/#definitions-or)
 * function in Typst, or [TSelectableFunc.where] in Kotlin.
 */
@SerialName("selector.where")
data class TWhereSelector<S : TContent>(val func: TSelectableFunc<S>, val args: TArgs<*>) : TSelector<S> {
    override fun repr(sb: StringBuilder) {
        func.repr(sb)
        sb.append(".where(..")
        args.repr(sb)
        sb.append(")")
    }
}
