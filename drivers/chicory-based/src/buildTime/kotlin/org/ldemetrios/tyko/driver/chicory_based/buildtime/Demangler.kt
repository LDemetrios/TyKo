package org.ldemetrios.tyko.driver.chicory_based.buildtime

import com.dylibso.chicory.runtime.Instance
import com.dylibso.chicory.wasm.Parser
import org.ldemetrios.tyko.driver.chicory_based.buildtime.Demangler.demangle

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

fun main() {
    val warnings = $$$"""
Warning: using interpreted mode for WASM function index: 2072 (name: _ZN9hayagriva7interop96_$LT$impl$u20$core..convert..TryFrom$LT$$RF$biblatex..Entry$GT$$u20$for$u20$hayagriva..Entry$GT$8try_from17h894bde5fbaac1d09E)
Warning: using interpreted mode for WASM function index: 6398 (name: _ZN14regex_automata4meta8strategy3new17he89e2ffbfcbc6a75E)
Warning: using interpreted mode for WASM function index: 10589 (name: _ZN12typst_layout4math15layout_realized17ha121143aa60d717aE)
Warning: using interpreted mode for WASM function index: 14614 (name: _ZN13typst_library9visualize5color3map17ha2cfc28ad6656d1fE)
Warning: using interpreted mode for WASM function index: 25331 (name: _ZN5wasmi6engine8executor6instrs14execute_instrs17heba21acf43c2d478E)
        """.trimIndent()
        .lines()
        .map { it.split("(name: ").last().trimEnd(')') }
        .filter { it.isNotBlank() }
        .map { demangle(it) }
        .forEach { println(it) }
}

