package org.ldemetrios.tyko.compiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Identifier of a package.
 */
@Serializable
data class PackageSpec(val namespace: String, val name: String, val version: PackageVersion) {
    override fun toString() = "@$namespace/$name:$version"
}

/**
 * Semver-like version of a package.
 */
@Serializable
data class PackageVersion(val major: Int, val minor: Int, val patch: Int) {
    override fun toString() = "$major.$minor.$patch"
}

/**
 * Identifier of a virtual file in a project.
 */
@Serializable
data class FileDescriptor(
    /**
     * What package the file belongs to. Null, if it's project file.
     */
    @SerialName("pack")
    val packageSpec: PackageSpec?,
    /**
     * Path of file inside a package or a project.
     */
    @SerialName("path")
    val virtualPath: String
) {
    override fun toString() = (packageSpec?.toString() ?: "") + virtualPath
}

/**
 * A range in the source file.
 */
@Serializable
data class Span(
    /**
     * Native representation of the span.
     */
    val native: ULong,
    /**
     * Source file, which the range is in.
     */
    val file: FileDescriptor?,
    /**
     * Index of first byte in the range. NB! Byte in UTF-8, not char.
     */
    @SerialName("start_ind") val startInd: Long,
    /**
     * Index of first byte after the range. NB! Byte in UTF-8, not char.
     */
    @SerialName("end_ind") val endInd: Long,
    /**
     * Line index of the [startInd].
     */
    @SerialName("start_line") val startLine: Long,
    /**
     * Columns index of the [startInd].
     */
    @SerialName("start_col") val startCol: Long,
    /**
     * Line index of the [endInd].
     */
    @SerialName("end_line") val endLine: Long,
    /**
     * Column index of the [endInd].
     */
    @SerialName("end_col") val endCol: Long,
)