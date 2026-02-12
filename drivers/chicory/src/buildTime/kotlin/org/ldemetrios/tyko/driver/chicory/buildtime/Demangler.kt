package org.ldemetrios.tyko.driver.chicory.buildtime

import com.dylibso.chicory.runtime.Instance
import com.dylibso.chicory.wasm.Parser

object Demangler {
    private val instance: Instance = Instance.builder(
        Parser.parse(
            Demangler::class.java.classLoader.getResourceAsStream("rustc_demangle.wasm")
        )
    )
        .build()

    private fun rustcDemangle(mangled: String): String {
        var mangledPtr = 0
        val mangledSize = mangled.length + 1
        var outPtr = 0
        var outSize = 0

        try {
            mangledPtr = instance.export("alloc").apply(mangledSize.toLong())[0].toInt()
            instance.memory().writeCString(mangledPtr, mangled)

            outSize = instance.export("rustc_demangle_len").apply(mangledPtr.toLong())[0].toInt()
            if (outSize == 0) {
                return mangled
            }
            outPtr = instance.export("alloc").apply(outSize.toLong())[0].toInt()
            val result = instance.export("rustc_demangle")
                .apply(mangledPtr.toLong(), outPtr.toLong(), outSize.toLong())[0].toInt()
            if (result == 0) {
                return mangled
            }
            return instance.memory().readCString(outPtr)
        } finally {
            if (mangledPtr != 0) {
                instance.export("dealloc").apply(mangledPtr.toLong(), mangledSize.toLong())
            }
            if (outPtr != 0 && outSize != 0) {
                instance.export("dealloc").apply(outPtr.toLong(), outSize.toLong())
            }
        }
    }

    fun demangle(str: String): String {
        return rustcDemangle(str)
    }
}
