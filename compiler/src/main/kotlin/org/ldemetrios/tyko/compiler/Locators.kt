package org.ldemetrios.tyko.compiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackageSpec(val namespace: String, val name: String, val version: PackageVersion) {
    override fun toString() = "@$namespace/$name:$version"
}

@Serializable
data class PackageVersion(val major: Int, val minor: Int, val patch: Int) {
    override fun toString() = "$major.$minor.$patch"
}

@Serializable
data class FileDescriptor(
    @SerialName("pack")
    val packageSpec: PackageSpec?,
    @SerialName("path")
    val virtualPath: String
) {
    override fun toString() = (packageSpec?.toString() ?: "") + virtualPath
}

@Serializable
data class Span(
    val native: ULong,
    val file: FileDescriptor?,
    @SerialName("start_ind") val startInd: Long,
    @SerialName("end_ind") val endInd: Long,
    @SerialName("start_line") val startLine: Long,
    @SerialName("start_col") val startCol: Long,
    @SerialName("end_line") val endLine: Long,
    @SerialName("end_col") val endCol: Long,
)