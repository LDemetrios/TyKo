package org.ldemetrios.tyko.compiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FileError {
    @Serializable
    @SerialName("NotFound")
    data class NotFound(val path: String) : FileError()

    @Serializable
    @SerialName("AccessDenied")
    data object AccessDenied : FileError()

    @Serializable
    @SerialName("IsDirectory")
    data object IsDirectory : FileError()

    @Serializable
    @SerialName("NotSource")
    data object NotSource : FileError()

    @Serializable
    @SerialName("InvalidUtf8")
    data object InvalidUtf8 : FileError()

    @Serializable
    @SerialName("Package")
    data class Package(val error: PackageError) : FileError()

    @Serializable
    @SerialName("Other")
    data class Other(val message: String?) : FileError()
}

@Serializable
sealed class PackageError {
    @Serializable
    @SerialName("NotFound")
    data class NotFound(@SerialName("package") val spec: PackageSpec) : PackageError()

    @Serializable
    @SerialName("VersionNotFound")
    data class VersionNotFound(@SerialName("package") val spec: PackageSpec, val version: PackageVersion) :
        PackageError()

    @Serializable
    @SerialName("NetworkFailed")
    data class NetworkFailed(val message: String?) : PackageError()

    @Serializable
    @SerialName("MalformedArchive")
    data class MalformedArchive(val message: String?) : PackageError()

    @Serializable
    @SerialName("Other")
    data class Other(val message: String?) : PackageError()
}

typealias FileResult<T> = RResult<T, FileError>