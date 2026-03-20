package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.NativeFuncDataDescriptor
import org.ldemetrios.tyko.compiler.Now
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrar
import org.ldemetrios.tyko.driver.api.ResourceReference
import org.ldemetrios.tyko.model.TArgs
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TStyle
import org.ldemetrios.tyko.model.TStyles
import org.ldemetrios.tyko.model.TValue

data class WorldSpecification(
    val librarySpecification: LibrarySpecification?,
    val fontsSpecification: FontsSpecification?,
    val fsContextSpecification: FileContextSpecification?,
    val worldTimeSpecification: Now?,
    val jvmObjectsSession: CreateSessionMode?,
)

data class LibrarySpecification(
    val prototype: LibraryPrototypeSpecification,
    val additionalStyles: List<TStyle>,
    val appendStyles: Boolean,
    val inputs: TDict<TValue>?,
    val values: Map<String, TValue>,
    val funcs: Map<String, FuncSpecification>,
)

sealed class LibraryPrototypeSpecification {
    data object UseDefault : LibraryPrototypeSpecification()
    data class Fresh(val features: Set<Feature>) : LibraryPrototypeSpecification()
    data class UseReference(val ref: LibraryReference) : LibraryPrototypeSpecification()
}

sealed class FuncSpecification {
    data class BorrowedFunction(val ref: NativeFuncDataReference) : FuncSpecification()
    data class NewFunction(
        val descriptor: NativeFuncDataDescriptor,
        val executor: (TArgs<TValue>, TStyles) -> RResult<TValue, String>
    ) : FuncSpecification()
}

sealed class FontsSpecification {
    data class BorrowedFontCollection(
        val ref: FontCollectionReference
    ) : FontsSpecification()

    data class NewFontCollection(
        val includeSystem: Boolean,
        val includeEmbedded: Boolean,
        val fontPaths: List<String>,
    ) : FontsSpecification()
}

data class FileContextSpecification(
    val failfast: Boolean,
    val matchers: List<FileMatcher>
)

fun interface FileMatcher {
    fun tryResolve(
        fileDescriptor: FileDescriptor,
        session: ResourceReference<JavaObjectsRegistrar>?
    ): RResult<ByteArray, FileError>?
}

