package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.Now
import org.ldemetrios.tyko.compiler.NativeFuncDataDescriptor
import org.ldemetrios.tyko.compiler.PackageError
import org.ldemetrios.tyko.compiler.PagedDocumentPointer
import org.ldemetrios.tyko.compiler.PdfOptionsConfig
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.SourceDiagnostic
import org.ldemetrios.tyko.compiler.SyntaxMode
import org.ldemetrios.tyko.compiler.SyntaxTree
import org.ldemetrios.tyko.compiler.TypstCompiler
import org.ldemetrios.tyko.compiler.Warned
import org.ldemetrios.tyko.compiler.orThrow
import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrar
import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrarResource
import org.ldemetrios.tyko.driver.api.ResourceDebugMode
import org.ldemetrios.tyko.driver.api.TyKoCorrectnessAlert
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.TypstCore
import org.ldemetrios.tyko.driver.api.registerJavaObject
import org.ldemetrios.tyko.driver.api.resolveJavaObject
import org.ldemetrios.tyko.driver.api.withSession
import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TSelector
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.deserialize
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.TArgs
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TNone
import org.ldemetrios.tyko.model.TStyle
import org.ldemetrios.tyko.model.TStyles
import org.ldemetrios.tyko.model.repr
import java.io.Closeable

private val defaultDefaultsSpec = buildWorldSpecification {
    library {
        fresh(Feature.none())
    }
    fonts {
        includeSystem = false
        includeEmbedded = true
    }
    files {
        fallback { _, _ -> RResult.Err(FileError.AccessDenied) }
    }
    useSystemTime()
    createSessionIfNeeded()
}

/**
 * Brings together object model of Typst's elements ([IntoValue] hierarchy), and the [TypstCompiler].
 * For methods in TypstCompiler that require specific Typst code, TypstRuntime provides a typed wrapper.
 * Additionally, it handles common defaults for fonts, stdlib, etc.
 *
 * @param bridge An instance of [TypstCore] to wrap. No more than one TypstRuntime can possess a single core
 * @param defaultFeatures features, which default library will be created with
 */
