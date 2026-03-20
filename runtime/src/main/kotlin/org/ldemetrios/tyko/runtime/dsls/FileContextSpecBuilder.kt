package org.ldemetrios.tyko.runtime

import org.intellij.lang.annotations.Language
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.FileResult
import org.ldemetrios.tyko.compiler.PackageError
import org.ldemetrios.tyko.compiler.PackageSpec
import org.ldemetrios.tyko.compiler.PackageVersion
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.SyntaxMode
import org.ldemetrios.tyko.driver.api.Session
import org.ldemetrios.tyko.driver.api.registerJavaObject
import org.ldemetrios.tyko.driver.api.withSession
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.repr
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.readBytes

private typealias FD = FileDescriptor

private fun toFileContent(value: TValue, session: Session?, mode: SyntaxMode): RResult.Ok<ByteArray> {
    val repr = withSession(session) { ses -> value.repr { ses to registerJavaObject(ses, it) } }
    val content = if (mode == SyntaxMode.Code) repr else "#" + repr
    return RResult.Ok(content.toByteArray())
}

open class FileContextPackageSpecBuilder internal constructor(
    protected var matchers: MutableList<FileMatcher>,
    private val packageSpec: PackageSpec? = null,
) {
    fun singleFile(fd: String, content: FileResult<ByteArray>) {
        matchers.add { it, _ -> if (it.packageSpec == packageSpec && it.virtualPath == fd) content else null }
    }

    fun singleFile(fd: String, content: FileError) {
        singleFile(fd, RResult.Err(content))
    }

    fun singleFile(fd: String, content: ByteArray) {
        singleFile(fd, RResult.Ok(content))
    }

    fun singleFile(fd: String, @Language("typ") content: String) {
        singleFile(fd, content.toByteArray())
    }

    fun singleFile(fd: String, mirror: File) = singleFile(fd, mirror.toPath().toUri().toURL())
    fun singleFile(fd: String, mirror: Path) = singleFile(fd, mirror.toUri().toURL())
    fun singleFile(fd: String, mirror: URI) = singleFile(fd, mirror.toURL())
    fun singleFile(fd: String, mirror: URL) {
        matchers.add { it, _ -> if (it.packageSpec == packageSpec && it.virtualPath == fd) RResult.Ok(mirror.readBytes()) else null }
    }

    fun singleFile(fd: String, content: TValue, mode: SyntaxMode = SyntaxMode.Markup) {
        matchers.add { it, session ->
            if (it.packageSpec == packageSpec && it.virtualPath == fd) toFileContent(content, session, mode) else null
        }
    }

    fun hierarchy(fd: String, file: File) = hierarchy(fd, file.toPath())
    fun hierarchy(fd: String, file: Path) {
        matchers.add { it, _ ->
            if (packageSpec != it.packageSpec) return@add null
            val fdComponents = fd.split("/")
            val itComponents = it.virtualPath.split("/")
            if (itComponents.take(fdComponents.size) != fdComponents) return@add null
            val rest = itComponents.drop(fdComponents.size)
            val resultPath = file.resolve(rest.joinToString("/"))
            if (!resultPath.absolutePathString().startsWith(file.absolutePathString())) return@add null
            readFileResult(resultPath)
        }
    }
}

class FileContextSpecBuilder : FileContextPackageSpecBuilder(mutableListOf()) {
    private var fallback: ((FD, Session?) -> FileResult<ByteArray>)? = null
    var failfast: Boolean = true

    fun addRaw(fileMatcher: FileMatcher) {
        matchers.add(fileMatcher)
    }

    fun aPackage(packageSpec: PackageSpec?, body: FileContextPackageSpecBuilder.() -> Unit) {
        FileContextPackageSpecBuilder(matchers, packageSpec).body()
    }

    fun singleFile(fd: FD, content: FileResult<ByteArray>) =
        aPackage(fd.packageSpec) { singleFile(fd.virtualPath, content) }

