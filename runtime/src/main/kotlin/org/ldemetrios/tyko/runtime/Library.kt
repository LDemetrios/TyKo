package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.LibraryPointer
import org.ldemetrios.tyko.compiler.LibraryPointerReference
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.SourceDiagnostic
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceDebugMode
import org.ldemetrios.tyko.driver.api.ResourceReference
import org.ldemetrios.tyko.driver.api.Session
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.withSession
import org.ldemetrios.tyko.model.*


sealed class LibraryReference {
    abstract fun release()
    abstract fun clone(): LibraryReference
    internal abstract fun <R> withPtrs(func: (LibraryPointerReference, Session?, List<Session>, List<Session>) -> R): R
    protected abstract val owner: TypstRuntime
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>
    protected abstract val debugMode: ResourceDebugMode

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withInputs(
        inputs: TDict<*>,
        fonts: FontCollectionReference,
    ): RResult<Library, List<SourceDiagnostic>> = withPtrs { lib, capInputs, capStyles, capValues ->
        fonts.withPtr { fontsPtr ->
            val (session, repr) = reprWithNewSession(inputs, CreateSessionMode.IfNeeded, debugMode)
            lib.withInputs(repr, fontsPtr)
                .map {
                    Library(
                        it,
                        owner,
                        session,
                        capStyles.cloneRefs(),
                        capValues.cloneRefs(),
                        debugMode
                    )
                }
                .mapError { session?.release(); it }
        }
    }

