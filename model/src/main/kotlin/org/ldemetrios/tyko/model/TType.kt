package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


//!https://typst.app/docs/reference/foundations/type/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/type/](https://typst.app/docs/reference/foundations/type/)
 * 
 * Describes a kind of value.
 * 
 * To style your document, you need to work with values of different kinds: Lengths specifying the size of your elements, colors for your text and shapes, and more. Typst categorizes these into clearly defined *types* and tells you where it expects which type of value.
 * 
 * Apart from basic types for numeric values and [typical](https://typst.app/docs/reference/foundations/int/)[types](https://typst.app/docs/reference/foundations/float/)[known](https://typst.app/docs/reference/foundations/str/)[from](https://typst.app/docs/reference/foundations/array/)[programming](https://typst.app/docs/reference/foundations/dictionary/) languages, Typst provides a special type for [*content.*](https://typst.app/docs/reference/foundations/content/) A value of this type can hold anything that you can enter into your document: Text, elements like headings and shapes, and style information.
 * 
 * **_Example_**
 * 
 * ```typ
 * #let x = 10
 * #if type(x) == int [
 *   #x is an integer!
 * ] else [
 *   #x is another value...
 * ]
 * 
 * An image is of type
 * #type(image("glacier.jpg")).
 * ```
 * <img src="https://typst.app/assets/docs/dTjHaEMO5150e0-XVg1OzwAAAAAAAAAA.png" alt="Preview" />
 * 
 * The type of `10` is `int`. Now, what is the type of `int` or even `type`?
 * 
 * ```typ
 * #type(int) \
 * #type(type)
 * ```
 * <img src="https://typst.app/assets/docs/HqIgZy_wqBbnboRlZ-Iv4AAAAAAAAAAA.png" alt="Preview" />
 * 
 * Unlike other types like `int`, [none](https://typst.app/docs/reference/foundations/none/) and [auto](https://typst.app/docs/reference/foundations/auto/) do not have a name representing them. To test if a value is one of these, compare your value to them directly, e.g:
 * 
 * ```typ
 * #let val = none
 * #if val == none [
 *   Yep, it's none.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/n-ZLDWMH52qhM-X09GjUlAAAAAAAAAAA.png" alt="Preview" />
 * 
 * Note that `type` will return [`content`](https://typst.app/docs/reference/foundations/content/) for all document elements. To programmatically determine which kind of content you are dealing with, see [`content.func`](https://typst.app/docs/reference/foundations/content/#definitions-func).
 */
@SerialName("type")
data class TType(
    @all:Positional val name: TStr
) : TValue, Smart<TType> {
    constructor(name: String) : this(name.t)

    override fun type(): TType = TYPE

    companion object {
        val STATE: TType = TType("state")
        val NONE = TType("none")
        val AUTO = TType("auto")
        val BOOL = TType("bool")
        val COUNTER: TType = TType("counter")
        val INT = TType("int")
        val FLOAT = TType("float")
        val DECIMAL = TType("decimal")
        val FRACTION = TType("fraction")
        val LENGTH = TType("length")
        val RELATIVE = TType("relative")
        val RATIO = TType("ratio")
        val ANGLE = TType("angle")
        val DIRECTION = TType("direction")
        val ALIGNMENT = TType("alignment")
        val STROKE = TType("stroke")
        val COLOR = TType("color")
        val GRADIENT = TType("gradient")
        val TILING = TType("tiling")
        val DURATION = TType("duration")
        val DATETIME = TType("datetime")
        val BYTES = TType("bytes")
        val STR = TType("str")
        val SYMBOL = TType("symbol")
        val REGEX = TType("regex")
        val LABEL = TType("label")
        val CONTENT = TType("content")
        val ARRAY = TType("array")
        val DICT = TType("dict")
        val ARGS = TType("args")
        val FUNC = TType("func")
        val TYPE = TType("type")
        val MODULE = TType("module")
        val VERSION = TType("version")
        val LOCATION = TType("location")
        val SELECTOR = TType("selector")
        val STYLE = TType("style")
        val PATH = TType("path")
    }

    override fun repr(sb: StringBuilder) {
        sb.append(name.value)
    }
}
