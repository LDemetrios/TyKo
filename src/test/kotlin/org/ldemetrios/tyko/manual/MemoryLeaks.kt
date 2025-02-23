package org.ldemetrios.tyko

import com.sun.jna.Native
import org.ldemetrios.tyko.compiler.DetachedWorld
import org.ldemetrios.tyko.compiler.TypstCompilerException
import org.ldemetrios.tyko.compiler.WorldBasedTypstCompiler
import org.ldemetrios.tyko.compiler.evalDetached
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.original.SHARED_LIBRARY_PATH
import kotlin.io.path.Path

// They exist...

fun main() {
    val world = WorldBasedTypstCompiler(TypstSharedLibrary.instance(Path(SHARED_LIBRARY_PATH)), DetachedWorld())
    val lim = 20000000
    for (i in 0 until lim) {
        world.evalDetached("\"1 + 2\"")
    }
}