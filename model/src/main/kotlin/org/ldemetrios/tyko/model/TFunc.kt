package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Numbering
import org.ldemetrios.tyko.model.TTransform

//!https://typst.app/docs/reference/foundations/function/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/function/](https://typst.app/docs/reference/foundations/function/)
 * 
 * A mapping from argument values to a return value.
 * 
 * You can call a function by writing a comma-separated list of function *arguments* enclosed in parentheses directly after the function name. Additionally, you can pass any number of trailing content block arguments to a function *after* the normal argument list. If the normal argument list would become empty, it can be omitted. Typst supports positional and named arguments. The former are identified by position and type, while the latter are written as `name: value`.
 * 
 * Within math mode, function calls have special behaviour. See the [math documentation](https://typst.app/docs/reference/math/) for more details.
 * 
 * **_Example_**
 * 
 * ```typ
 * // Call a function.
 * #list([A], [B])
 * 
 * // Named arguments and trailing
 * // content blocks.
 * #enum(start: 2)[A][B]
 * 
 * // Version without parentheses.
 * #list[A][B]
 * ```
 * <img src="https://typst.app/assets/docs/h8ulslRsTYE05Pu4qy5C6AAAAAAAAAAA.png" alt="Preview" />
 * 
 * Functions are a fundamental building block of Typst. Typst provides functions for a variety of typesetting tasks. Moreover, the markup you write is backed by functions and all styling happens through functions. This reference lists all available functions and how you can use them. Please also refer to the documentation about [set](https://typst.app/docs/reference/styling/#set-rules) and [show](https://typst.app/docs/reference/styling/#show-rules) rules to learn about additional ways you can work with functions in Typst.
 * 
 * **_Element functions_**
 * 
 * Some functions are associated with *elements* like [headings](https://typst.app/docs/reference/model/heading/) or [tables](https://typst.app/docs/reference/model/table/). When called, these create an element of their respective kind. In contrast to normal functions, they can further be used in [set rules](https://typst.app/docs/reference/styling/#set-rules), [show rules](https://typst.app/docs/reference/styling/#show-rules), and [selectors](https://typst.app/docs/reference/foundations/selector/).
 * 
 * **_Function scopes_**
 * 
 * Functions can hold related definitions in their own scope, similar to a [module](https://typst.app/docs/reference/scripting/#modules). Examples of this are [`assert.eq`](https://typst.app/docs/reference/foundations/assert/#definitions-eq) or [`list.item`](https://typst.app/docs/reference/model/list/#definitions-item). However, this feature is currently only available for built-in functions.
 * 
 * **_Defining functions_**
 * 
 * You can define your own function with a [let binding](https://typst.app/docs/reference/scripting/#bindings) that has a parameter list after the binding's name. The parameter list can contain mandatory positional parameters, named parameters with default values and [argument sinks](https://typst.app/docs/reference/foundations/arguments/).
 * 
 * The right-hand side of a function binding is the function body, which can be a block or any other expression. It defines the function's return value and can depend on the parameters. If the function body is a [code block](https://typst.app/docs/reference/scripting/#blocks), the return value is the result of joining the values of each expression in the block.
 * 
 * Within a function body, the `return` keyword can be used to exit early and optionally specify a return value. If no explicit return value is given, the body evaluates to the result of joining all expressions preceding the `return`.
 * 
 * Functions that don't return any meaningful value return [`none`](https://typst.app/docs/reference/foundations/none/) instead. The return type of such functions is not explicitly specified in the documentation. (An example of this is [`array.push`](https://typst.app/docs/reference/foundations/array/#definitions-push)).
 * 
 * ```typ
 * #let alert(body, fill: red) = {
 *   set text(white)
 *   set align(center)
 *   rect(
 *     fill: fill,
 *     inset: 8pt,
 *     radius: 4pt,
 *     [*Warning:\ #body*],
 *   )
 * }
 * 
 * #alert[
 *   Danger is imminent!
 * ]
 * 
 * #alert(fill: blue)[
 *   KEEP OFF TRACKS
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/56wK4TQtzRt7_B3OQOxb7QAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Importing functions_**
 * 
 * Functions can be imported from one file ([`module`](https://typst.app/docs/reference/scripting/#modules)) into another using `import`. For example, assume that we have defined the `alert` function from the previous example in a file called `foo.typ`. We can import it into another file by writing `import "foo.typ": alert`.
 * 
 * **_Unnamed functions_**
 * 
 * You can also create an unnamed function without creating a binding by specifying a parameter list followed by `=>` and the function body. If your function has just one parameter, the parentheses around the parameter list are optional. Unnamed functions are mainly useful for show rules, but also for settable properties that take functions like the page function's [`footer`](https://typst.app/docs/reference/layout/page/#parameters-footer) property.
 * 
 * ```typ
 * #show "once?": it => [#it #it]
 * once?
 * ```
 * <img src="https://typst.app/assets/docs/yXee-w_McX_Uho7Ghovc-QAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Note on function purity_**
 * 
 * In Typst, all functions are *pure.* This means that for the same arguments, they always return the same result. They cannot "remember" things to produce another value when they are called a second time.
 * 
 * The only exception are built-in methods like [`array.push(value)`](https://typst.app/docs/reference/foundations/array/#definitions-push). These can modify the values they are called on.
 */
sealed interface TFunc : TValue, TTransform, Progression<Nothing>, TFigureKind, Numbering, Computable<Nothing>,
    Smart<TFunc>,
    Option<TFunc> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.FUNC
    }
}


/**
 * A function that can be used as selector.
 *
 * @param S the type of content that can be selected by this func
 */
sealed interface TSelectableFunc<S : TContent> : TFunc, TSelectable<S> {
    override fun type(): TType = TFunc.TYPE

    fun where(args: TArgs<*>) = TWhereSelector(this, args)
    fun where(vararg args: Argument) = TWhereSelector(this, TArgs.of(*args))
}


/**
 * A native Typst function.
 */
@SerialName("native-function")
data class TNativeFunc(
    val name: String,
    val ptr: Long? = null,
) : TFunc {
    override fun equals(other: Any?): Boolean =
        this === other || other is TElement &&
                if (ptr == null || other.ptr == null) {
                    name == other.name
                } else {
                    ptr == other.ptr
                }

    override fun repr(sb: StringBuilder) {
        sb.append(name)
    }
}

/**
 * A Typst element.
 */
@SerialName("element")
open class TElement(
    open val name: String,
    open val ptr: Long? = null,
) : TFunc {
    override fun hashCode(): Int = name.hashCode()
    override fun equals(other: Any?): Boolean =
        this === other || other is TElement &&
                if (ptr == null || other.ptr == null) {
                    name == other.name
                } else {
                    ptr == other.ptr
                }

    override fun repr(sb: StringBuilder) {
        sb.append(
            when (name) {
                "sequence" -> "[a\\ ].func()"
                "math.align-point" -> "\$&\$.body.func()"
                else -> name
            }
        )
    }
}

/**
 * An element that can be located
 *
 * @param S the type of content that can be selected by this func
 */
@SerialName("element")
class TLocatableElement<S : TContent>(
    name: String,
    ptr: Long? = null,
) : TElement(name, ptr), TSelectableFunc<S>


/**
 * A Typst closure.
 */
@SerialName("closure")
data class TClosure(
    val node: TClosureSyntaxNode,
    val defaults: TArray<TValue>,
    val captured: TDict<TValue>,
    val numPosParams: Long,
) : TFunc {
    override fun repr(sb: StringBuilder) {
        sb.append("{")
        for ((k, v) in captured) {
            sb.append("let $k = ")
            v.repr(sb)
            sb.append(";")
        }
        if (node is RealClosure) {
            val name = node.name
            if (name != null) {
                sb.append("{ let ")
                sb.append(node.value)
                sb.append("; ")
                sb.append(name)
                sb.append("}")
            } else {
                sb.append(node.value)
            }
        } else {
            sb.append("(context (")
            sb.append(node.value)
            sb.append(")).func")
        }
        sb.append("}")
    }
}


sealed class TClosureSyntaxNode {
    abstract val value: String
}

@SerialName("real-closure")
data class RealClosure(
    override val value: String,
    val name: String? = null,
) : TClosureSyntaxNode()


@SerialName("synt-closure")
data class SyntClosure(override val value: String) : TClosureSyntaxNode()

@SerialName("func.with")
data class TFuncWith(val func: TFunc, val args: TArgs<*>) : TFunc {
    override fun repr(sb: StringBuilder) {
        sb.append("(")
        func.repr(sb)
        sb.append(".with(..")
        args.repr(sb)
        sb.append("))")
    }
}
