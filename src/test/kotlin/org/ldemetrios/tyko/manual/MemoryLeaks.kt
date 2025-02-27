package org.ldemetrios.tyko.manual

import org.ldemetrios.tyko.compiler.DetachedWorld
import org.ldemetrios.tyko.compiler.WorldBasedTypstCompiler
import org.ldemetrios.tyko.compiler.evalDetached
import org.ldemetrios.tyko.original.sharedLib

// They exist...

fun main() {
    val world = WorldBasedTypstCompiler(sharedLib, DetachedWorld())
        val lim = 20000000
    for (i in 0 until lim) {
        world.evalDetached("\"1 + 2\"")
    }
}