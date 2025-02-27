package org.ldemetrios.tyko.original

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.PackageError
import org.ldemetrios.tyko.compiler.PackageSpec
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.StdlibProvider
import org.ldemetrios.tyko.compiler.World
import org.ldemetrios.tyko.compiler.WorldBasedTypstCompiler
import org.ldemetrios.tyko.compiler.WorldTime
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.model.TDictionary
import org.ldemetrios.tyko.model.TEmptyDictionaryImpl
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.split
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.readText
import kotlin.io.path.toPath
import kotlin.io.path.walk

val TEST_SEPARATOR = Regex("--- [a-zA-Z0-9-]+ ---")

val localFiles = run {
    val root = TestWorld::class.java.classLoader
        .getResource("suite")!!
        .toURI()
        .toPath()

    val files = root.walk().filter { !it.isDirectory() }.associateWith { it.readText() }

    val skipped = files.filter { (key, value) -> value.startsWith("// SKIP") }

    val notSkipped = files
        .filter { (key, value) -> !value.startsWith("// SKIP") }
        .flatMap { (key, value) ->
            value.lines().split(keepSeparator = true) { it.matches(TEST_SEPARATOR) }.drop(1).map {
                val first = it.first().removePrefix("--- ").removeSuffix(" ---")
                val rest = it.drop(1).joinToString("\n")
                "${key}__part__$first" to rest
            }
        }
        .toMap()

    val localFiles = skipped.mapKeys { "/suite/" + it.key.toString().split("/suite/").last() } +
            notSkipped.mapKeys { "/suite/" + it.key.split("/suite/").last() }.mapValues {
                "#set page(width: 120pt)\n" +
                        "#set page(height:auto)\n" +
                        "#set page(margin: 10pt)\n" +
                        "#set text(size: 10pt)\n" + it.value
            }

    localFiles
}

fun testPackage(spec: PackageSpec, path: String): RResult<ByteArray, FileError> {
    if (spec.namespace != "test") return RResult.Err(FileError.Package(PackageError.NotFound(spec)))
    val fileName = "packages/${spec.name}-${spec.version}$path"
    val resource = TestWorld::class.java.classLoader.getResource(fileName) ?: return RResult.Err(
        FileError.Package(
            PackageError.NotFound(spec)
        )
    )
    return RResult.Ok(resource.readBytes())
}

class TestWorld(var currentMain: String) : World {
    var overriden: Pair<String, String>? = null

    override fun library(): StdlibProvider = object : StdlibProvider.Standard {
        override val inputs: TDictionary<TValue> get() = TEmptyDictionaryImpl
        override val features: List<Feature> get() = listOf(Feature.Html)
    }

    override fun mainFile(): FileDescriptor {
        return FileDescriptor(null, "/$currentMain")
    }

    override fun file(file: FileDescriptor): RResult<ByteArray, FileError> {
        return when (file.pack?.namespace) {
            null -> {
                if (overriden?.first == file.path) return RResult.Ok(overriden!!.second.toByteArray())
                val local = localFiles[file.path]
                when {
                    local != null -> RResult.Ok(
                        (
                                local).toByteArray()
                    )

                    else -> {
                        val bytes = TestWorld::class.java.classLoader.getResource(file.path.drop(1))?.readBytes()
                        if (bytes != null) {
                            RResult.Ok(bytes)
                        } else RResult.Err(FileError.NotFound(file.path))
                    }
                }
            }

            "test" -> testPackage(file.pack, file.path)
            else -> TODO()
        }
    }

    override fun now(): WorldTime? = WorldTime.Fixed(Instant.EPOCH)

    override val autoManageCentral: Boolean = false
}

public const val SHARED_LIBRARY_PATH = "/home/ldemetrios/Workspace/TypstNKotlinInterop/libtypst_shared.so"

val sharedLib = TypstSharedLibrary.instance(Path(SHARED_LIBRARY_PATH))

fun TestCompiler(world: TestWorld) =
    WorldBasedTypstCompiler(sharedLib, world)