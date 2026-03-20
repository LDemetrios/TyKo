package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.PointerAccess
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.PointerType
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo
import org.ldemetrios.tyko.driver.api.Session
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi

sealed class LibraryPointerReference {
    abstract fun release()
    abstract fun clone(): LibraryPointerReference
    internal abstract fun <R> withPtr(func: (PointerAccess) -> R): R
    protected abstract val owner: TypstCompiler
    protected abstract fun cloneCarriedNativeFuncData(): List<NativeFuncDataPointerReference>
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withInputs(
        inputs: String,
        fonts: FontCollectionPointerReference,
    ): RResult<LibraryPointer, List<SourceDiagnostic>> = withPtr {
        fonts.withPtr { fontsPtr ->
            owner.bridge.withInputs(null, it, fontsPtr, inputs) { str, ptrConstr ->
                str
                    .deserialize<RResult<Long, List<SourceDiagnostic>>>()
                    .sanitize()
                    .map { LibraryPointer(ptrConstr(it), owner, cloneCarriedNativeFuncData()) }
            }
        }
    }

    /**
     * Construct a new [LibraryPointer] with built-in test definitions attached.
     */
    @TyKoInternalApi
    fun withTestDefinitions(): LibraryPointer = withPtr {
        LibraryPointer(owner.bridge.withTestDefinitions(it), owner, cloneCarriedNativeFuncData())
    }

    /**
     * Construct a new [LibraryPointer] with the same definitions, but with new default styles applied.
     * Returns either a new Library, or an error if `styled` failed to compile.
     *
     * @param styled The Typst _code_, representing a "styled" element, which the style will be taken from.
     * @param fonts Fonts accessible while compiling `styled`.
     * @param append Whether styles should be appended instead of replacing the existing style set.
     */
    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withStyles(
        styled: String,
        fonts: FontCollectionPointerReference,
        append: Boolean,
    ): RResult<LibraryPointer, List<SourceDiagnostic>> = withPtr { libraryPtr ->
        fonts.withPtr { fontsPtr ->
            owner.bridge.withStyles(
                null,
                libraryPtr,
                fontsPtr,
                styled,
                append,
            ) { str, ptrContsr ->
                str.deserialize<RResult<Long, List<SourceDiagnostic>>>()
                    .sanitize()
                    .map { LibraryPointer(ptrContsr(it), owner, cloneCarriedNativeFuncData()) }
            }
        }
    }

