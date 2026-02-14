package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/version/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/version/](https://typst.app/docs/reference/foundations/version/)
 * 
 * A version with an arbitrary number of components.
 * 
 * The first three components have names that can be used as fields: `major`, `minor`, `patch`. All following components do not have names.
 * 
 * The list of components is semantically extended by an infinite list of zeros. This means that, for example, `0.8` is the same as `0.8.0`. As a special case, the empty version (that has no components at all) is the same as `0`, `0.0`, `0.0.0`, and so on.
 * 
 * The current version of the Typst compiler is available as `sys.version`.
 * 
 * You can convert a version to an array of explicitly given components using the [`array`](https://typst.app/docs/reference/foundations/array/) constructor.
 */
@SerialName("version")
data class TVersion(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/version/](https://typst.app/docs/reference/foundations/version/)
     * 
     * The components of the version (array arguments are flattened)
     * 
     * Required, positional, variadic; Typst type: int|array
     */
    @all:Variadic @all:SerialName("value") val components: TArray<TInt>
) : TValue, Smart<TVersion> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.VERSION
    }
}
