//package org.ldemetrios.tyko
//
//import com.sun.jna.Native
//import org.ldemetrios.tyko.compiler.DetachedWorld
//import org.ldemetrios.tyko.compiler.WorldBasedTypstCompiler
//import org.ldemetrios.tyko.compiler.evalDetached
//import org.ldemetrios.tyko.ffi.SHARED_LIBRARY_PATH
//import org.ldemetrios.tyko.ffi.TypstSharedLibrary
//
//fun main() {
//    val world = TestCompiler()
//    val lim = 20000000
//    for (i in 0 until lim) {
//        world.evalDetached("\"1 + 2\"")
//    }
//}