class TypstRuntime private constructor(
    val sessionDebugMode: ResourceDebugMode,
    val spec: WorldSpecification,
    val compiler: TypstCompiler,
    val parent: TypstRuntime?,
) : Closeable {
    constructor(
        defaultsSpec: WorldSpecification = defaultDefaultsSpec,
        sessionDebugMode: ResourceDebugMode = ResourceDebugMode.None,
        bridge: () -> TypstCore
    ) : this(sessionDebugMode, defaultsSpec, TypstCompiler(bridge), null)

    fun inherit(
        worldSpec: WorldSpecification,
        sessionDebugMode: ResourceDebugMode? = null,
    ): TypstRuntime = TypstRuntime(
        sessionDebugMode ?: this.sessionDebugMode,
        worldSpec,
        compiler.childScope(),
        this,
    )

    fun inherit(
        sessionDebugMode: ResourceDebugMode? = null,
        worldSpecBuilder: WorldSpecBuilder.() -> Unit,
    ): TypstRuntime = inherit(buildWorldSpecification(worldSpecBuilder), sessionDebugMode)

    private fun resolveFileContext(spec: FileContextSpecification?, holder: TypstRuntime?): FSContextReference {
        if (spec == null) return holder?.defaultFileCtx?.clone()
            ?: fileContextOf { RResult.Err(FileError.AccessDenied) }

        val session = JavaObjectsRegistrarResource(holder?.sessionDebugMode ?: sessionDebugMode)
        return FSContext(
            compiler.fileContext(
                { it, session ->
                    for (matcher in spec.matchers) {
                        try {
                            val result = matcher.tryResolve(it, session)
                            if (result != null) return@fileContext result
                        } catch (e: ThreadDeath) {
                            throw e
                        } catch (e: VirtualMachineError) {
                            throw e
                        } catch (e: Throwable) {
                            if (spec.failfast) {
                                val here = Thread.currentThread().stackTrace.reversed()
                                val there = e.stackTrace.reversed()

                                val overlap = minOf(there.size, here.size)
                                val common = (0 until overlap).takeWhile { there[it] == here[it] }.size
                                val errorString = e.toString() + "\n" + there.drop(common).reversed().joinToString("\n")
                                return@fileContext RResult.Err(FileError.Other(errorString))
                            } else {
                                continue
                            }
                        }
                    }
                    return@fileContext RResult.Err(FileError.Other("No matchers found for $it"))
                },
                session
            ),
            this,
            session
        )
    }

    private fun resolveLibrary(
        spec: LibrarySpecification?,
        fonts: FontCollectionReference,
        holder: TypstRuntime?
    ): LibraryReference {
        if (spec == null) return holder?.defaultLibrary?.clone() ?: library(setOf())

        // Resolve prototype:
        val proto = when (spec.prototype) {
            is LibraryPrototypeSpecification.Fresh -> library(spec.prototype.features)
            LibraryPrototypeSpecification.UseDefault -> holder?.defaultLibrary?.clone() ?: library(setOf())
            is LibraryPrototypeSpecification.UseReference -> spec.prototype.ref.clone()
        }

        if (spec.appendStyles && spec.additionalStyles.isEmpty() && spec.inputs == null && spec.values.isEmpty() && spec.funcs.isEmpty()) {
            return proto
        }

        val alreadyOwned = when (spec.prototype) {
            is LibraryPrototypeSpecification.Fresh -> true
            LibraryPrototypeSpecification.UseDefault -> holder == null
            is LibraryPrototypeSpecification.UseReference -> false
        }

        var library = if (alreadyOwned) proto as Library else proto.withStyles(fonts = fonts, append = false).orThrow()

        if (!spec.appendStyles || !spec.additionalStyles.isEmpty()) {
            library = library
                .alterStyles(*spec.additionalStyles.toTypedArray(), fonts = fonts, append = spec.appendStyles)
                .orThrow()
        }

        if (spec.inputs != null) {
            library = library.alterInputs(spec.inputs, fonts).orThrow()
        }

        for ((name, value) in spec.values) {
            library = library.addValue(name, value, fonts).orThrow()
        }

        for ((name, func) in spec.funcs) {
            val value = when (func) {
                is FuncSpecification.BorrowedFunction -> func.ref.clone()
                is FuncSpecification.NewFunction -> {
                    nativeFuncData(func.descriptor, func.executor)
                }
            }

            try {
                library = library.addNativeFunction(name, value)
            } finally {
                value.release()
            }
        }

        return library
    }

    private fun resolveFonts(spec: FontsSpecification?, holder: TypstRuntime?): FontCollectionReference {
        if (spec == null) return holder?.defaultFonts?.clone() ?: fontCollection(false, true)

        return when (spec) {
            is FontsSpecification.BorrowedFontCollection -> spec.ref.clone()
            is FontsSpecification.NewFontCollection -> fontCollection(
                spec.includeSystem,
                spec.includeEmbedded,
                spec.fontPaths
            )
        }
    }

    private fun resolveNow(spec: Now?, holder: TypstRuntime?): Now = when {
        spec != null -> spec
        holder == null -> Now.System
        else -> resolveNow(holder.spec.worldTimeSpecification, holder.parent)
    }

    private fun resolveSessionMode(spec: CreateSessionMode?, holder: TypstRuntime?): CreateSessionMode = when {
        spec != null -> spec
        holder == null -> CreateSessionMode.IfNeeded
        else -> resolveSessionMode(holder.spec.jvmObjectsSession, holder.parent)
    }

    private val defaultFileCtxF = lazy {
        resolveFileContext(spec.fsContextSpecification, parent)
    }

    private val defaultFontsF = lazy {
        resolveFonts(spec.fontsSpecification, parent)
    }

    private val defaultLibraryF = lazy {
        resolveLibrary(spec.librarySpecification, defaultFonts, parent)
    }

    private val defaultFileCtx by defaultFileCtxF
    private val defaultLibrary by defaultLibraryF
    private val defaultFonts by defaultFontsF

    /**
     * Call Typstyle formatter on a code, with given settings.
     *
     * @param content The source code to format
     * @param column How wide the result should be, maximum
     * @param tabWidth The indentation shift
     */
    fun formatSource(content: String, column: Int, tabWidth: Int): String {
        return compiler.formatSource(content, column, tabWidth)
    }

    /**
     * Construct a Library, carrying all definitions achievable from within a document.
     *
     * @param features Set of optional [Feature]s to enable.
     */
    fun library(features: Set<Feature> = setOf()): Library {
        return Library(compiler.library(features), this, null, listOf(), listOf(), sessionDebugMode)
    }

    fun library(builder: LibrarySpecBuilder.() -> Unit): LibraryReference {
        return resolveLibrary(LibrarySpecBuilder().apply(builder).build(), defaultFonts, this)
    }

    /**
     * Construct a Library, carrying all definitions achievable from within a document.
     *
     * @param features Optional [Feature]s to enable.
     */
    fun library(vararg features: Feature): Library {
        return library(features.toSet())
    }

    @OptIn(TyKoInternalApi::class)
    fun nativeFuncData(
        descriptor: NativeFuncDataDescriptor,
        hostCall: (args: TArgs<TValue>, context: TStyles) -> RResult<TValue, String>,
    ): NativeFuncData {
        return NativeFuncData(compiler.nativeFuncData(descriptor) { session, argsJson, contextJson ->
            val args = deserialize(argsJson, ::resolveJavaObject) as TArgs<TValue>
            val context = deserialize(contextJson, ::resolveJavaObject) as? TStyles
            hostCall(args, context ?: TStyles(TArray(listOf()))).map {
                it.repr { jobj ->
                    if (session == 0L) throw IllegalArgumentException("Can't transfer java object without a session")
                    else session to registerJavaObject(session, jobj)
                }
            }
        }, this)
    }

    /**
     * Perform a [query](https://typst.app/docs/reference/introspection/query/) on the document.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param selector Typed selector expression
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param main The entry point of the compilation
     * @param now Time, which the compilation will consider to be "now"
     */
    @OptIn(TyKoCorrectnessAlert::class)
    fun <S : TValue> query(
        context: FSContextReference,
        selector: TSelector<S>,
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        now: Now = Now.System,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<TArray<S>, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(selector, sessionMode, sessionDebugMode)
        return try {
            withSession(session) {
                context.withPtr { contextPtr ->
                    fonts.withPtr { fontsPtr ->
                        library.withPtrs { libraryPtr, _, _, _ ->
                            compiler.queryRaw(session, contextPtr, fontsPtr, libraryPtr, main, now, repr)
                                .map { it.map { deserialize(it) as TArray<S> } }
                        }
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    fun <S : TValue> query(
        selector: TSelector<S>,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        spec: WorldSpecification
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> query(fs, selector, fonts, lib, main, now, ses) }

    fun <S : TValue> query(
        selector: TSelector<S>,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        builder: WorldSpecBuilder.() -> Unit
    ) = query(selector, main, WorldSpecBuilder().apply(builder).build())

    /**
     * Parse Typst source code into an abstract syntax tree.
     *
     * @param string Source code to parse
     * @param mode Parsing mode (markup, code, math)
     */
    fun parseSyntax(string: String, mode: SyntaxMode): SyntaxTree {
        return compiler.parseSyntax(string, mode)
    }

    private fun <T> withSpec(
        spec: WorldSpecification,
        producer: (LibraryReference, FontCollectionReference, FSContextReference, Now, CreateSessionMode) -> T,
    ): T {
        val fonts = resolveFonts(spec.fontsSpecification, this)
        val lib = resolveLibrary(spec.librarySpecification, fonts, this)
        val session = resolveSessionMode(spec.jvmObjectsSession, this)
        val fs = resolveFileContext(spec.fsContextSpecification, this)
        val now = resolveNow(spec.worldTimeSpecification, this)
        try {
            return producer(lib, fonts, fs, now, session)
        } finally {
            fonts.release()
            lib.release()
            fs.release()
        }
    }

    /**
     * Evaluate the entry file and return result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the evaluation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun eval(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ) = fonts.withPtr { fontsPtr ->
        val (session, _) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        try {
            withSession(session) {
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.evalRaw(session, contextPtr, fontsPtr, libraryPtr, main, now)
                            .map { it.map { deserialize(it) } }
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    fun eval(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        spec: WorldSpecification
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> eval(fs, main, fonts, lib, now, ses) }

    fun eval(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        builder: WorldSpecBuilder.() -> Unit
    ) = eval(main, WorldSpecBuilder().apply(builder).build())


    /**
     * Compile a document to HTML.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun compileHtml(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<String, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        return try {
            fonts.withPtr { fontsPtr ->
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.compileHtmlRaw(
                            session,
                            contextPtr,
                            fontsPtr,
                            libraryPtr,
                            main,
                            now
                        )
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    fun compileHtml(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        spec: WorldSpecification
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> compileHtml(fs, main, fonts, lib, now, ses) }

    fun compileHtml(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        builder: WorldSpecBuilder.() -> Unit
    ) = compileHtml(main, WorldSpecBuilder().apply(builder).build())

    /**
     * Compile a document to SVG pages.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     */
    fun compileSvg(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<List<String>, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        return try {
            fonts.withPtr { fontsPtr ->
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.compileSvgRaw(
                            session,
                            contextPtr,
                            fontsPtr,
                            libraryPtr,
                            main,
                            now,
                            from,
                            to
                        )
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    fun compileSvg(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        spec: WorldSpecification,
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> compileSvg(fs, main, fonts, lib, now, from, to, ses) }

    fun compileSvg(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        builder: WorldSpecBuilder.() -> Unit,
    ) = compileSvg(main, from, to, WorldSpecBuilder().apply(builder).build())

    /**
     * Compile a document to PNG pages.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from First page index (inclusive)
     * @param to Last page index (exclusive)
     * @param ppi Output resolution in pixels per inch
     */
    fun compilePng(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        ppi: Double = 144.0,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        return try {
            fonts.withPtr { fontsPtr ->
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.compilePngRaw(
                            session,
                            contextPtr,
                            fontsPtr,
                            libraryPtr,
                            main,
                            now,
                            from,
                            to,
                            ppi
                        )
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    fun compilePng(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        ppi: Double = 144.0,
        spec: WorldSpecification,
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> compilePng(fs, main, fonts, lib, now, from, to, ppi, ses) }

    fun compilePng(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        from: Int = 0,
        to: Int = Int.MAX_VALUE,
        ppi: Double = 144.0,
        builder: WorldSpecBuilder.() -> Unit,
    ) = compilePng(main, from, to, ppi, WorldSpecBuilder().apply(builder).build())

    /**
     * Compile a document to a merged bitmap with positional links.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param ppi Output resolution in pixels per inch
     */
    @TyKoInternalApi
    fun compileMergedWithLinks(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        ppi: Double = 72.0,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        return try {
            fonts.withPtr { fontsPtr ->
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.compileMergedWithLinksRaw(
                            session,
                            contextPtr,
                            fontsPtr,
                            libraryPtr,
                            main,
                            now,
                            ppi
                        )
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    @TyKoInternalApi
    fun compileMergedWithLinks(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        ppi: Double = 144.0,
        spec: WorldSpecification,
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> compileMergedWithLinks(fs, main, fonts, lib, now, ppi, ses) }

    @TyKoInternalApi
    fun compileMergedWithLinks(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        ppi: Double = 144.0,
        builder: WorldSpecBuilder.() -> Unit,
    ) = compileMergedWithLinks(main, ppi, WorldSpecBuilder().apply(builder).build())

    /**
     * Compile a document to PDF.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     * @param from Kept for API symmetry with paged renderers
     * @param to Kept for API symmetry with paged renderers
     * @param options Optional PDF serialization options
     */
    fun compilePdf(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        options: PdfOptionsConfig? = null,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<ByteArray, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        return try {
            fonts.withPtr { fontsPtr ->
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.compilePdfRaw(
                            session,
                            contextPtr,
                            fontsPtr,
                            libraryPtr,
                            main,
                            now,
                            options
                        )
                    }
                }
            }
        } finally {
            session?.release()
        }
    }


    fun compilePdf(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        options: PdfOptionsConfig? = null,
        spec: WorldSpecification,
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> compilePdf(fs, main, fonts, lib, now, options, ses) }

    fun compilePdf(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        options: PdfOptionsConfig? = null,
        builder: WorldSpecBuilder.() -> Unit,
    ) = compilePdf(main, options, WorldSpecBuilder().apply(builder).build())

    /**
     * Precompile a paged document, delaying final format rendering.
     * Returns result as provided, with warnings and errors.
     *
     * @param context Virtual File System, which the file contents will be searched in
     * @param main The entry point of the compilation
     * @param fonts Fonts, available to compiler
     * @param library What the compilation will consider to be stdlib
     * @param now Time, which the compilation will consider to be "now"
     */
    fun precompilePaged(
        context: FSContextReference,
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        fonts: FontCollectionReference = defaultFonts,
        library: LibraryReference = defaultLibrary,
        now: Now = Now.System,
        sessionMode: CreateSessionMode = CreateSessionMode.No
    ): Warned<RResult<PagedDocumentPointer, List<SourceDiagnostic>>> {
        val (session, repr) = reprWithNewSession(TNone, sessionMode, sessionDebugMode)
        return try {
            fonts.withPtr { fontsPtr ->
                context.withPtr { contextPtr ->
                    library.withPtrs { libraryPtr, _, _, _ ->
                        compiler.precompilePagedRaw(
                            session,
                            contextPtr,
                            fontsPtr,
                            libraryPtr,
                            main,
                            now
                        )
                    }
                }
            }
        } finally {
            session?.release()
        }
    }

    fun precompilePaged(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        spec: WorldSpecification,
    ) = withSpec(spec) { lib, fonts, fs, now, ses -> precompilePaged(fs, main, fonts, lib, now, ses) }

    fun precompilePaged(
        main: FileDescriptor = FileDescriptor(null, "main.typ"),
        builder: WorldSpecBuilder.() -> Unit,
    ) = precompilePaged(main, WorldSpecBuilder().apply(builder).build())

    /**
     * Create a virtual file-system context.
     *
     * @param files Callback for retrieving file content. Accepts file location in VFS, returns either content or an error.
     */
    fun fileContextOf(files: (FileDescriptor) -> RResult<ByteArray, FileError>): FSContext {
        return FSContext(compiler.fileContext({ it, _ -> files(it) }, null), this, null)
    }

    fun fileContext(builder: FileContextSpecBuilder.() -> Unit): FSContext {
        return fileContext(FileContextSpecBuilder().apply(builder).build())
    }

    fun fileContext(spec: FileContextSpecification): FSContext {
        return resolveFileContext(spec, this) as FSContext // spec != null => FSContext is owned
    }

    /**
     * Use native mechanism for package resolution, for usual package namespaces like "preview" or "local".
     *
     * @param file file descriptor
     */
    fun resolvePackage(file: FileDescriptor): RResult<ByteArray, FileError> {
        return compiler.resolvePackage(file)
    }

    /**
     * Construct file-system context from virtual in-memory files.
     *
     * @param files Mapping from virtual path to file contents
     */
    fun fileContext(files: Map<String, String>): FSContext {
        return FSContext(
            compiler.fileContext({ it, _ ->
                files[it.virtualPath]?.let {
                    RResult.Ok(it.toByteArray())
                } ?: RResult.Err(
                    when (val pack = it.packageSpec) {
                        null -> FileError.NotFound(it.virtualPath)
                        else -> FileError.Package(PackageError.NotFound(pack))
                    }
                )
            }, null),
            this,
            null
        )
    }

    /**
     * Construct a collection of fonts available to compiler operations.
     *
     * @param includeSystem Whether system-installed fonts should be loaded
     * @param includeEmbedded Whether embedded fallback fonts should be loaded
     * @param fontPaths Additional file-system paths to scan for fonts
     */
    fun fontCollection(
        includeSystem: Boolean,
        includeEmbedded: Boolean,
        fontPaths: List<String> = listOf()
    ): FontCollection {
        return FontCollection(
            compiler.fontCollection(includeSystem, includeEmbedded, fontPaths),
            this,
            FontsSpecification.NewFontCollection(includeSystem, includeEmbedded, fontPaths)
        )
    }

    /**
     * Construct a collection of fonts available to compiler operations.
     *
     * @param includeSystem Whether system-installed fonts should be loaded
     * @param includeEmbedded Whether embedded fallback fonts should be loaded
     * @param fontPaths Additional file-system paths to scan for fonts
     */
    fun fontCollection(
        includeSystem: Boolean = false,
        includeEmbedded: Boolean = true,
        vararg fontPaths: String
    ): FontCollection {
        return fontCollection(includeSystem, includeEmbedded, fontPaths.asList())
    }

    /**
     * Construct a collection of fonts available to compiler operations.
     *
     * @param fontPaths Additional file-system paths to scan for fonts
     */
    fun fontCollection(vararg fontPaths: String): FontCollection {
        return fontCollection(false, true, fontPaths.asList())
    }

    fun fontCollection(spec: FontsSpecification): FontCollectionReference {
        return resolveFonts(spec, this)
    }

    fun fontCollection(builder: FontsSpecBuilder.() -> Unit): FontCollectionReference {
        return fontCollection(FontsSpecBuilder().apply(builder).build())
    }

    /**
     * https://docs.rs/comemo/latest/comemo/fn.evict.html
     *
     * Evict the global cache.
     *
     * This removes all memoized results from the cache whose age is larger than or equal to `max_age`.
     * The age of a result grows by one during each eviction and is reset to zero when the result produces a cache hit.
     * Set `max_age` to zero to completely clear the cache.
     */
    fun evictCache(maxAge: Long) {
        compiler.evictCache(maxAge)
    }

    /**
     * Release all initialized default runtime resources and close the underlying core.
     */
    override fun close() {
        if (defaultFileCtxF.isInitialized()) defaultFileCtx.release()
        if (defaultFontsF.isInitialized()) defaultFonts.release()
        if (defaultLibraryF.isInitialized()) defaultLibrary.release()
        compiler.close()
    }
}
