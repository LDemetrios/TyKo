package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/module/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/module/](https://typst.app/docs/reference/foundations/module/)
 * 
 * A collection of variables and functions that are commonly related to a single theme.
 * 
 * A module can
 * 
 * - be built-in
 * - stem from a [file import](https://typst.app/docs/reference/scripting/#modules)
 * - stem from a [package import](https://typst.app/docs/reference/scripting/#packages) (and thus indirectly its entrypoint file)
 * - result from a call to the [plugin](https://typst.app/docs/reference/foundations/plugin/) function
 * 
 * You can access definitions from the module using [field access notation](https://typst.app/docs/reference/scripting/#fields) and interact with it using the [import and include syntaxes](https://typst.app/docs/reference/scripting/#modules).
 * 
 * ```typ
 * #import "utils.typ"
 * #utils.add(2, 5)
 * 
 * #import utils: sub
 * #sub(1, 4)
 * ```
 * <img src="https://typst.app/assets/docs/itOPaialNOb62A81RHFv_wAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can check whether a definition is present in a module using the `in` operator, with a string on the left-hand side. This can be useful to [conditionally access](https://typst.app/docs/reference/foundations/std/#conditional-access) definitions in a module.
 * 
 * ```typ
 * #("table" in std) \
 * #("nope" in std)
 * ```
 * <img src="https://typst.app/assets/docs/GiSc_VRTChaL9AHuEwSoUgAAAAAAAAAA.png" alt="Preview" />
 * 
 * Alternatively, it is possible to convert a module to a dictionary, and therefore access its contents dynamically, using the [dictionary constructor](https://typst.app/docs/reference/foundations/dictionary/#constructor).
 */
@SerialName("module")
data class TModule(
    val name: TStr?,
    val scope: TDict<TValue>,
    val content: TContent,
) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.MODULE
    }

    override fun repr(sb: StringBuilder) {
        sb.append("std.")
        sb.append(name!!.value)
    }
}
