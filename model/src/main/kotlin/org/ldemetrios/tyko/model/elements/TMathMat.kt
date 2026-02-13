package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


sealed interface TMatAugment: IntoValue {
    companion object {
        fun fromValue(value: TValue) : TMatAugment = when (value) {
            is TDict<*> -> TMatAugmentDict.fromValue(value)
            is TInt -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
@SerialName("dict")

data class TMatAugmentDict(
    val hline: ArrayOrSingle<TInt>? = null,
    val vline: ArrayOrSingle<TInt>? = null,
    val stroke: Smart<TStroke>? = null
) : IntoDict<IntoValue>, TMatAugment, Option<TMatAugmentDict> {
    override fun intoValue(): TDict<IntoValue> = TDict(
        mapOfNotNullValues(
            "hline" to hline,
            "vline" to vline,
            "stroke" to stroke.takeIf { it != TAuto }
        )
    )
    companion object {
        fun fromValue(value: TValue) = if(value is TDict<*>) TMatAugmentDict(
            value["hline"]?.intoValue()?.let { ArrayOrSingle.fromValue<TInt>(it) },
            value["vline"]?.intoValue()?.let { ArrayOrSingle.fromValue<TInt>(it) },
            value["stroke"]?.intoValue()?.let { Smart.fromValue<TStroke>(it) },
        ) else throw AssertionError("Can't convert from $value")
    }
}

//!https://typst.app/docs/reference/math/mat/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
 * 
 * A matrix.
 * 
 * The elements of a row should be separated by commas, while the rows themselves should be separated by semicolons. The semicolon syntax merges preceding arguments separated by commas into an array. You can also use this special syntax of math function calls to define custom functions that take 2D data.
 * 
 * Content in cells can be aligned with the [`align`](https://typst.app/docs/reference/math/mat/#parameters-align) parameter, or content in cells that are in the same row can be aligned with the `&` symbol.
 * 
 * **_Example_**
 * 
 * ```typ
 * $ mat(
 *   1, 2, ..., 10;
 *   2, 2, ..., 10;
 *   dots.v, dots.v, dots.down, dots.v;
 *   10, 10, ..., 10;
 * ) $
 * ```
 * <img src="https://typst.app/assets/docs/yiSilYGQ1wRBpIK3ON349AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.mat")
data class TMathMat(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * An array of arrays with the rows of the matrix.
     * 
     * Required, positional, variadic; Typst type: array
     */
    @all:Variadic @all:Positional val rows: TArray<TArray<SequenceElement>>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * The delimiter to use.
     * 
     * Can be a single character specifying the left delimiter, in which case the right delimiter is inferred. Otherwise, can be an array containing a left and a right delimiter.
     * 
     * Settable; Typst type: none|str|array|symbol
     */
    @all:Settable val delim: ArrayOrSingle<Option<TSymbolLike>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * The horizontal alignment that each cell should have.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val align: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * Draws augmentation lines in a matrix.
     * 
     * - `none`: No lines are drawn.
     * - A single number: A vertical augmentation line is drawn after the specified column number. Negative numbers start from the end.
     * - A dictionary: With a dictionary, multiple augmentation lines can be drawn both horizontally and vertically. Additionally, the style of the lines can be set. The dictionary can contain the following keys:
     *   - `hline`: The offsets at which horizontal lines should be drawn. For example, an offset of `2` would result in a horizontal line being drawn after the second row of the matrix. Accepts either an integer for a single line, or an array of integers for multiple lines. Like for a single number, negative numbers start from the end.
     *   - `vline`: The offsets at which vertical lines should be drawn. For example, an offset of `2` would result in a vertical line being drawn after the second column of the matrix. Accepts either an integer for a single line, or an array of integers for multiple lines. Like for a single number, negative numbers start from the end.
     *   - `stroke`: How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the line. If set to `auto`, takes on a thickness of 0.05 em and square line caps.
     * 
     * Settable; Typst type: none|int|dictionary
     */
    @all:Settable val augment: Option<TMatAugment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * The gap between rows and columns.
     * 
     * This is a shorthand to set `row-gap` and `column-gap` to the same value.
     * 
     * Typst type: relative
     */
    val gap: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * The gap between rows.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val rowGap: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/mat/](https://typst.app/docs/reference/math/mat/)
     * 
     * The gap between columns.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val columnGap: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.mat")
    }
}


@SerialName("set-math.mat")
data class TSetMathMat(
    override val internals: SetRuleInternals? = null,
    val delim: ArrayOrSingle<Option<TSymbolLike>>? = null,
    val align: TAlignment? = null,
    val augment: Option<TMatAugment>? = null,
    val rowGap: TRelative? = null,
    val columnGap: TRelative? = null
) : TSetRule()
