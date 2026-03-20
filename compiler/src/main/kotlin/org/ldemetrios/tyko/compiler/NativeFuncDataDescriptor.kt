package org.ldemetrios.tyko.compiler

import kotlinx.serialization.Serializable

@Serializable
data class NativeFuncDataDescriptor(
    val name: String,
    val title: String = name,
    val docs: String = "",
    val keywords: List<String> = emptyList(),
    val contextual: Boolean = true,
    val params: List<NativeParamDescriptor> = emptyList(),
    val returns: NativeCastInfo = NativeCastInfo.any(),
)

@Serializable
data class NativeParamDescriptor(
    val name: String,
    val docs: String = "",
    val input: NativeCastInfo = NativeCastInfo.any(),
    val positional: Boolean = true,
    val named: Boolean = false,
    val variadic: Boolean = false,
    val required: Boolean = true,
    val settable: Boolean = false,
)

@ExposedCopyVisibility
@Serializable
data class NativeCastInfo private constructor(
    val kind: String,
    val name: String? = null,
    val items: List<NativeCastInfo> = emptyList(),
) {
    companion object {
        fun any() = NativeCastInfo(kind = "any")
        fun type(name: String) = NativeCastInfo(kind = "type", name = name)
        fun union(vararg items: NativeCastInfo) = NativeCastInfo(kind = "union", items = items.toList())
    }
}
