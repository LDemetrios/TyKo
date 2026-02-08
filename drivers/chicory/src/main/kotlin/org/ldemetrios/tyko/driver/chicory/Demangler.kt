package org.ldemetrios.tyko.driver.chicory

import com.dylibso.chicory.runtime.Instance
import com.dylibso.chicory.wasm.Parser

object Demangler {
    private val INSTANCE: Instance = Instance.builder(
        Parser.parse(
            Demangler::class.java.getClassLoader().getResourceAsStream("rustc_demangle.wasm")
        )
    )
        .build()

    // Demangles symbol given in `mangled` argument into `out` buffer
    //
    // Returns 0 if `mangled` is not Rust symbol or if `out` buffer is too small
    // Returns 1 otherwise
    // int rustc_demangle(const char *mangled, char *out, size_t out_size);
    private fun rustcDemangle(mangled: String): String? {
        var mangledPtr = 0
        val mangledSize = mangled.length + 1
        var outPtr = 0
        var outSize = 0

        try {
            mangledPtr = INSTANCE.export("alloc").apply(mangledSize.toLong())[0].toInt()
            INSTANCE.memory().writeCString(mangledPtr, mangled)

            outSize = INSTANCE.export("rustc_demangle_len").apply(mangledPtr.toLong())[0].toInt()
            // Not a Rust symbol
            if (outSize == 0) {
                return mangled
            } else {
                outPtr = INSTANCE.export("alloc").apply(outSize.toLong())[0].toInt()
                val result = INSTANCE.export("rustc_demangle")
                    .apply(mangledPtr.toLong(), outPtr.toLong(), outSize.toLong())[0].toInt()
                if (result == 0) {
                    throw RuntimeException(
                        ("Failed to demangle '"
                                + mangled
                                + "' is not Rust symbol or the out buffer is too small")
                    )
                } else {
                    return INSTANCE.memory().readCString(outPtr)
                }
            }
        } finally {
            if (mangledPtr != 0) {
                INSTANCE.export("dealloc").apply(mangledPtr.toLong(), mangledSize.toLong())
            }
            if (outPtr != 0 && outSize != 0) {
                INSTANCE.export("dealloc").apply(outPtr.toLong(), outSize.toLong())
            }
        }
    }

    fun demangle(str: String): String? {
        return rustcDemangle(str)
    }
}