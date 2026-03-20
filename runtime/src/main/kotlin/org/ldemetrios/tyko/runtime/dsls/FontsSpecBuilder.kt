package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.runtime.FontCollectionReference
import org.ldemetrios.tyko.runtime.FontsSpecification
import java.io.File
import java.nio.file.Path
import kotlin.collections.plus
import kotlin.collections.toMutableList
import kotlin.io.path.absolutePathString

class FontsSpecBuilder {
    private var prototype: FontCollectionReference? = null
    private var includeSystemV: Boolean? = null
    private var includeEmbeddedV: Boolean? = null
    var fontPaths = mutableListOf<String>()
    fun prototype(prototype: FontCollectionReference) {
        this.prototype = prototype
    }

    fun prototype(prototype: FontsSpecification) {
        when (prototype) {
            is FontsSpecification.NewFontCollection -> {
                includeSystemV = includeSystemV ?: prototype.includeSystem
                includeEmbeddedV = includeEmbeddedV ?: prototype.includeEmbedded
                fontPaths = (prototype.fontPaths + fontPaths).toMutableList()
            }

            is FontsSpecification.BorrowedFontCollection -> {
                this.prototype = prototype.ref
            }
        }
    }

    var includeSystem: Boolean
        get() = includeSystemV ?: throw IllegalStateException("`includeSystem` is not set yet")
        set(value) {
            includeSystemV = value
        }


    var includeEmbedded: Boolean
        get() = includeEmbeddedV ?: throw IllegalStateException("`includeEmbedded` is not set yet")
        set(value) {
            includeEmbeddedV = value
        }

    fun fontPath(path: String) {
        fontPaths.add(path)
    }

    fun fontPath(path: Path) {
        fontPaths.add(path.absolutePathString())
    }

    fun fontPath(path: File) {
        fontPaths.add(path.absolutePath)
    }

    @JvmName("fontPathsString")
    fun fontPaths(builder: MutableList<String>.() -> Unit) {
        fontPaths.addAll(buildList(builder))
    }

    @JvmName("fontPathsPath")
    fun fontPaths(builder: MutableList<Path>.() -> Unit) {
        fontPaths.addAll(buildList(builder).map { it.absolutePathString() })
    }

    @JvmName("fontPathsFile")
    fun fontPaths(builder: MutableList<File>.() -> Unit) {
        fontPaths.addAll(buildList(builder).map { it.absolutePath })
    }

    fun build(): FontsSpecification {
        val proto = prototype
        if (proto != null) {
            if (includeSystemV == null && includeEmbeddedV == null && fontPaths.isEmpty()) {
                return FontsSpecification.BorrowedFontCollection(proto)
            } else {
                prototype(proto.originalSpec)
                return build() // Try again
            }
        } else {
            return FontsSpecification.NewFontCollection(
                includeSystemV ?: false,
                includeEmbeddedV ?: false,
                fontPaths
            )
        }
    }
}