    @TyKoInternalApi
    fun withTestDefinitions(): Library = withPtrs { lib, capInputs, capStyles, capValues ->
        Library(
            lib.withTestDefinitions(),
            owner,
            capInputs?.reference(),
            capStyles.cloneRefs(),
            capValues.cloneRefs(),
            debugMode
        )
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withStyles(
        vararg styles: TStyle,
        fonts: FontCollectionReference,
        append: Boolean,
    ): RResult<Library, List<SourceDiagnostic>> = withPtrs { lib, capInputs, capStyles, capValues ->
        fonts.withPtr { fontsPtr ->
            val (session, repr) = reprWithNewSession(styled(styles), CreateSessionMode.IfNeeded, debugMode)
            lib.withStyles(repr, fontsPtr, append)
                .map {
                    Library(
                        it,
                        owner,
                        capInputs?.reference(),
                        if (append) capStyles.cloneWithNew(session)
                        else listOfNotNull(session),
                        capValues.cloneRefs(),
                        debugMode
                    )
                }
                .mapError { session?.release(); it }
        }
    }

    @OptIn(TyKoInternalApi::class)
    fun withNativeFunction(
        name: String,
        funcData: NativeFuncDataReference,
    ): Library = withPtrs { lib, capInputs, capStyles, capValues ->
        funcData.withPtr { funcDataPtr ->
            Library(
                lib.withNativeFunction(name, funcDataPtr),
                owner,
                capInputs?.reference(),
                capStyles.cloneRefs(),
                capValues.cloneRefs(),
                debugMode
            )
        }
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun withValue(
        name: String,
        value: TValue,
        fonts: FontCollectionReference,
    ): RResult<Library, List<SourceDiagnostic>> = withPtrs { lib, capInputs, capStyles, capValues ->
        fonts.withPtr { fontsPtr ->
            val (session, repr) = reprWithNewSession(value, CreateSessionMode.IfNeeded, debugMode)
            lib.withValue(name, repr, fontsPtr).map {
                Library(
                    it,
                    owner,
                    capInputs?.reference(),
                    capStyles.cloneRefs(),
                    capValues.cloneWithNew(session),
                    debugMode
                )
            }.mapError {
                session?.release()
                it
            }
        }
    }
}

fun <T : Any> List<ResourceReference<T>>.cloneRefs() = map { it.reference() }
fun <T : Any> List<ResourceReference<T>>.cloneWithNew(vararg news: ResourceReference<T>?) =
    map { it.reference() } + listOfNotNull(*news)

class Library(
    internal val pointer: LibraryPointer,
    override val owner: TypstRuntime,
    internal val capturedInputs: Session?,
    internal val capturedStyles: List<Session>,
    internal val capturedValues: List<Session>,
    override val debugMode: ResourceDebugMode
) : LibraryReference() {
    private inner class Reference(
        val ref: LibraryPointerReference,
        val inputRef: Session?,
        val stylesRefs: List<Session>,
        val valuesRefs: List<Session>,
    ) : LibraryReference() {
        override val owner get() = this@Library.owner
        override fun release() {
            ref.release()
            inputRef?.release()
            stylesRefs.forEach { it.release() }
            valuesRefs.forEach { it.release() }
        }

        override fun clone() = Reference(
            ref.clone(),
            inputRef?.reference(),
            stylesRefs.cloneRefs(),
            valuesRefs.cloneRefs()
        )

        override fun <R> withPtrs(func: (LibraryPointerReference, Session?, List<Session>, List<Session>) -> R): R {
            return func(ref, inputRef, stylesRefs, valuesRefs)
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = ref.debugInfo()
        override val debugMode: ResourceDebugMode get() = this@Library.debugMode
    }

    fun reference(): LibraryReference = Reference(
        pointer.reference(),
        capturedInputs?.reference(),
        capturedStyles.cloneRefs(),
        capturedValues.cloneRefs(),
    )

    override fun clone() = reference()
    override fun <R> withPtrs(func: (LibraryPointerReference, Session?, List<Session>, List<Session>) -> R): R {
        return func(pointer, capturedInputs, capturedStyles, capturedValues)
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() {
        pointer.release()
        capturedValues.forEach { it.release() }
        capturedInputs?.release()
        capturedStyles.forEach { it.release() }
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun alterInputs(
        inputs: TDict<*>,
        fonts: FontCollectionReference,
    ): RResult<Library, List<SourceDiagnostic>> = fonts.withPtr { fontsPtr ->
        withSession(capturedInputs) {
            withSessionList(capturedStyles) {
                withSessionList(capturedValues) {
                    try {
                        val (session, repr) = reprWithNewSession(inputs, CreateSessionMode.IfNeeded, debugMode)
                        pointer.alterInputs(repr, fontsPtr)
                            .map { Library(it, owner, session, this.capturedStyles, this.capturedValues, debugMode) }
                            .mapError {
                                session?.release()
                                this.capturedStyles.release()
                                this.capturedValues.release()
                                it
                            }
                    } finally {
                        this.capturedInputs?.release()
                    }
                }
            }
        }
    }

    @TyKoInternalApi
    fun addTestDefinitions(): Library {
        return Library(pointer.addTestDefinitions(), owner, capturedInputs, capturedStyles, capturedValues, debugMode)
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun alterStyles(
        vararg styles: TStyle,
        fonts: FontCollectionReference,
        append: Boolean,
    ): RResult<Library, List<SourceDiagnostic>> = fonts.withPtr { fontsPtr ->
        withSession(capturedInputs) {
            withSessionList(capturedStyles) {
                withSessionList(capturedValues) {
                    try {
                        val (session, repr) = reprWithNewSession(styled(styles), CreateSessionMode.IfNeeded, debugMode)
                        this.pointer.alterStyles(repr, fontsPtr, append)
                            .map {
                                Library(
                                    it,
                                    owner,
                                    capturedInputs,
                                    if (append) {
                                        capturedStyles + listOfNotNull(session)
                                    } else {
                                        listOfNotNull(session)
                                    },
                                    capturedValues,
                                    debugMode
                                )
                            }
                            .mapError {
                                session?.release()
                                this.capturedInputs?.release()
                                if (append) this.capturedStyles.release()
                                this.capturedValues.release()
                                it
                            }
                    } finally {
                        if (!append) this.capturedStyles.release()
                    }
                }
            }
        }
    }

    @OptIn(TyKoInternalApi::class)
    fun addNativeFunction(
        name: String,
        funcData: NativeFuncDataReference,
    ): Library {
        return withSession(capturedInputs) {
            withSessionList(capturedStyles) {
                withSessionList(capturedValues) {
                    funcData.withPtr { funcDataPtr ->
                        Library(
                            pointer.addNativeFunction(name, funcDataPtr),
                            owner,
                            capturedInputs,
                            capturedStyles,
                            capturedValues,
                            debugMode
                        )
                    }
                }
            }
        }
    }

    @OptIn(TyKoFFIEntity::class, TyKoInternalApi::class)
    fun addValue(
        name: String,
        value: TValue,
        fonts: FontCollectionReference,
    ): RResult<Library, List<SourceDiagnostic>> = fonts.withPtr { fontsPtr ->
        withSession(capturedInputs) {
            withSessionList(capturedStyles) {
                withSessionList(capturedValues) {
                    val (session, repr) = reprWithNewSession(value, CreateSessionMode.IfNeeded, debugMode)
                    pointer.addValue(name, repr, fontsPtr)
                        .map {
                            Library(
                                it,
                                owner,
                                capturedInputs,
                                capturedStyles,
                                capturedValues + listOfNotNull(session),
                                debugMode
                            )
                        }.mapError {
                            session?.release()
                            capturedInputs?.release()
                            capturedStyles.release()
                            capturedValues.release()
                            it
                        }
                }
            }
        }
    }
}

private fun styled(styles: Array<out TStyle>): TStyled {
    return TStyled(
        TStyles(
            TArray(
                styles.asList()
            )
        ),
        TSpace(),
    )
}

private fun <R> withSessionList(sessions: List<Session>, body: () -> R): R {
    val reserved = mutableListOf<Session>()
    return try {
        for (session in sessions) {
            reserved.add(session.reference())
        }
        body()
    } finally {
        reserved.forEach { it.release() }
    }
}

private fun List<Session>.release() = forEach { it.release() }
