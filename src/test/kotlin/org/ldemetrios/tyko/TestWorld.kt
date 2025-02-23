package org.ldemetrios.tyko

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.PackageError
import org.ldemetrios.tyko.compiler.PackageSpec
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.StdlibProvider
import org.ldemetrios.tyko.compiler.World
import org.ldemetrios.tyko.compiler.WorldTime
import org.ldemetrios.tyko.model.TDictionary
import org.ldemetrios.tyko.model.TEmptyDictionaryImpl
import org.ldemetrios.tyko.model.TValue

fun testPackage(spec: PackageSpec, path: String): RResult<ByteArray, FileError> {
    if (spec.namespace != "test") return RResult.Err(FileError.Package(PackageError.NotFound(spec)))
    val fileName = "packages/${spec.name}-${spec.version}$path"
    val resource = TestWorld::class.java.classLoader.getResource(fileName) ?: return RResult.Err(FileError.Package(PackageError.NotFound(spec)))
    return RResult.Ok(resource.readBytes())
}

class TestWorld(val source: String = "") : World {
    override fun library(): StdlibProvider = object : StdlibProvider.Standard {
        override val inputs: TDictionary<TValue> get() = TEmptyDictionaryImpl
        override val features: List<Feature> get() = listOf(Feature.Html)

    }

    override fun mainFile(): FileDescriptor {
        return FileDescriptor(null, "/main.typ")
    }

    override fun file(file: FileDescriptor): RResult<ByteArray, FileError> {
        println(file)
        return when {
            file.pack == null && (file.path == "/main.typ" || file.path == "main.typ") -> RResult.Ok(source.toByteArray())
            file.pack == null -> RResult.Err(FileError.NotFound(file.path))
            else -> testPackage(file.pack, file.path)
        }
    }

    override fun now(): WorldTime? = WorldTime.System
}

//fun TestCompiler(source: String = "") = WorldBasedTypstCompiler(TypstSharedLibrary.INSTANCE, TestWorld(source))