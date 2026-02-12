package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

@SerialName("package-version")
data class PackageVersion(val major: Int, val minor: Int, val patch: Int) {
    override fun toString(): String = "$major.$minor.$patch"
}

sealed class VirtualRoot {
    @SerialName("project")
    data object Project : VirtualRoot() {
        override fun toString(): String = ""
    }

    @SerialName("package")
    data class Package(val namespace: String, val name: String, val version: PackageVersion) : VirtualRoot() {
        override fun toString(): String = "@$namespace/$name:$version"
    }
}

@SerialName("path")
data class TPath(
    val root: VirtualRoot,
    val path: String
) : TValue, Smart<TPath>, DataSource, Option<TPath>, ArrayOrSingle<TPath> {
    override fun type(): TType = TYPE

    override fun repr(sb: StringBuilder) {
        TStr("$root$path").repr(sb)
    }

    companion object {
        val TYPE = TType.PATH
    }
}