    fun singleFile(fd: FD, content: FileError) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, content) }
    fun singleFile(fd: FD, content: ByteArray) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, content) }
    fun singleFile(fd: FD, @Language("typ") content: String) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, content) }
    fun singleFile(fd: FD, mirror: File) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, mirror) }
    fun singleFile(fd: FD, mirror: Path) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, mirror) }
    fun singleFile(fd: FD, mirror: URI) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, mirror) }
    fun singleFile(fd: FD, mirror: URL) = aPackage(fd.packageSpec) { singleFile(fd.virtualPath, mirror) }
    fun hierarchy(fd: FD, file: File) = aPackage(fd.packageSpec) { hierarchy(fd.virtualPath, file) }
    fun hierarchy(fd: FD, file: Path) = aPackage(fd.packageSpec) { hierarchy(fd.virtualPath, file) }
    fun singleFile(fd: FD, content: TValue, mode: SyntaxMode = SyntaxMode.Markup) =
        aPackage(fd.packageSpec) { singleFile(fd.virtualPath, content, mode) }

    fun inPackage(packageSpec: PackageSpec?, callback: (String, Session?) -> FileResult<ByteArray>) {
        addRaw { it, session -> if (it.packageSpec == packageSpec) callback(it.virtualPath, session) else null }
    }

    fun inPackageCatching(packageSpec: PackageSpec?, callback: (String, Session?) -> ByteArray) {
        inPackage(packageSpec) { it, session -> runOrFileError { callback(it, session) } }
    }

    fun inNamespace(namespace: String?, callback: (FD, Session?) -> FileResult<ByteArray>) {
        addRaw { it, session -> if (it.packageSpec?.namespace == namespace) callback(it, session) else null }
    }

    fun inNamespaceCatching(namespace: String?, callback: (FD, Session?) -> ByteArray) {
        inNamespace(namespace) { it, session -> runOrFileError { callback(it, session) } }
    }

    fun fallback(callback: (FD, Session?) -> FileResult<ByteArray>) {
        fallback = callback
    }

    fun fallbackCatching(callback: (FD, Session?) -> ByteArray) {
        fallback { it, session -> runOrFileError { callback(it, session) } }
    }

    fun build(): FileContextSpecification = FileContextSpecification(
        failfast,
        matchers + listOfNotNull(fallback?.let(::FileMatcher))
    )
}

fun readFileResult(path: Path): FileResult<ByteArray> {
    return when {
        !Files.isReadable(path) -> RResult.Err(FileError.AccessDenied)
        !path.exists() -> RResult.Err(FileError.NotFound(path.absolutePathString()))
        path.isDirectory() -> RResult.Err(FileError.IsDirectory)
        else -> RResult.Ok(path.readBytes())
    }
}

sealed class FileErrorException : RuntimeException() {
    abstract fun toFileError(): FileError

    class FileNotFoundException(val path: String) : FileErrorException() {
        override fun toFileError(): FileError = FileError.NotFound(path)
    }

    class AccessDeniedException() : FileErrorException() {
        override fun toFileError(): FileError = FileError.AccessDenied
    }

    class IsDirectoryException() : FileErrorException() {
        override fun toFileError(): FileError = FileError.IsDirectory
    }

    class NotSourceException() : FileErrorException() {
        override fun toFileError(): FileError = FileError.NotSource
    }

    class InvalidUtf8Exception() : FileErrorException() {
        override fun toFileError(): FileError = FileError.InvalidUtf8
    }

    sealed class PackageErrorException() : FileErrorException() {
        abstract fun toPackageError(): PackageError
        override fun toFileError(): FileError = FileError.Package(toPackageError())
    }

    class PackageNotFoundException(val spec: PackageSpec) : PackageErrorException() {
        override fun toPackageError(): PackageError = PackageError.NotFound(spec)
    }

    class PackageVersionNotFoundException(val spec: PackageSpec, val version: PackageVersion = spec.version) :
        PackageErrorException() {
        override fun toPackageError(): PackageError = PackageError.VersionNotFound(spec, version)
    }

    class NetworkFailedException(override val message: String?) : PackageErrorException() {
        override fun toPackageError(): PackageError = PackageError.NetworkFailed(message)
    }

    class MalformedPackageArchiveException(override val message: String?) : PackageErrorException() {
        override fun toPackageError(): PackageError = PackageError.MalformedArchive(message)
    }

    class OtherPackageErrorException(override val message: String?) : PackageErrorException() {
        override fun toPackageError(): PackageError = PackageError.Other(message)
    }

    class OtherFileErrorException(override val message: String?) : FileErrorException() {
        override fun toFileError(): FileError = FileError.Other(message)
    }
}

fun FileErrorException(message: String?) = FileErrorException.OtherFileErrorException(message)
fun PackageErrorException(message: String?) = FileErrorException.OtherPackageErrorException(message)

fun <T> runOrFileError(body: () -> T) = try {
    RResult.Ok(body())
} catch (e: FileErrorException) {
    RResult.Err(e.toFileError())
}
