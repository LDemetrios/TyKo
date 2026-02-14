package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Attribution
import org.ldemetrios.tyko.model.TAttachment
import org.ldemetrios.tyko.model.TStackComponent
import org.ldemetrios.tyko.model.TTransform

//!https://typst.app/docs/reference/foundations/content/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/content/](https://typst.app/docs/reference/foundations/content/)
 * 
 * A piece of document content.
 * 
 * This type is at the heart of Typst. All markup you write and most [functions](https://typst.app/docs/reference/foundations/function/) you call produce content values. You can create a content value by enclosing markup in square brackets. This is also how you pass content to functions.
 * 
 * **_Example_**
 * 
 * ```typ
 * Type of *Hello!* is
 * #type([*Hello!*])
 * ```
 * <img src="https://typst.app/assets/docs/X4qekl24YgH3SaXf1J0tagAAAAAAAAAA.png" alt="Preview" />
 * 
 * Content can be added with the `+` operator, [joined together](https://typst.app/docs/reference/scripting/#blocks) and multiplied with integers. Wherever content is expected, you can also pass a [string](https://typst.app/docs/reference/foundations/str/) or `none`.
 * 
 * **_Representation_**
 * 
 * Content consists of elements with fields. When constructing an element with its *element function,* you provide these fields as arguments and when you have a content value, you can access its fields with [field access syntax](https://typst.app/docs/reference/scripting/#field-access).
 * 
 * Some fields are required: These must be provided when constructing an element and as a consequence, they are always available through field access on content of that type. Required fields are marked as such in the documentation.
 * 
 * Most fields are optional: Like required fields, they can be passed to the element function to configure them for a single element. However, these can also be configured with [set rules](https://typst.app/docs/reference/styling/#set-rules) to apply them to all elements within a scope. Optional fields are only available with field access syntax when they were explicitly passed to the element function, not when they result from a set rule.
 * 
 * Each element has a default appearance. However, you can also completely customize its appearance with a [show rule](https://typst.app/docs/reference/styling/#show-rules). The show rule is passed the element. It can access the element's field and produce arbitrary content from it.
 * 
 * In the web app, you can hover over a content variable to see exactly which elements the content is composed of and what fields they have. Alternatively, you can inspect the output of the [`repr`](https://typst.app/docs/reference/foundations/repr/) function.
 */
sealed class TContent() : SequenceElement(), TValue, TTransform, ArrayOrSingle<TContent>, Option<TContent>,
    Smart<TContent>, Attribution, Computable<TContent>, TStackComponent, TAttachment {
    override fun type(): TType = TYPE
    abstract fun elem(): TElement
    abstract val label: TLabel?
    companion object {
        val TYPE = TType.CONTENT
    }

    internal open fun reprImpl(sb: StringBuilder) {
        elem().repr(sb)
        sb.append("(")
        writeFieldsInto(sb)
        sb.append(")")
    }

    override fun repr(sb: StringBuilder)  {
        if (label == null) {
            reprImpl(sb)
        } else {
            sb.append("[#(")
            reprImpl(sb)
            sb.append(")#")
            label!!.repr(sb)
            sb.append("]")
        }
    }
}

/**
 * Lists supported fill rules.
 */
@SerialName("str")
enum class TFillRule(val value: String) : IntoStr {
    @SerialName("non-zero")
    NON_ZERO("non-zero"),
    @SerialName("even-odd")
    EVEN_ODD("even-odd");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue) = when (value) {
            NON_ZERO.value.t -> NON_ZERO
            EVEN_ODD.value.t -> EVEN_ODD
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}


/**
 * Base class for content elements that can be selected/queried by selectors.
 */
sealed class TSelectableContent<Self: TSelectableContent<Self>>() : TContent() {
    abstract override fun elem(): TLocatableElement<Self>
}