    @TyKoInternalApi
    fun withNativeFunction(
        name: String,
        funcData: NativeFuncDataPointerReference,
    ): LibraryPointer = withPtr { libraryPtr ->
        funcData.withPtr { nativeFuncPtr ->
            val carried = cloneCarriedNativeFuncData().toMutableList()
            carried += funcData.clone()
            LibraryPointer(
                owner.bridge.withNativeFunction(
                    libraryPtr,
                    nativeFuncPtr,
                    name,
                ),
                owner,
                carried
            )
        }
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withValue(
        name: String,
        value: String,
        fonts: FontCollectionPointerReference,
    ): RResult<LibraryPointer, List<SourceDiagnostic>> = withPtr { libraryPtr ->
        fonts.withPtr { fontsPtr ->
            owner.bridge.withValue(
                null,
                libraryPtr,
                fontsPtr,
                name,
                value,
            ) { str, ptrConstr ->
                str.deserialize<RResult<Long, List<SourceDiagnostic>>>()
                    .sanitize()
                    .map { LibraryPointer(ptrConstr(it), owner, cloneCarriedNativeFuncData()) }
            }
        }
    }
}

/**
 * Represents a set of definitions, which the compilation of documents will consider to be stdlib.
 *
 * Instances manage a native pointer and must be used with the respect to [Pointer] handling rules (most notably, closed in time).
 */
class LibraryPointer(
    internal val pointer: Pointer,
    override val owner: TypstCompiler,
    private val carriedNativeFuncData: List<NativeFuncDataPointerReference> = emptyList(),
) : PointerWrapper, LibraryPointerReference() {
    override val type: PointerType get() = PointerType.Library

    init {
        require(pointer.type == type) { "Expected $type pointer, got ${pointer.type}" }
        owner.register(this)
        pointer.addShutdownHook { owner.unregister(this) }
    }

    private inner class Reference(
        val ref: Pointer.Reference,
        val carriedNativeFuncData: List<NativeFuncDataPointerReference>,
    ) : LibraryPointerReference() {
        override val owner get() = this@LibraryPointer.owner
        override fun release() {
            ref.release()
            carriedNativeFuncData.forEach { it.release() }
        }

        override fun clone() = Reference(
            ref.clone(),
            carriedNativeFuncData.map { it.clone() },
        )

        override fun <R> withPtr(func: (PointerAccess) -> R): R {
            return withCarriedNativeFuncData(carriedNativeFuncData) {
                ref.withPointer(owner.bridge, PointerType.Library) { func(ref) }
            }
        }

        override fun cloneCarriedNativeFuncData() = carriedNativeFuncData.map { it.clone() }
        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = this@LibraryPointer.debugInfo()
    }

    fun reference(): LibraryPointerReference =
        Reference(pointer.reference(), carriedNativeFuncData.map { it.clone() })

    override fun cloneCarriedNativeFuncData() = carriedNativeFuncData.map { it.clone() }
    override fun clone() = reference()
    override fun <R> withPtr(func: (PointerAccess) -> R): R {
        return withCarriedNativeFuncData(carriedNativeFuncData) {
            pointer.withPointer(owner.bridge, PointerType.Library) { func(pointer) }
        }
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() {
        carriedNativeFuncData.forEach { it.release() }
        pointer.release()
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun alterInputs(
        inputs: String,
        fonts: FontCollectionPointerReference,
    ): RResult<LibraryPointer, List<SourceDiagnostic>> {
        return fonts.withPtr { fontsPtr ->
            owner.bridge.alterInputs(null, pointer, fontsPtr, inputs) { str, ptrContr ->
                str.deserialize<RResult<Long, List<SourceDiagnostic>>>()
                    .sanitize()
                    .map { LibraryPointer(ptrContr(it), owner, cloneCarriedNativeFuncData()) }
            }
        }
    }


    /**
     * Construct a new [LibraryPointer] with built-in test definitions attached.
     */
    @TyKoInternalApi
    fun addTestDefinitions(): LibraryPointer {
        return LibraryPointer(owner.bridge.addTestDefinitions(pointer), owner, cloneCarriedNativeFuncData())
    }

    /**
     * Construct a new [LibraryPointer] with the same definitions, but with new default styles applied.
     * Returns either a new Library, or an error if `styled` failed to compile.
     *
     * @param styled The Typst _code_, representing a "styled" element, which the style will be taken from.
     * @param fonts Fonts accessible while compiling `styled`.
     * @param append Whether styles should be appended instead of replacing the existing style set.
     */
    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun alterStyles(
        styled: String,
        fonts: FontCollectionPointerReference,
        append: Boolean,
    ): RResult<LibraryPointer, List<SourceDiagnostic>> = fonts.withPtr { fontsPtr ->
        owner.bridge.alterStyles(null, pointer, fontsPtr, styled, append) { str, ptrContr ->
            str.deserialize<RResult<Long, List<SourceDiagnostic>>>()
                .sanitize().map { LibraryPointer(ptrContr(it), owner, cloneCarriedNativeFuncData()) }
        }
    }

    @TyKoInternalApi
    fun addNativeFunction(
        name: String,
        funcData: NativeFuncDataPointerReference,
    ): LibraryPointer {
        return funcData.withPtr { nativeFuncPtr ->
            val carried = cloneCarriedNativeFuncData().toMutableList()
            carried += funcData.clone()
            LibraryPointer(
                owner.bridge.addNativeFunction(
                    pointer,
                    nativeFuncPtr,
                    name,
                ),
                owner,
                carried
            )
        }
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun addValue(
        name: String,
        value: String,
        fonts: FontCollectionPointerReference,
    ): RResult<LibraryPointer, List<SourceDiagnostic>> = fonts.withPtr { fontsPtr ->
        owner.bridge.addValue(
            null,
            pointer,
            fontsPtr,
            name,
            value,
        ) { str, ptrConstr ->
            str.deserialize<RResult<Long, List<SourceDiagnostic>>>()
                .sanitize()
                .map { LibraryPointer(ptrConstr(it), owner, cloneCarriedNativeFuncData()) }
        }
    }

    override fun consume(body: (Pointer) -> Unit) {
        try {
            pointer.consume(owner.bridge, type) { body(pointer) }
        } finally {
            carriedNativeFuncData.forEach { it.release() }
        }
    }

    private fun <R> withCarriedNativeFuncData(
        refs: List<NativeFuncDataPointerReference>,
        body: () -> R,
    ): R {
        fun nested(index: Int): R {
            if (index >= refs.size) return body()
            return refs[index].withPtr { nested(index + 1) }
        }
        return nested(0)
    }
}
